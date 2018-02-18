package ru.friends.util;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import ru.friends.model.dto.DataChange;
import ru.friends.model.dto.FriendChange;
import ru.friends.model.dto.data.AbstractData;
import ru.friends.model.dto.data.FriendData;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class EntityUtils {

    public static void updateFriendChangeFromSavedInDb(List<FriendChange> changes,  Map<Long, FriendData> savedMap) {
        changes.forEach(change -> change.setFriendData(savedMap.get(change.getFriendData().getRemoteId())));
    }

    public static void updateDataChangeFromSavedInDb(List<DataChange> dataList,  Map<Long, FriendData> savedMap) {
        dataList.forEach(data -> data.setData(savedMap.get(data.getData().getRemoteId())));
    }

    public static <T extends AbstractData> Map<Long, T> groupByRemoteId(List<T> sourceList) {
        return sourceList.stream()
                .collect(Collectors.toMap(AbstractData::getRemoteId, abstractData -> abstractData));
    }

    public static List<FriendChange> getLeftAndMap(
            Map<Long, FriendData> left, Map<Long, FriendData> right, Function<FriendData, FriendChange> mapper) {
        return getRightAndMap(right, left, mapper);
    }

    public static List<FriendChange> getRightAndMap(
            Map<Long, FriendData> left, Map<Long, FriendData> right, Function<FriendData, FriendChange> mapper) {
        return right.values().stream()
                .filter(friendData -> !friendData.isRemoved())
                .filter(friendData -> !isFriendExist(friendData, left))
                .map(mapper)
                .collect(Collectors.toList());
    }

    private static boolean isFriendExist(FriendData friendData, Map<Long, FriendData> map) {
        return map.containsKey(friendData.getRemoteId()) && !map.get(friendData.getRemoteId()).isRemoved();
    }

    public static List<Long> getIntersectionRemoteIdsIfNotRemoved(Map<Long, FriendData> left, Map<Long, FriendData> right) {
        return left.keySet().stream()
                .filter(right::containsKey)
                .filter(remoteId -> !right.get(remoteId).isRemoved())
                .collect(Collectors.toList());
    }

    public static boolean isEqualsById(AbstractData first, AbstractData second) {
        if (first == null && second != null)
            return false;

        if (first != null && second == null)
            return false;

        if (first != null && second != null && first.getRemoteId() != second.getRemoteId())
            return false;

        return true;
    }

    @SneakyThrows
    public static <T> T copy(T source) {
        if (source instanceof Collection) {
            return (T) tryCopyList((Collection) source);
        } else {
            return tryCopy(source);
        }
    }

    private static List tryCopyList(Collection source) throws IllegalAccessException, InstantiationException {
        List copy = Lists.newArrayList();
        source.forEach(e -> copy.add(copy(e)));
        return copy;
    }

    private static <T> T tryCopy(T source) throws IllegalAccessException, InstantiationException {
        T copy = (T) source.getClass().newInstance();
        BeanUtils.copyProperties(source, copy);
        return copy;
    }

}
