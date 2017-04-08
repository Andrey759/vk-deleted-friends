package ru.friends.converter;

import org.springframework.stereotype.Component;
import ru.friends.model.dto.User;
import ru.friends.model.vo.UserVo;

@Component
public class UserConverter extends AbstractConverter<User, UserVo> {

    public UserConverter() {
        super(User.class, UserVo.class);
    }

}
