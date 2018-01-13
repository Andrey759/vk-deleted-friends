package ru.friends.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.friends.model.domain.ChangeType;

import java.time.Instant;

@EqualsAndHashCode
@ToString
@Data
public class FriendChangeVo {

    FriendDataVo friendData;

    Instant detectTimeMin;

    Instant detectTimeMax;

    ChangeType changeType;

}
