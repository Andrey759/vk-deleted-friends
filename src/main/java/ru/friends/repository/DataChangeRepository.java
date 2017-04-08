package ru.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.friends.model.dto.DataChange;

public interface DataChangeRepository extends JpaRepository<DataChange, Long> {
}
