package ru.friends.converter;

import com.vk.api.sdk.objects.users.UserMin;
import org.springframework.stereotype.Component;
import ru.friends.model.dto.data.RelationPartnerData;
import ru.friends.model.dto.remote.RemoteUserData;
import ru.friends.model.vo.FriendDataVo;

@Component
public class RelationPartnerDataConverter extends AbstractConverter<RelationPartnerData, FriendDataVo> {

    public RelationPartnerDataConverter() {
        super(RelationPartnerData.class, FriendDataVo.class);
    }

    public RelationPartnerData toRelationPartnerDataFromUserMin(UserMin userMin) {
        RelationPartnerData relationPartnerData = new RelationPartnerData();
        relationPartnerData.setRemoteId(userMin.getId());
        relationPartnerData.setFirstName(userMin.getFirstName());
        relationPartnerData.setLastName(userMin.getLastName());
        return relationPartnerData;
    }

}
