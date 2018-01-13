package ru.friends.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class FriendDataVo {

    long remoteId;

    String firstAndLastName;

    Enum sexType;

    String url;

    String photo50;

}
