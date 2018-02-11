package ru.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.friends.model.domain.IntervalType;
import ru.friends.model.dto.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIntervalType(IntervalType intervalType);

}