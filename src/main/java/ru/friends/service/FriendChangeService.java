package ru.friends.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.friends.converter.FriendChangeConverter;
import ru.friends.model.domain.ChangeType;
import ru.friends.model.dto.FriendChange;
import ru.friends.model.vo.FriendChangeVo;
import ru.friends.repository.FriendChangeRepository;

@Service
public class FriendChangeService {

    @Autowired
    FriendChangeRepository friendChangeRepository;
    @Autowired
    FriendChangeConverter friendChangeConverter;

    @Transactional(readOnly = true)
    public Page<FriendChangeVo> findByUserId(long userId, Pageable page) {
        Page<FriendChange> resultDto = friendChangeRepository.findByUserIdOrderByIdDesc(userId, page);
        return friendChangeConverter.toVo(resultDto);
    }

    @Transactional(readOnly = true)
    public Page<FriendChangeVo> findByUserIdAndChangeType(long userId, ChangeType changeType, Pageable page) {
        Page<FriendChange> resultDto = friendChangeRepository.findByUserIdAndChangeTypeOrderByIdDesc(userId, changeType, page);
        return friendChangeConverter.toVo(resultDto);
    }

}
