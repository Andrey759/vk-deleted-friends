package ru.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.friends.model.dto.FriendChange;
import ru.friends.model.dto.User;

import java.util.List;

public interface FriendChangeRepository extends JpaRepository<FriendChange, Long> {

    List<FriendChange> findByUser(User user);

}
