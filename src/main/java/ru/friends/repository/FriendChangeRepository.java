package ru.friends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.friends.model.domain.ChangeType;
import ru.friends.model.dto.FriendChange;

@Repository
public interface FriendChangeRepository extends JpaRepository<FriendChange, Long> {

    Page<FriendChange> findByUserIdOrderByIdDesc(long userId, Pageable page);

    Page<FriendChange> findByUserIdAndChangeTypeOrderByIdDesc(long userId, ChangeType changeType, Pageable page);

}
