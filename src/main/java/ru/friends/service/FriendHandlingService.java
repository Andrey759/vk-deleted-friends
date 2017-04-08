package ru.friends.service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import ru.friends.model.domain.ChangeType;
import ru.friends.model.domain.IntervalType;
import ru.friends.model.dto.DataChange;
import ru.friends.model.dto.FriendChange;
import ru.friends.model.dto.User;
import ru.friends.model.dto.data.FriendData;
import ru.friends.model.dto.data.UserData;
import ru.friends.repository.DataChangeRepository;
import ru.friends.repository.FriendChangeRepository;
import ru.friends.repository.UserDataRepository;
import ru.friends.repository.UserRepository;
import ru.friends.util.EntityUtils;
import ru.friends.util.FutureUtils;
import ru.friends.util.StopWatchUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FriendHandlingService implements ApplicationContextAware {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDataRepository userDataRepository;
    @Autowired
    FriendChangeRepository friendChangeRepository;
    @Autowired
    DataChangeRepository dataChangeRepository;
    @Autowired
    ExternalRequestService externalRequestService;
    @Autowired
    TaskExecutor updateFriendsTaskExecutor;

    @Autowired
    FriendHandlingService self;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.self = applicationContext.getBean(FriendHandlingService.class);
    }

    @Transactional
    public void initFriendListForNewUser(long userId) {
        User user = userRepository.getOne(userId);
        List<FriendData> userFriends = externalRequestService.loadFriendsById(user.getId());
        user.setFriends(userFriends);
        user.setLastUpdate(Instant.now());
        userRepository.save(user);
    }

    @Transactional  // TODO: remove
    public void generateUsersAndLoadFriendsForEach() {
        StopWatch totalMetrics = new StopWatch();
        totalMetrics.start("totalTimer");
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            List<FriendData> friends = user.getFriends();
            friends.forEach(friend -> {
                User newUser = new User();
                newUser.setId(friend.getRemoteId());
                newUser.setPassHash("");
                newUser.setIntervalType(IntervalType.EVERY_NIGHT);
                newUser = userRepository.save(newUser);
                StopWatch initFriendsMetrics = new StopWatch();
                initFriendsMetrics.start("initFriendListForNewUser");
                initFriendListForNewUser(newUser.getId());
                initFriendsMetrics.stop();
                log.info(initFriendsMetrics.prettyPrint());
            });
        });
        totalMetrics.stop();
        log.info(totalMetrics.prettyPrint());
    }

    public void updateFriendsForUsersWithMetrics(IntervalType intervalType) {
        StopWatch stopWatch = StopWatchUtils.createAndStart(String.format("updateFriendsForUsers(%s)", intervalType.name()));

        self.updateFriendsForUsers(intervalType);

        log.info(StopWatchUtils.stopAndPrintSeconds(stopWatch));
    }

    public void updateFriendsForUsers(IntervalType intervalType) {
        List<User> users = userRepository.findByIntervalType(intervalType);
        List<CompletableFuture> futures = Lists.newArrayList();

        for (User user : users) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                    self.updateFriendsForUser(user.getId()), updateFriendsTaskExecutor);
            futures.add(future);
        }

        FutureUtils.checkExceptions(futures);
    }

    @Transactional
    public void updateFriendsForUser(long userId) {
        User user = userRepository.getOne(userId);
        List<FriendData> oldFriends = EntityUtils.copy(user.getFriends());
        List<FriendData> currentFriends = externalRequestService.loadFriendsById(user.getId());

        Map<Long, FriendData> oldFriendsMap = EntityUtils.groupByRemoteId(oldFriends);
        Map<Long, FriendData> currentFriendsMap = EntityUtils.groupByRemoteId(currentFriends);

        List<FriendData> friendsForSave = getUnion(oldFriends, currentFriendsMap);
        setRemovedIfNotContainsInCurrentFriends(friendsForSave, currentFriendsMap);
        setIdFromSavedInDb(friendsForSave, oldFriendsMap);
        user.setFriends(friendsForSave);
        Instant lastUpdate = user.getLastUpdate();
        user.setLastUpdate(Instant.now());

        final User savedUser = userRepository.save(user);
        Map<Long, FriendData> savedInDbFriendsMap = EntityUtils.groupByRemoteId(friendsForSave);

        Function<FriendData, FriendChange> addedMapper =
                friendData -> toFriendChange(friendData, ChangeType.ADDED, savedUser, lastUpdate);
        List<FriendChange> addedFriends = EntityUtils.getRightAndMap(oldFriendsMap, currentFriendsMap, addedMapper);
        EntityUtils.updateFriendChangeFromSavedInDb(addedFriends, savedInDbFriendsMap);
        friendChangeRepository.save(addedFriends);

        Function<FriendData, FriendChange> removedMapper =
                friendData -> toFriendChange(friendData, ChangeType.REMOVED, savedUser, lastUpdate);
        List<FriendChange> removedFriends = EntityUtils.getLeftAndMap(oldFriendsMap, currentFriendsMap, removedMapper);
        EntityUtils.updateFriendChangeFromSavedInDb(removedFriends, savedInDbFriendsMap);
        friendChangeRepository.save(removedFriends);


        List<Long> remainingFriendIds = EntityUtils.getIntersectionRemoteIds(oldFriendsMap, currentFriendsMap);
        List<DataChange> friendDataChanges = remainingFriendIds.stream()
                .map(remoteId -> toFriendDataChanges(
                        oldFriendsMap.get(remoteId), currentFriendsMap.get(remoteId)))
                .filter(changeList -> !Iterables.isEmpty(changeList))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        EntityUtils.updateDataChangeFromSavedInDb(friendDataChanges, savedInDbFriendsMap);
        dataChangeRepository.save(friendDataChanges);
    }

    private static void setRemovedIfNotContainsInCurrentFriends(List<FriendData> friendsForSave, Map<Long, FriendData> currentFriendsMap) {
        friendsForSave.forEach(friend -> friend.setRemoved(!currentFriendsMap.containsKey(friend.getRemoteId())));
    }

    private static void setIdFromSavedInDb(List<FriendData> friendsForSave, Map<Long, FriendData> savedInDbFriendsMap) {
        friendsForSave.forEach(friend -> friend.setId(savedInDbFriendsMap.get(friend.getRemoteId()).getId()));
    }

    private static List<FriendData> getUnion(List<FriendData> oldFriends, Map<Long, FriendData> currentFriendsMap) {
        Collection<FriendData> currentFriends = currentFriendsMap.values();
        Collection<FriendData> result = EntityUtils.copy(currentFriends);
        result.addAll(oldFriends.stream()
                .filter(currentFriend -> !currentFriendsMap.containsKey(currentFriend.getRemoteId()))
                .map(EntityUtils::copy)
                .collect(Collectors.toList()));
        return Lists.newArrayList(result);
    }

    private static FriendChange toFriendChange(FriendData friendData, ChangeType changeType, User user, Instant lastUpdate) {
        FriendChange friendChange = new FriendChange();
        friendChange.setUser(user);
        friendChange.setFriendData(friendData);
        friendChange.setDetectTimeMin(lastUpdate);
        friendChange.setDetectTimeMax(Instant.now());
        friendChange.setChangeType(changeType);
        return friendChange;
    }

    private static List<DataChange> toFriendDataChanges(FriendData oldData, FriendData newData) {

        if (oldData.getDeactivatedType() != newData.getDeactivatedType()) {
            return Collections.singletonList(toDeactivatedTypeDataChange(oldData, newData));
        }

        // TODO: fix for relationPartner change name or surname

        if (!EntityUtils.isEqualsById(oldData.getRelationPartnerData(), newData.getRelationPartnerData())) {
            return Collections.singletonList(toRelationPartnerDataChange(oldData, newData));
        }

        return compareAndReturnChanges(oldData, newData);
    }

    private static DataChange toDeactivatedTypeDataChange(FriendData oldData, FriendData newData) {
        DataChange dataChange = new DataChange();
        dataChange.setData(newData);
        dataChange.setDetectTimeMin(oldData.getLastUpdate());
        dataChange.setDetectTimeMax(newData.getLastUpdate());
        dataChange.setFieldName("deactivatedType");
        dataChange.setOldValue(
                oldData.getDeactivatedType() != null ? oldData.getDeactivatedType().name() : null);
        dataChange.setNewValue(
                newData.getDeactivatedType() != null ? newData.getDeactivatedType().name() : null);
        return dataChange;
    }

    private static DataChange toRelationPartnerDataChange(FriendData oldData, FriendData newData) {
        DataChange dataChange = new DataChange();
        dataChange.setData(newData);
        dataChange.setDetectTimeMin(oldData.getLastUpdate());
        dataChange.setDetectTimeMax(newData.getLastUpdate());
        dataChange.setFieldName("relationPartner");
        dataChange.setOldValue(
                oldData.getRelationPartnerData() != null ? "" + oldData.getRelationPartnerData().getId() : null);
        dataChange.setNewValue(
                newData.getRelationPartnerData() != null ? "" + newData.getRelationPartnerData().getId() : null);
        return dataChange;
    }

    private static final Javers JAVERS = JaversBuilder.javers().registerEntity(UserData.class).build();
    private static List<DataChange> compareAndReturnChanges(FriendData oldData, FriendData newData) {
        newData.setId(oldData.getId()); // TODO: need refactoring
        if (newData.getRelationPartnerData() != null) {
            newData.getRelationPartnerData().setId(oldData.getRelationPartnerData().getId());
        }
        Diff diff = JAVERS.compare(oldData, newData);
        return diff.getChanges().stream()
                .map(change -> toFriendDataChange(oldData, newData, change))
                .filter(userDataChange -> userDataChange != null)
                .collect(Collectors.toList());
    }

    private static DataChange toFriendDataChange(FriendData oldData, FriendData newData, Change change) {
        if (!(change instanceof ValueChange))
            return null;

        ValueChange valueChange = (ValueChange) change;

        if ("lastUpdate".equals(valueChange.getPropertyName()) || "removed".equals(valueChange.getPropertyName()))
            return null;

        DataChange userDataChange = new DataChange();
        userDataChange.setData(newData);
        userDataChange.setDetectTimeMin(oldData.getLastUpdate());
        userDataChange.setDetectTimeMax(newData.getLastUpdate());
        userDataChange.setFieldName(valueChange.getPropertyName());
        userDataChange.setOldValue(valueChange.getLeft().toString());
        userDataChange.setNewValue(valueChange.getRight().toString());

        return userDataChange;
    }

}
