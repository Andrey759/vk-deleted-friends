package ru.friends.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.friends.model.domain.IntervalType;

import java.time.Instant;

@Data
@EqualsAndHashCode
public class UserVo {

    long id;

    String passHash;

    IntervalType intervalType;

    Instant lastUpdate;

}
