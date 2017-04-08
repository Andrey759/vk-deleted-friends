package ru.friends.model.dto.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class RelationPartnerData extends AbstractData {
}
