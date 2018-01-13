package ru.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.friends.model.dto.data.FriendData;

public interface FriendDataRepository extends JpaRepository<FriendData, Long> {



}
