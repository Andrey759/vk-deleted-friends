package ru.friends.model.dto.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.friends.model.domain.DeactivatedType;
import ru.friends.model.domain.RelationType;
import ru.friends.model.domain.SexType;

import javax.persistence.*;
import java.time.Instant;

@Entity
@EqualsAndHashCode(exclude = {"relationPartnerData"}, callSuper = true)
@ToString(exclude = {"relationPartnerData"}, callSuper = true)
@Data
public class FriendData extends AbstractData {

    @Column
    Instant lastUpdate;

    @Column
    boolean removed;

    @Column
    String nickname;

    @Enumerated(value = EnumType.STRING)
    @Column
    SexType sexType;

    @Enumerated(value = EnumType.STRING)
    @Column
    DeactivatedType deactivatedType;

    @Column
    String domain;

    @Column
    String photo50;

    @Enumerated(value = EnumType.STRING)
    @Column
    RelationType relationType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "relation_partner_data_id")
    RelationPartnerData relationPartnerData;

}
