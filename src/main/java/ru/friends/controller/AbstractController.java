package ru.friends.controller;

import com.google.common.hash.Hashing;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import ru.friends.model.domain.ChangeType;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class AbstractController {

    private static final int ROWS_ON_PAGE = 10;

    @Value("${vk.app.id:}")
    Integer vkAppId;
    @Value("${vk.app.protect.key:}")
    String vkAppProtectKey;
    @Value("${vk.user.auth.key.enabled:true}")
    boolean vkUserAuthKeyEnabled;

    protected String handlePageWithDataTable(
            Model model,
            long viewerId,
            String authKey,
            Long friendRemoteId,
            ChangeType changeType,
            Integer page,
            String url,
            String pageName,
            Function<PageRequest, Page> function
    ) throws ClientException, ApiException {

        validate(viewerId, authKey);

        if (page == null)
            page = 1;

        PageRequest pageRequest = new PageRequest(page - 1, ROWS_ON_PAGE);
        Page resultPage = function.apply(pageRequest);
        model.addAttribute("resultList", resultPage.getContent());
        int totalPages = (int) Math.ceil(((double) resultPage.getTotalElements()) / ROWS_ON_PAGE);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("baseUrlParams", getBaseUrlParams(viewerId, authKey));
        model.addAttribute("allUrlParams", getAllUrlParams(viewerId, authKey, friendRemoteId, changeType));

        if (page < 1 || (page > totalPages && totalPages > 0))
            return vkUserAuthKeyEnabled
                    ? "redirect:/" + url
                    : "redirect:/" + url + "?viewer_id=" + viewerId;

        return pageName;
    }

    protected void validate(long viewerId, String authKey) {

        if (vkUserAuthKeyEnabled && authKey == null)
            throw new RuntimeException("Parameter 'auth_key' required.");

        String authKeyGenerated = Hashing.md5()
                .hashString(vkAppId + "_" + viewerId + "_" + vkAppProtectKey, Charset.defaultCharset())
                .toString();

        if (vkUserAuthKeyEnabled && !Objects.equals(authKey, authKeyGenerated))
            throw new RuntimeException("'auth_key' wrong.");
    }

    protected String getBaseUrlParams(long viewerId, String authKey) {
        StringBuilder builder = new StringBuilder().append("viewer_id=").append(viewerId);

        if (authKey != null)
            builder.append("&auth_key=").append(authKey);

        return builder.toString();
    }

    protected String getAllUrlParams(long viewerId, String authKey, Long friendRemoteId, ChangeType changeType) {
        StringBuilder builder = new StringBuilder();

        builder.append("viewer_id=").append(viewerId);

        if (authKey != null)
            builder.append("&auth_key=").append(authKey);

        if (friendRemoteId != null)
            builder.append("&friend_remote_id=").append(friendRemoteId);

        if (changeType != null)
            builder.append("&change_type=").append(changeType);

        return builder.toString();
    }

    private UserXtrCounters getUser(Integer viewerId) throws ClientException, ApiException {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        ServiceActor actor = new ServiceActor(vkAppId, vkAppProtectKey);
        List<String> viewerIds = Collections.singletonList(viewerId.toString());
        return vk.users().get(actor).userIds(viewerIds).execute().get(0);
    }

}
