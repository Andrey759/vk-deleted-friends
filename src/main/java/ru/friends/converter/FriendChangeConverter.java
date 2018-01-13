package ru.friends.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.friends.model.dto.FriendChange;
import ru.friends.model.dto.data.FriendData;
import ru.friends.model.vo.FriendChangeVo;

@Component
public class FriendChangeConverter extends AbstractConverter<FriendChange, FriendChangeVo> {

    @Autowired
    FriendDataConverter friendDataConverter;

    public FriendChangeConverter() {
        super(FriendChange.class, FriendChangeVo.class);
    }

    @Override
    public FriendChangeVo toVo(FriendChange friendChange) {
        FriendChangeVo friendChangeVo = super.toVo(friendChange);

        FriendData friendData = friendChange.getFriendData();
        friendChangeVo.setFriendData(friendDataConverter.toVo(friendData));

        return friendChangeVo;
    }

}
