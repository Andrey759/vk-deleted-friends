package ru.friends.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.friends.converter.DataChangeConverter;
import ru.friends.model.dto.DataChange;
import ru.friends.model.dto.User;
import ru.friends.model.dto.data.AbstractData;
import ru.friends.model.dto.data.FriendData;
import ru.friends.model.vo.DataChangeVo;
import ru.friends.repository.DataChangeRepository;
import ru.friends.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DataChangeService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    DataChangeRepository dataChangeRepository;
    @Autowired
    DataChangeConverter dataChangeConverter;

    @Transactional(readOnly = true)
    public Page<DataChangeVo> findByUserId(long userId, Pageable page) {
        User user = userRepository.findOne(userId);
        List<FriendData> friends = user.getFriends();
        List<Long> friendIds = friends.stream().map(AbstractData::getId).collect(Collectors.toList());
        Page<DataChange> resultDto = dataChangeRepository.findByDataIdInOrderByIdDesc(friendIds, page);
        return dataChangeConverter.toVo(resultDto);
    }

    @Transactional(readOnly = true)
    public Page<DataChangeVo> findByFriendRemoteId(long userId, long friendRemoteId, Pageable page) {
        User user = userRepository.findOne(userId);
        List<FriendData> friends = user.getFriends();
        Optional<FriendData> friendOptional = friends.stream().filter(friendData -> friendData.getRemoteId() == friendRemoteId).findAny();

        if (!friendOptional.isPresent())
            throw new RuntimeException("No friendRemoteId=" + friendRemoteId + " for userId=" + userId);

        Page<DataChange> resultDto = dataChangeRepository.findByDataIdOrderByIdDesc(friendOptional.get().getId(), page);
        return dataChangeConverter.toVo(resultDto);
    }

}
