package ru.friends.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.base.Throwables;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.queries.users.UserField;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.friends.converter.FriendDataConverter;
import ru.friends.model.dto.data.FriendData;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.vk.api.sdk.queries.users.UserField.*;

@Service
public class ExternalRequestService {
    private static final String VK_API_GET_FRIENDS = "https://api.vk.com/method/friends.get?user_id=%d&fields=" +
            "nickname,domain,sex,bdate,city,country,timezone,photo_50,photo_100,photo_200_orig,has_mobile,contacts," +
            "education,online,relation,last_seen,status,can_write_private_message,can_see_all_posts,can_post," +
            "universities";

    private static final String VK_API_GET_FRIENDS_SHORT = "https://api.vk.com/method/friends.get?user_id=%d&fields=" +
            "nickname,domain,sex,photo_50,relation";

    private static final List<UserField> FIELD_LIST = Arrays.asList(
            SEX,        //++
            BDATE,      //+
            CITY,       //+
            COUNTRY,    //+
            HOME_TOWN,  //+
            PHOTO_50,   //++
            PHOTO_ID,   //???
            PHOTO_MAX_ORIG, //???
            //LISTS,
            DOMAIN,     //++
            //CONTACTS, //???
            SITE,       //+
            EDUCATION,  //+
            UNIVERSITIES,//+
            SCHOOLS,    //+
            STATUS,     //+
            OCCUPATION, //+
            NICKNAME,   //++
            //RELATIVES,  // skip
            RELATION,   //++
            PERSONAL,   //+
            //CONNECTIONS, //???
            EXPORTS,    //+
            ACTIVITIES, //+
            INTERESTS,  //+
            MUSIC,      //+
            MOVIES,     //+
            TV,         //+
            BOOKS,      //+
            GAMES,      //+
            ABOUT,      //+
            QUOTES,     //+
            MAIDEN_NAME,//+
            CAREER,     //+
            MILITARY    //+
    );

    private static final ObjectReader READER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .readerFor(ResponseHolder.class);

    @Autowired
    FriendDataConverter userDataConverter;

    @Value("${vk.app.id:}")
    Integer vkAppId;
    @Value("${vk.app.service.key:}")
    String vkAppServiceKey;

    @Deprecated // but it works faster
    public List<FriendData> loadFriendsByIdOld(long id) {
        try {
            URL url = new URL(String.format(VK_API_GET_FRIENDS, id));
            URLConnection connection = url.openConnection();
            String response = IOUtils.toString(connection.getInputStream(), "UTF-8");
            ResponseHolder responseDto = READER.readValue(response);
            List<UserFull> remoteUsers = responseDto.getResponse();
            return userDataConverter.toFriendDataFromFullUser(remoteUsers);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Data
    private static class ResponseHolder {
        List<UserFull> response;
    }


    @SuppressWarnings("unchecked")
    public List<FriendData> loadFriendsById(Long id) {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        ServiceActor actor = new ServiceActor(vkAppId, vkAppServiceKey);
        List<Integer> friendIds = null;
        try {
            friendIds = vk.friends().get(actor).userId(id.intValue()).execute().getItems();
        } catch (ApiException | ClientException e) {
            Throwables.propagate(e);
        }
        List<String> friendIdStrings = friendIds.stream().map(Object::toString).collect(Collectors.toList());
        List remoteUsers = null;
        try {
            remoteUsers = vk.users().get(actor).userIds(friendIdStrings).fields(FIELD_LIST).execute();
        } catch (ApiException | ClientException e) {
            Throwables.propagate(e);
        }
        return userDataConverter.toFriendDataFromFullUser(remoteUsers);
    }

}
