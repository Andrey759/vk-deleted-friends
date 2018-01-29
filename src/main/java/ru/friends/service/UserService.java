package ru.friends.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.friends.model.domain.IntervalType;
import ru.friends.model.dto.User;
import ru.friends.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendHandlingService friendHandlingService;

    @Value("${user.IntervalType.default:EVERY_HOUR}")
    IntervalType intervalType;

    @Transactional(readOnly = true)
    public boolean isUserExists(long viewerId) {
        return Optional.ofNullable(userRepository.findOne(viewerId)).isPresent();
    }

    @Transactional
    public void createAndHandleUser(long viewerId) {
        User user = new User();
        user.setId(viewerId);
        user.setIntervalType(intervalType);
        user.setLastEntry(Instant.now());
        User userSavedInDb = userRepository.save(user);
        friendHandlingService.updateFriendsForUser(userSavedInDb);
    }

    @Transactional
    public void updateLastEntryForUser(long viewerId) {
        User user = userRepository.findOne(viewerId);
        user.setLastEntry(Instant.now());
        userRepository.save(user);
    }

}
