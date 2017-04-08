package ru.friends.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.friends.model.domain.ChangeType;
import ru.friends.model.dto.data.FriendData;

import javax.persistence.*;
import java.time.Instant;

@Entity
@EqualsAndHashCode
@ToString
@Data
public class FriendChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "friend_data_id")
    FriendData friendData;

    @Column
    Instant detectTimeMin;

    @Column
    Instant detectTimeMax;

    @Enumerated(value = EnumType.STRING)
    @Column
    ChangeType changeType;

}
