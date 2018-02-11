package ru.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.friends.model.dto.data.RelationPartnerData;

@Repository
public interface RelationPartnerDataRepository extends JpaRepository<RelationPartnerData, Long> {

}
