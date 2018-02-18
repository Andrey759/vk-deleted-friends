package ru.friends.model.dto.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.friends.model.domain.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@EqualsAndHashCode(exclude = {FriendData.RELATION_PARTNER_DATA}, callSuper = true)
@ToString(exclude = {FriendData.RELATION_PARTNER_DATA}, callSuper = true)
@Data
public class FriendData extends AbstractData {

    public static final String LAST_UPDATE = "lastUpdate";
    public static final String REMOVED = "removed";
    public static final String COUNTRY = "country";
    public static final String OCCUPATION = "occupation";
    public static final String POLITICAL_TYPE = "politicalType";
    public static final String LIFE_MAIN_TYPE = "lifeMainType";
    public static final String PEOPLE_MAIN_TYPE = "peopleMainType";
    public static final String SMOKING_TYPE = "smokingType";
    public static final String ALCOHOL_TYPE = "alcoholType";
    public static final String SEX_TYPE = "sexType";
    public static final String DEACTIVATED_TYPE = "deactivatedType";
    public static final String RELATION_TYPE = "relationType";
    public static final String PHOTO_50 = "photo50";
    public static final String PHOTO_MAX_ORIG = "photoMaxOrig";
    public static final String RELATION_PARTNER_DATA = "relationPartnerData";

    @Column
    Instant lastUpdate;

    @Column
    boolean removed;

    @Column
    String bdate;

    @Column
    String city;

    @Column
    String country;

    @Column
    String homeTown;

    @Column
    String photo50;

    @Column
    String photoMaxOrig;

    @Column
    String site;

    @Column
    String facultyName;

    @Column
    Integer graduation;

    @Column
    String educationForm;

    @Column
    String educationStatus;

    @Column
    String universities;

    @Column
    String schools;

    @Column
    String status;

    @Column
    String occupation;

    @Column
    String nickname;

    @Enumerated(value = EnumType.STRING)
    @Column
    PoliticalType politicalType;

    @Column
    String religion;

    @Enumerated(value = EnumType.STRING)
    @Column
    LifeMainType lifeMainType;

    @Enumerated(value = EnumType.STRING)
    @Column
    PeopleMainType peopleMainType;

    @Column
    OpinionType smokingType;

    @Column
    OpinionType alcoholType;

    @Column
    String inspiredBy;

    @Column
    Integer twitter;

    @Column
    Integer facebook;

    @Column
    Integer livejournal;

    @Column
    String instagram;

    @Column
    String homePhone;

    @Column
    String mobilePhone;

    @Column
    String activities;

    @Column
    String interests;

    @Column
    String music;

    @Column
    String movies;

    @Column
    String tv;

    @Column
    String books;

    @Column
    String games;

    @Column
    String about;

    @Column
    String quotes;

    @Column
    String maidenName;

    @Column
    String career;

    @Column
    String military;

    @Enumerated(value = EnumType.STRING)
    @Column
    SexType sexType;

    @Enumerated(value = EnumType.STRING)
    @Column
    DeactivatedType deactivatedType;

    @Column
    String domain;

    @Enumerated(value = EnumType.STRING)
    @Column
    RelationType relationType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "relation_partner_data_id")
    RelationPartnerData relationPartnerData;

}
