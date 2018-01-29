package ru.friends.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.friends.model.domain.IntervalType;
import ru.friends.model.dto.data.FriendData;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(schema = "public")
@EqualsAndHashCode(exclude = "friends")
//@ToString(exclude = {"userData", "friends"})
@ToString(exclude = {"friends"})
@Data
public class User {

    @Id
    Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    IntervalType intervalType;

    @Column
    Instant lastUpdate;

    @Column
    Instant lastEntry;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    List<FriendData> friends;

}
