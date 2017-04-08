package ru.friends.converter;

import org.springframework.stereotype.Component;
import ru.friends.model.dto.data.RelationPartnerData;
import ru.friends.model.dto.remote.RemoteUserData;
import ru.friends.model.vo.FriendDataVo;

@Component
public class RelationPartnerDataConverter extends AbstractConverter<RelationPartnerData, FriendDataVo> {

    public RelationPartnerDataConverter() {
        super(RelationPartnerData.class, FriendDataVo.class);
    }

    public RelationPartnerData toDtoFromRemoteUserData(RemoteUserData remoteUserData) {
        RelationPartnerData relationPartnerData = new RelationPartnerData();
        relationPartnerData.setRemoteId(remoteUserData.getUid());
        relationPartnerData.setFirstName(remoteUserData.getFirstName());
        relationPartnerData.setLastName(remoteUserData.getLastName());
        return relationPartnerData;
    }

}
