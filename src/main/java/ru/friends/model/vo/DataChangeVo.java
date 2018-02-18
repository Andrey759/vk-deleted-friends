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

    String oldPhotoMaxOrig; // exists if "fieldName" is "photo50"

    String newPhotoMaxOrig; // exists if "fieldName" is "photo50"

}
