package ru.friends.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.friends.model.domain.IntervalType;
import ru.friends.model.dto.User;
import ru.friends.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendHandlingService friendHandlingService;

    @Autowired
    UserService self;

    @Value("${user.IntervalType.default:EVERY_HOUR}")
    IntervalType intervalType;

    public void checkAndCreateIfNeeded(long viewerId) {
        Optional<User> userOptional = self.getUserOptional(viewerId);

        if (!userOptional.isPresent())
            self.createAndHandleUser(viewerId);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserOptional(long viewerId) {
        return Optional.ofNullable(userRepository.findOne(viewerId));
    }

    @Transactional
    public void createAndHandleUser(long viewerId) {
        User user = new User();
        user.setId(viewerId);
        user.setIntervalType(intervalType);
        User userSavedInDb = userRepository.save(user);
        friendHandlingService.updateFriendsForUser(userSavedInDb);
    }

}
