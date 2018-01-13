package ru.friends.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.base.Throwables;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.friends.converter.FriendDataConverter;
import ru.friends.model.dto.data.FriendData;
import ru.friends.model.dto.remote.RemoteUserData;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@Service
public class ExternalRequestService {
    private static final String VK_API_GET_FRIENDS = "https://api.vk.com/method/friends.get?user_id=%d&fields=" +
            "nickname,domain,sex,bdate,city,country,timezone,photo_50,photo_100,photo_200_orig,has_mobile,contacts," +
            "education,online,relation,last_seen,status,can_write_private_message,can_see_all_posts,can_post," +
            "universities";

    private static final String VK_API_GET_FRIENDS_SHORT = "https://api.vk.com/method/friends.get?user_id=%d&fields=" +
            "nickname,domain,sex,photo_50,relation";

    private static final ObjectReader READER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .readerFor(ResponseHolder.class);

    @Autowired
    FriendDataConverter userDataConverter;

    public List<FriendData> loadFriendsById(long id) {
        try {
            URL url = new URL(String.format(VK_API_GET_FRIENDS_SHORT, id));
            URLConnection connection = url.openConnection();
            String response = IOUtils.toString(connection.getInputStream(), "UTF-8");
            ResponseHolder responseDto = READER.readValue(response);
            List<RemoteUserData> remoteUsers = responseDto.getResponse();
            return userDataConverter.toDtoFromRemoteUserData(remoteUsers);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Data
    private static class ResponseHolder {
        List<RemoteUserData> response;
    }

}
