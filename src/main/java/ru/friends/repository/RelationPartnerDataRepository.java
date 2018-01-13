package ru.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.friends.model.dto.data.RelationPartnerData;

public interface RelationPartnerDataRepository extends JpaRepository<RelationPartnerData, Long> {

}
