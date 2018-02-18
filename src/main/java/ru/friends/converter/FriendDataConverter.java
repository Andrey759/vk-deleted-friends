package ru.friends.converter;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.vk.api.sdk.objects.base.BaseObject;
import com.vk.api.sdk.objects.users.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import ru.friends.model.domain.LifeMainType;
import ru.friends.model.domain.OpinionType;
import ru.friends.model.domain.PeopleMainType;
import ru.friends.model.domain.PoliticalType;
import ru.friends.model.dto.data.FriendData;
import ru.friends.model.vo.FriendDataVo;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FriendDataConverter extends AbstractConverter<FriendData, FriendDataVo> {

    private static final String PART_JOINER = ", ";
    private static final String LIST_JOINER = ";\n";
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
        //friendData.setBdate(userFull.getBdate());
        friendData.setCity(Optional.ofNullable(userFull.getCity()).map(BaseObject::getTitle).orElse(null));
        friendData.setCountry(Optional.ofNullable(userFull.getCity()).map(BaseObject::getTitle).orElse(null));
        //friendData.setHomeTown(userFull.getHomeTown());
        //friendData.setSite(userFull.getSite());
        //friendData.setUniversityName(userFull.getUniversityName());
        //friendData.setFacultyName(userFull.getFacultyName());
        //friendData.setGraduation(userFull.getGraduation());
        //friendData.setEducationForm(userFull.getEducationForm());
        //friendData.setEducationStatus(userFull.getEducationStatus());
        friendData.setUniversities(formatList(Optional.ofNullable(userFull.getUniversities()), FriendDataConverter::formatUniversity));
        friendData.setSchools(formatList(Optional.ofNullable(userFull.getSchools()), FriendDataConverter::formatSchool));
        //friendData.setStatus(userFull.getStatus());
        friendData.setOccupation(Optional.ofNullable(userFull.getOccupation()).map(Occupation::getName).orElse(null));

        friendData.setPoliticalType(formatIntegerEnum(Optional.ofNullable(userFull.getPersonal()).map(Personal::getPolitical), PoliticalType.values()));
        friendData.setReligion(Optional.ofNullable(userFull.getPersonal()).map(Personal::getReligion).orElse(""));
        friendData.setLifeMainType(formatIntegerEnum(Optional.ofNullable(userFull.getPersonal()).map(Personal::getLifeMain), LifeMainType.values()));
        friendData.setPeopleMainType(formatIntegerEnum(Optional.ofNullable(userFull.getPersonal()).map(Personal::getPeopleMain), PeopleMainType.values()));
        friendData.setSmokingType(formatIntegerEnum(Optional.ofNullable(userFull.getPersonal()).map(Personal::getSmoking), OpinionType.values()));
        friendData.setAlcoholType(formatIntegerEnum(Optional.ofNullable(userFull.getPersonal()).map(Personal::getAlcohol), OpinionType.values()));
        friendData.setInspiredBy(Optional.ofNullable(userFull.getPersonal()).map(Personal::getInspiredBy).orElse(""));

        friendData.setTwitter(Optional.ofNullable(userFull.getExports()).map(Exports::getTwitter).orElse(null));
        friendData.setFacebook(Optional.ofNullable(userFull.getExports()).map(Exports::getFacebook).orElse(null));
        friendData.setLivejournal(Optional.ofNullable(userFull.getExports()).map(Exports::getLivejournal).orElse(null));

        //friendData.setInstagram(userFull.getInstagram());
        //friendData.setHomePhone(userFull.getHomePhone());
        //friendData.setMobilePhone(userFull.getMobilePhone());

        //friendData.setActivities(userFull.getActivities());
        //friendData.setInterests(userFull.getInterests());
        //friendData.setMusic(userFull.getMusic());
        //friendData.setMovies(userFull.getMovies());
        //friendData.setTv(userFull.getTv());
        //friendData.setBooks(userFull.getBooks());
        //friendData.setGames(userFull.getGames());
        //friendData.setAbout(userFull.getAbout());
        //friendData.setQuotes(userFull.getQuotes());
        //friendData.setMaidenName(userFull.getMaidenName());

        friendData.setCareer(formatList(Optional.ofNullable(userFull.getCareer()), FriendDataConverter::formatCareer));
        friendData.setMilitary(formatList(Optional.ofNullable(userFull.getMilitary()), FriendDataConverter::formatMilitary));

        friendData.setRelationPartnerData(Optional.ofNullable(userFull.getRelationPartner())
                .map(relationPartnerDataConverter::toRelationPartnerDataFromUserMin).orElse(null));
    }

    private static <T> String formatList(Optional<List<T>> listOptional, Function<T, String> func) {
        return StringUtils.join(listOptional.map(list -> list.stream().filter(Objects::nonNull).map(func)
                .filter(Objects::nonNull).collect(Collectors.toList())).orElse(Collections.emptyList()), LIST_JOINER);
    }

    private static String formatUniversity(University university) {
        if (StringUtils.isEmpty(university.getName()))
            return null;

        return new StringBuilder(university.getName())
                .append(StringUtils.isEmpty(university.getFacultyName())     ? "" : PART_JOINER + university.getFaculty())
                .append(StringUtils.isEmpty(university.getChairName())       ? "" : PART_JOINER + university.getChairName())
                .append(StringUtils.isEmpty(university.getEducationForm())   ? "" : PART_JOINER + university.getEducationForm())
                .append(StringUtils.isEmpty(university.getEducationStatus()) ? "" : PART_JOINER + university.getEducationStatus())
                .toString();
    }

    private static String formatSchool(School school) {
        if (StringUtils.isEmpty(school.getName()))
            return null;

        return new StringBuilder(school.getName())
                .append(StringUtils.isEmpty(school.getClassName())  ? "" : ", класс " + school.getClassName())
                .append(school.getYearFrom() == null || school.getYearTo() == null ? ""
                                                                    : PART_JOINER + school.getYearFrom() + " - " + school.getYearTo())
                .append(school.getYearGraduated() == null           ? "" : PART_JOINER + "год окончания " + school.getYearGraduated())
                .toString();
    }

    private static String formatCareer(Career career) {
        if (StringUtils.isEmpty(career.getCompany()) && StringUtils.isEmpty(career.getPosition()))
            return null;

        return new StringBuilder(career.getCompany() == null ? "" : career.getCompany())
                .append(StringUtils.isEmpty(career.getCompany()) || StringUtils.isEmpty(career.getPosition()) ? "" : PART_JOINER)
                .append(StringUtils.isEmpty(career.getPosition()) ? "" : career.getPosition())
                .append(career.getFrom() == null || career.getUntil() == null ? "" : PART_JOINER + career.getFrom() + " - " + career.getUntil())
                .toString();
    }

    private static String formatMilitary(Military military) {
        if (StringUtils.isEmpty(military.getUnit()))
            return null;

        return new StringBuilder(military.getUnit())
                .append(military.getFrom() == null || military.getUnit() == null ? "" : PART_JOINER + military.getFrom() + " - " + military.getUntil())
                .toString();
    }

    private static <T extends Enum> T formatIntegerEnum(Optional<Integer> indexOptional, T[] valuesArray) {
        List<T> values = Arrays.asList(valuesArray);

        indexOptional = indexOptional.map(index -> --index);

        if (indexOptional.isPresent() && indexOptional.get() >= values.size()) {
            log.error("Can't parse value: " + indexOptional.get()
                    + " for class " + values.get(0).getClass().getSimpleName());
            return null;
        }

        return indexOptional.map(values::get).orElse(null);
    }

    @SuppressWarnings("unchecked")
    private static void fillEnumField(Field fieldTo, UserFull userFull, FriendData FriendData) {
        try {
            String fieldName = fieldTo.getName().replace("Type", "");

            if (!FIELD_MAP.containsKey(fieldName))
                return;

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
