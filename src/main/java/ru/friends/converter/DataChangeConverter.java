package ru.friends.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import ru.friends.model.domain.*;
import ru.friends.model.dto.DataChange;
import ru.friends.model.dto.data.FriendData;
import ru.friends.model.dto.data.RelationPartnerData;
import ru.friends.model.vo.DataChangeVo;
import ru.friends.repository.RelationPartnerDataRepository;

import java.util.Optional;

@Component
public class DataChangeConverter extends AbstractConverter<DataChange, DataChangeVo> {

    public static final String PHOTO_SEPARATOR = "\n";

    @Autowired
    RelationPartnerDataRepository relationPartnerDataRepository;
    @Autowired
    FriendDataConverter friendDataConverter;
    @Autowired
    MessageSource messageSource;

    public DataChangeConverter() {
        super(DataChange.class, DataChangeVo.class);
    }

    @Override
    public DataChangeVo toVo(DataChange dataChange) {
        DataChangeVo dataChangeVo = super.toVo(dataChange);

        dataChangeVo.setFriendData(friendDataConverter.toVo((FriendData) dataChange.getData()));
        if (FriendData.PHOTO_50.equals(dataChange.getFieldName())) {
            String oldValue = Optional.ofNullable(dataChange.getOldValue()).orElse("");
            String newValue = Optional.ofNullable(dataChange.getNewValue()).orElse("");
            if (oldValue.contains(PHOTO_SEPARATOR)) {
                String[] oldValues = oldValue.split(PHOTO_SEPARATOR);
                dataChangeVo.setOldValue(oldValues[0]);
                dataChangeVo.setOldPhotoMaxOrig(oldValues[1]);
            } else {
                dataChangeVo.setOldValue(oldValue);
            }
            if (newValue.contains(PHOTO_SEPARATOR)) {
                String[] newValues = newValue.split(PHOTO_SEPARATOR);
                dataChangeVo.setNewValue(newValues[0]);
                dataChangeVo.setNewPhotoMaxOrig(newValues[1]);
            } else {
                dataChangeVo.setNewValue(newValue);
            }
        } else {
            dataChangeVo.setOldValue(formatValue(dataChange.getFieldName(), dataChange.getOldValue()));
            dataChangeVo.setNewValue(formatValue(dataChange.getFieldName(), dataChange.getNewValue()));
        }

        return dataChangeVo;
    }

    private String formatValue(String fieldName, String value) {
        if (StringUtils.isEmpty(value) || "null".equals(value))
            return "";

        switch (fieldName) {
            case FriendData.POLITICAL_TYPE:
                return formatEnum(PoliticalType.class, value);
            case FriendData.LIFE_MAIN_TYPE:
                return formatEnum(LifeMainType.class, value);
            case FriendData.PEOPLE_MAIN_TYPE:
                return formatEnum(PeopleMainType.class, value);
            case FriendData.SMOKING_TYPE:
                return formatEnum(OpinionType.class, value);
            case FriendData.ALCOHOL_TYPE:
                return formatEnum(OpinionType.class, value);
            case FriendData.DEACTIVATED_TYPE:
                return formatEnum(DeactivatedType.class, value);
            case FriendData.RELATION_TYPE:
                return formatEnum(RelationType.class, value);
            case FriendData.RELATION_PARTNER_DATA:
                RelationPartnerData partner = relationPartnerDataRepository.findOne(Long.valueOf(value));
                return partner.getFirstName() + " " + partner.getLastName();
            case FriendData.SEX_TYPE:
                return formatEnum(SexType.class, value);
            default:
                return value;
        }
    }

    private static final String TEMPLATE = "enum.%s.%s";

    private String formatEnum(Class<? extends Enum> clazz, String value) {
        String str = String.format(TEMPLATE, clazz.getSimpleName(), value);
        return messageSource.getMessage(str, new Object[0], str, LocaleContextHolder.getLocale());
    }

}
