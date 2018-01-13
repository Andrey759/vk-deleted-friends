package ru.friends.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@EqualsAndHashCode
@ToString
@Data
public class DataChangeVo {

    FriendDataVo friendData;

    Instant detectTimeMin;

    Instant detectTimeMax;

    String fieldName;

    String oldValue;

    String newValue;

}
