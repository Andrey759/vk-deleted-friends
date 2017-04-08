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

    public List<FriendData> toDtoFromRemoteUserData(Collection<RemoteUserData> RemoteUserDataCollection) {
        return RemoteUserDataCollection.stream().map(this::toDtoFromRemoteUserData).collect(Collectors.toList());
    }

    public FriendData toDtoFromRemoteUserData(RemoteUserData RemoteUserData) {
        try {
            FriendData FriendData = dtoClass.newInstance();
            fillFriendDataColumns(RemoteUserData, FriendData);
            return FriendData;
        } catch (InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    private void fillFriendDataColumns(RemoteUserData RemoteUserData, FriendData friendData) {
        BeanUtils.copyProperties(RemoteUserData, friendData);

        Arrays.asList(
                FriendData.class.getDeclaredFields()
        ).stream().filter(field -> Enum.class.isAssignableFrom(field.getType())).forEach(field -> {
            fillEnumField(field, RemoteUserData, friendData);
        });

        friendData.setRemoteId(RemoteUserData.getUid());
        friendData.setLastUpdate(Instant.now());
        friendData.setRemoved(false);
        friendData.setPhoto50(RemoteUserData.getPhoto_50());
        if (RemoteUserData.getRelationPartner() != null) {
            friendData.setRelationPartnerData(
                    relationPartnerDataConverter.toDtoFromRemoteUserData(RemoteUserData.getRelationPartner())
            );
        }
    }

    @SuppressWarnings("unchecked")
    private static void fillEnumField(Field fieldTo, RemoteUserData RemoteUserData, FriendData FriendData) {
        try {
            String RemoteUserDataFieldName = fieldTo.getName().replace("Type", "");
            Field fieldFrom = RemoteUserData.class.getDeclaredField(RemoteUserDataFieldName);

            fieldFrom.setAccessible(true);
            fieldTo.setAccessible(true);

            Class<Enum> enumClass = (Class<Enum>) fieldTo.getType();
            Class sourceClass = fieldFrom.getType();

            Enum enumValue = null;
            if (int.class.equals(sourceClass)) {
                enumValue = EnumConverter.getByNumber(fieldFrom.getInt(RemoteUserData), enumClass);
            } else if (Integer.class.equals(sourceClass) && fieldFrom.get(RemoteUserData) != null) {
                enumValue = EnumConverter.getByNumber((Integer) fieldFrom.get(RemoteUserData), enumClass);
            } else if (String.class.equals(sourceClass)) {
                enumValue = EnumConverter.getByName((String) fieldFrom.get(RemoteUserData), enumClass);
            }
            fieldTo.set(FriendData, enumValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

}
