package ru.friends.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.friends.model.dto.FriendChange;
import ru.friends.model.dto.User;
import ru.friends.repository.FriendChangeRepository;
import ru.friends.repository.UserRepository;

import java.util.List;

@Service
public class FriendChangeService {

    @Autowired
    FriendChangeRepository friendChangeRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<FriendChange> findByUserId(Long userId) {
        User user = userRepository.findOne(userId);
        //return friendChangeRepository.findByUser(user);
        return friendChangeRepository.findAll();
    }

}
