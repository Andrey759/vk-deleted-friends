package ru.friends.converter;

import com.google.common.base.Throwables;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.friends.model.dto.data.FriendData;
import ru.friends.model.dto.remote.RemoteUserData;
import ru.friends.model.vo.FriendDataVo;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FriendDataConverter extends AbstractConverter<FriendData, FriendDataVo> {

    @Autowired
    RelationPartnerDataConverter relationPartnerDataConverter;

    public FriendDataConverter() {
        super(FriendData.class, FriendDataVo.class);
    }

    public List<FriendData> toDtoFromRemoteUserData(Collection<RemoteUserData> remoteUserDataCollection) {
        return remoteUserDataCollection.stream().map(this::toDtoFromRemoteUserData).collect(Collectors.toList());
    }

    public FriendData toDtoFromRemoteUserData(RemoteUserData remoteUserData) {
        try {
            FriendData FriendData = dtoClass.newInstance();
            fillFriendDataColumns(remoteUserData, FriendData);

            return FriendData;
        } catch (InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    private void fillFriendDataColumns(RemoteUserData remoteUserData, FriendData friendData) {
        BeanUtils.copyProperties(remoteUserData, friendData);

        Arrays.asList(
                FriendData.class.getDeclaredFields()
        ).stream().filter(field -> Enum.class.isAssignableFrom(field.getType())).forEach(field -> {
            fillEnumField(field, remoteUserData, friendData);
        });

        friendData.setRemoteId(remoteUserData.getUid());
        friendData.setLastUpdate(Instant.now());
        friendData.setRemoved(false);
        friendData.setPhoto50(remoteUserData.getPhoto_50());
        if (remoteUserData.getRelationPartner() != null) {
            friendData.setRelationPartnerData(
                    relationPartnerDataConverter.toDtoFromRemoteUserData(remoteUserData.getRelationPartner())
            );
        }
    }

    @SuppressWarnings("unchecked")
    private static void fillEnumField(Field fieldTo, RemoteUserData remoteUserData, FriendData FriendData) {
        try {
            String remoteUserDataFieldName = fieldTo.getName().replace("Type", "");
            Field fieldFrom = RemoteUserData.class.getDeclaredField(remoteUserDataFieldName);

            fieldFrom.setAccessible(true);
            fieldTo.setAccessible(true);

            Class<Enum> enumClass = (Class<Enum>) fieldTo.getType();
            Class sourceClass = fieldFrom.getType();

            Enum enumValue = null;
            if (int.class.equals(sourceClass)) {
                enumValue = EnumConverter.getByNumber(fieldFrom.getInt(remoteUserData), enumClass);
            } else if (Integer.class.equals(sourceClass) && fieldFrom.get(remoteUserData) != null) {
                enumValue = EnumConverter.getByNumber((Integer) fieldFrom.get(remoteUserData), enumClass);
            } else if (String.class.equals(sourceClass)) {
                enumValue = EnumConverter.getByName((String) fieldFrom.get(remoteUserData), enumClass);
            }
            fieldTo.set(FriendData, enumValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public FriendDataVo toVo(FriendData friendData) {
        FriendDataVo friendDataVo = super.toVo(friendData);

        friendDataVo.setFirstAndLastName(String.format("%s %s", friendData.getFirstName(), friendData.getLastName()));
        friendDataVo.setUrl(String.format("https://vk.com/%s", friendData.getDomain()));

        return friendDataVo;
    }

}
