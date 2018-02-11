package ru.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.friends.model.dto.data.FriendData;

@Repository
public interface FriendDataRepository extends JpaRepository<FriendData, Long> {



}
