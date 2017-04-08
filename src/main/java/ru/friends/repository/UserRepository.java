package ru.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.friends.model.domain.IntervalType;
import ru.friends.model.dto.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIntervalType(IntervalType intervalType);

}