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
import ru.friends.model.dto.data.RelationPartnerData;
import ru.friends.model.dto.data.UserData;
import ru.friends.repository.DataChangeRepository;
import ru.friends.repository.FriendChangeRepository;
import ru.friends.repository.UserRepository;
import ru.friends.util.EntityUtils;
import ru.friends.util.FutureUtils;
import ru.friends.util.StopWatchUtils;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FriendHandlingService implements ApplicationContextAware {

    @Autowired
    UserRepository userRepository;
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
        updateFriendsForUser(user);
    }

    public void updateFriendsForUser(User user) {
        List<FriendData> oldFriends = EntityUtils.copy(Optional.ofNullable(user.getFriends()).orElse(Collections.emptyList()));
        List<FriendData> currentFriends = externalRequestService.loadFriendsById(user.getId());

        Map<Long, FriendData> oldFriendsMap = EntityUtils.groupByRemoteId(oldFriends);
        Map<Long, FriendData> currentFriendsMap = EntityUtils.groupByRemoteId(currentFriends);

        List<FriendData> friendsForSave = getUnion(oldFriends, currentFriendsMap);
        setRemovedIfNotContainsInCurrentFriends(friendsForSave, currentFriendsMap);
        setIdFromSavedInDb(friendsForSave, oldFriendsMap);
        setRelationPartnerIdFromSavedInDb(friendsForSave, oldFriendsMap);
        setNullRelationPartnerIdIfNameChanged(friendsForSave, oldFriendsMap);
        user.setFriends(friendsForSave);
        Instant lastUpdate = user.getLastUpdate();
        user.setLastUpdate(Instant.now());

        final User savedUser = userRepository.save(user);
        Map<Long, FriendData> savedInDbCurrentFriendsMap = EntityUtils.groupByRemoteId(EntityUtils.copy(friendsForSave));
        if (lastUpdate == null)
            return;

        Function<FriendData, FriendChange> addedMapper =
                friendData -> toFriendChange(friendData, ChangeType.ADDED, savedUser, lastUpdate);
        List<FriendChange> addedFriends = EntityUtils.getRightAndMap(oldFriendsMap, savedInDbCurrentFriendsMap, addedMapper);
        friendChangeRepository.save(addedFriends);

        Function<FriendData, FriendChange> removedMapper =
                friendData -> toFriendChange(friendData, ChangeType.REMOVED, savedUser, lastUpdate);
        List<FriendChange> removedFriends = EntityUtils.getLeftAndMap(oldFriendsMap, savedInDbCurrentFriendsMap, removedMapper);
        friendChangeRepository.save(removedFriends);


        List<Long> remainingFriendIds = EntityUtils.getIntersectionRemoteIdsIfNotRemoved(oldFriendsMap, savedInDbCurrentFriendsMap);
        List<DataChange> friendDataChanges = remainingFriendIds.stream()
                .map(remoteId -> toFriendDataChanges(
                        oldFriendsMap.get(remoteId), savedInDbCurrentFriendsMap.get(remoteId)))
                .filter(changeList -> !Iterables.isEmpty(changeList))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        dataChangeRepository.save(friendDataChanges);
    }

    private static void setRemovedIfNotContainsInCurrentFriends(List<FriendData> friendsForSave, Map<Long, FriendData> currentFriendsMap) {
        friendsForSave.forEach(friend -> friend.setRemoved(!currentFriendsMap.containsKey(friend.getRemoteId())));
    }

    private static void setIdFromSavedInDb(List<FriendData> friendsForSave, Map<Long, FriendData> savedInDbFriendsMap) {
        friendsForSave.stream()
                .filter(friend -> savedInDbFriendsMap.containsKey(friend.getRemoteId()))
                .forEach(friend -> friend.setId(savedInDbFriendsMap.get(friend.getRemoteId()).getId()));
    }

    private static void setRelationPartnerIdFromSavedInDb(List<FriendData> friendsForSave, Map<Long, FriendData> savedInDbFriendsMap) {
        friendsForSave.stream()
                .filter(friend -> friend.getRelationPartnerData() != null)
                .filter(friend -> friend.getRelationPartnerData().getId() == null)
                .filter(friend -> savedInDbFriendsMap.containsKey(friend.getRemoteId()))
                .filter(friend -> savedInDbFriendsMap.get(friend.getRemoteId()).getRelationPartnerData() != null)
                .forEach(friend -> friend.getRelationPartnerData().setId(
                        savedInDbFriendsMap.get(friend.getRemoteId()).getRelationPartnerData().getId()
                ));
    }

    private static void setNullRelationPartnerIdIfNameChanged(List<FriendData> friendsForSave, Map<Long, FriendData> savedInDbFriendsMap) {
        friendsForSave.stream()
                .filter(friend -> friend.getRelationPartnerData() != null)
                .filter(friend -> friend.getRelationPartnerData().getId() != null)
                .filter(friend -> {
                    FriendData savedFriend = savedInDbFriendsMap.get(friend.getRemoteId());
                    RelationPartnerData partner = friend.getRelationPartnerData();
                    RelationPartnerData savedPartner = savedFriend.getRelationPartnerData();
                    return !Objects.equals(partner.getFirstName(), savedPartner.getFirstName())
                            || !Objects.equals(partner.getLastName(), savedPartner.getLastName());
                })
                .forEach(friend -> friend.getRelationPartnerData().setId(null));
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

        List<DataChange> friendDataChanges = Lists.newArrayList();

        if (oldData.getDeactivatedType() != newData.getDeactivatedType()) {
            friendDataChanges.add(toDeactivatedTypeDataChange(oldData, newData));
        }

        if (!EntityUtils.isEqualsById(oldData.getRelationPartnerData(), newData.getRelationPartnerData())) {
            friendDataChanges.add(toRelationPartnerDataChange(oldData, newData));
        }

        friendDataChanges.addAll(compareAndReturnChanges(oldData, newData));
        return friendDataChanges;
    }

    private static DataChange toDeactivatedTypeDataChange(FriendData oldData, FriendData newData) {
        DataChange dataChange = new DataChange();
        dataChange.setData(newData);
        dataChange.setDetectTimeMin(oldData.getLastUpdate());
        dataChange.setDetectTimeMax(newData.getLastUpdate());
        dataChange.setFieldName(FriendData.DEACTIVATED_TYPE);
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
        dataChange.setFieldName(FriendData.RELATION_PARTNER_DATA);
        dataChange.setOldValue(
                oldData.getRelationPartnerData() != null ? "" + oldData.getRelationPartnerData().getId() : null);
        dataChange.setNewValue(
                newData.getRelationPartnerData() != null ? "" + newData.getRelationPartnerData().getId() : null);
        return dataChange;
    }

    private static final Javers JAVERS = JaversBuilder.javers().registerEntity(UserData.class).build();
    private static List<DataChange> compareAndReturnChanges(FriendData oldData, FriendData newData) {
        Diff diff = JAVERS.compare(oldData, newData);
        return diff.getChanges().stream()
                .map(change -> toFriendDataChange(oldData, newData, change))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static final Pattern PHOTO_PATTERN = Pattern.compile("^https://[a-z0-9.]+/([A-z0-9./]+)$");

    private static DataChange toFriendDataChange(FriendData oldData, FriendData newData, Change change) {
        if (!(change instanceof ValueChange))
            return null;

        ValueChange valueChange = (ValueChange) change;

        if (FriendData.LAST_UPDATE.equals(valueChange.getPropertyName())
                || FriendData.REMOVED.equals(valueChange.getPropertyName())
                || FriendData.RELATION_PARTNER_DATA.equals(valueChange.getPropertyName()))
            return null;

        Matcher oldPhotoMather = PHOTO_PATTERN.matcher(Optional.ofNullable(oldData.getPhoto50()).orElse(""));
        Matcher newPhotoMather = PHOTO_PATTERN.matcher(Optional.ofNullable(newData.getPhoto50()).orElse(""));
        if (FriendData.PHOTO_50.equals(valueChange.getPropertyName())
                && oldPhotoMather.matches()
                && newPhotoMather.matches()
                && oldPhotoMather.group(1).equals(newPhotoMather.group(1)))
            return null;

        String left = Optional.ofNullable(valueChange.getLeft()).map(Object::toString).orElse("");
        String right = Optional.ofNullable(valueChange.getRight()).map(Object::toString).orElse("");

        if (left.equals(right)) // null and empty string for example
            return null;

        DataChange userDataChange = new DataChange();
        userDataChange.setData(newData);
        userDataChange.setDetectTimeMin(oldData.getLastUpdate());
        userDataChange.setDetectTimeMax(newData.getLastUpdate());
        userDataChange.setFieldName(valueChange.getPropertyName());
        userDataChange.setOldValue(Optional.ofNullable(valueChange.getLeft()).map(Object::toString).orElse(""));
        userDataChange.setNewValue(Optional.ofNullable(valueChange.getRight()).map(Object::toString).orElse(""));

        return userDataChange;
    }

}
