package ru.friends.model.dto.remote;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode
@ToString
@Data
public class RemoteUserData {

    long uid;
    String firstName;
    String lastName;
    String nickname;
    int sex;
    String deactivated;
    String domain;
    String photo_50;
    int hidden;
    int relation;
    RemoteUserData relationPartner;


    // relationPartner has id, firstName, lastName
    public void setId(long id) {
        this.uid = id;
    }

    // userId is duplicate of uid
    public void setUserId(long userId) {
        if (this.uid != userId) {
            log.warn("uid is not equal userId [uid:{}, userId:{}]", uid, userId);
        }
    }

}
