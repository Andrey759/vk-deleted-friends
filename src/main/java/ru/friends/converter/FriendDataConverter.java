package ru.friends.converter;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vk.api.sdk.objects.base.BaseObject;
import com.vk.api.sdk.objects.users.Occupation;
import com.vk.api.sdk.objects.users.User;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.objects.users.UserMin;
import org.mockito.internal.util.reflection.Fields;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.friends.model.dto.data.FriendData;
import ru.friends.model.dto.remote.RemoteUserData;
import ru.friends.model.vo.FriendDataVo;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FriendDataConverter extends AbstractConverter<FriendData, FriendDataVo> {

    private static final Map<String, Field> FIELD_MAP;
    static {
        List<Field> fieldList = Lists.newArrayList();
        fieldList.addAll(Arrays.asList(UserFull.class.getDeclaredFields()));
        fieldList.addAll(Arrays.asList(User.class.getDeclaredFields()));
        fieldList.addAll(Arrays.asList(UserMin.class.getDeclaredFields()));
        FIELD_MAP = fieldList.stream().collect(Collectors.toMap(Field::getName, field -> field));
    }

    @Autowired
    RelationPartnerDataConverter relationPartnerDataConverter;

    public FriendDataConverter() {
        super(FriendData.class, FriendDataVo.class);
    }

    public List<FriendData> toFriendDataFromFullUser(List<UserFull> userFullList) {
        return userFullList.stream().map(this::toFriendDataFromFullUser).collect(Collectors.toList());
    }

    public FriendData toFriendDataFromFullUser(UserFull userFull) {
        try {
            FriendData FriendData = dtoClass.newInstance();
            fillFriendDataColumns(userFull, FriendData);

            return FriendData;
        } catch (InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    private void fillFriendDataColumns(UserFull userFull, FriendData friendData) {
        BeanUtils.copyProperties(userFull, friendData);

        Arrays.stream(
                FriendData.class.getDeclaredFields()
        ).filter(field -> Enum.class.isAssignableFrom(field.getType())).forEach(field -> {
            fillEnumField(field, userFull, friendData);
        });

        friendData.setRemoteId(userFull.getId());
        friendData.setLastUpdate(Instant.now());
        friendData.setRemoved(false);
        //friendData.setBDate(Optional.ofNullable(userFull.getBdate()).map().orElse(null));
        friendData.setCity(Optional.ofNullable(userFull.getCity()).map(BaseObject::getTitle).orElse(null));
        friendData.setCountry(Optional.ofNullable(userFull.getCity()).map(BaseObject::getTitle).orElse(null));
        friendData.setHomeTown(userFull.getHomeTown());
        friendData.setOccupation(Optional.ofNullable(userFull.getOccupation()).map(Occupation::getName).orElse(null));
        friendData.setRelationPartnerData(Optional.ofNullable(userFull.getRelationPartner())
                .map(relationPartnerDataConverter::toRelationPartnerDataFromUserMin).orElse(null));
    }

    @SuppressWarnings("unchecked")
    private static void fillEnumField(Field fieldTo, UserFull userFull, FriendData FriendData) {
        try {
            String fieldName = fieldTo.getName().replace("Type", "");

            Field fieldFrom = FIELD_MAP.get(fieldName);

            fieldFrom.setAccessible(true);
            fieldTo.setAccessible(true);

            Class<Enum> enumClass = (Class<Enum>) fieldTo.getType();
            Class sourceClass = fieldFrom.getType();

            Enum enumValue = null;
            if (fieldFrom.get(userFull) == null) {
                // nothing
            } else if (int.class.equals(sourceClass)) {
                enumValue = EnumConverter.getByNumber(fieldFrom.getInt(userFull), enumClass);
            } else if (Integer.class.equals(sourceClass)) {
                enumValue = EnumConverter.getByNumber((Integer) fieldFrom.get(userFull), enumClass);
            } else if (String.class.equals(sourceClass)) {
                enumValue = EnumConverter.getByName((String) fieldFrom.get(userFull), enumClass);
            } else if (sourceClass.isEnum()) {
                String stringValue = ((Enum) fieldFrom.get(userFull)).name();
                enumValue = EnumConverter.getByName(stringValue, enumClass);
            }
            fieldTo.set(FriendData, enumValue);
        } catch (IllegalAccessException e) {
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
