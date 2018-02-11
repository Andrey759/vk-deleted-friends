package ru.friends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.friends.model.dto.DataChange;

import java.util.List;

@Repository
public interface DataChangeRepository extends JpaRepository<DataChange, Long> {

    Page<DataChange> findByDataIdOrderByIdDesc(long dataId, Pageable page);
    
    Page<DataChange> findByDataIdInOrderByIdDesc(List<Long> dataIds, Pageable page);

}
