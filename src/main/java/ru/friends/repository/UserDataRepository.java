package ru.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.friends.model.dto.data.UserData;

import java.util.List;

public interface UserDataRepository extends JpaRepository<UserData, Long> {

    List<UserData> findByIdIn(List<Long> Id);

}
