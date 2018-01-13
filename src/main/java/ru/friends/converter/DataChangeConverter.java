package ru.friends.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import ru.friends.model.domain.DeactivatedType;
import ru.friends.model.domain.RelationType;
import ru.friends.model.domain.SexType;
import ru.friends.model.dto.DataChange;
import ru.friends.model.dto.data.FriendData;
import ru.friends.model.dto.data.RelationPartnerData;
import ru.friends.model.vo.DataChangeVo;
import ru.friends.repository.RelationPartnerDataRepository;

@Component
public class DataChangeConverter extends AbstractConverter<DataChange, DataChangeVo> {

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
        dataChangeVo.setOldValue(formatValue(dataChange.getFieldName(), dataChange.getOldValue()));
        dataChangeVo.setNewValue(formatValue(dataChange.getFieldName(), dataChange.getNewValue()));

        return dataChangeVo;
    }

    private String formatValue(String fieldName, String value) {
        switch (fieldName) {
            case FriendData.DEACTIVATED_TYPE:
                return formatEnum(DeactivatedType.class, value);
            case FriendData.RELATION_TYPE:
                return formatEnum(RelationType.class, value);
            case FriendData.RELATION_PARTNER_DATA:
                if (value == null || "null".equals(value))
                    return "";

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
