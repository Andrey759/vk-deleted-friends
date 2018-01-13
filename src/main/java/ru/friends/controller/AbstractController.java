package ru.friends.controller;

import com.google.common.hash.Hashing;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import ru.friends.model.domain.ChangeType;
import ru.friends.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class AbstractController {

    private static final int ROWS_ON_PAGE = 5;

    @Autowired
    UserService userService;

    @Value("${vk.app.id:}")
    Integer vkAppId;
    @Value("${vk.app.token:}")
    String vkAppToken;
    @Value("${vk.user.auth.key.enabled:true}")
    boolean vkUserAuthKeyEnabled;
    @Value("${vk.user.default.id:}")
    Integer vkUserDefaultId;

    // For local testing
    protected String handle(
            Model model,
            HttpServletRequest request,
            Long viewerId,
            String authKey,
            Integer page,
            Long friendRemoteId,
            ChangeType changeType,
            String url,
            String pageName,
            Function<PageRequest, Page> function
    ) throws URISyntaxException, ClientException, ApiException {

        if (viewerId != null)
            return _handle(model, viewerId, authKey, page, url, pageName, function);

        String refererStr = request.getHeader("referer");

        if (refererStr == null)
            throw new RuntimeException("No referer is available");

        URI referer = new URI(request.getHeader("referer"));
        List<NameValuePair> params = URLEncodedUtils.parse(referer, Charset.defaultCharset());
        Optional<String> viewerIdOptional = params.stream()
                .filter(pair -> "viewer_id".equals(pair.getName()))
                .map(NameValuePair::getValue)
                .findAny();

        if (!viewerIdOptional.isPresent())
            throw new RuntimeException("Parameter 'viewer_id' required.");

        return "redirect:/" + url + "?viewer_id=" + viewerIdOptional.get()
                + (friendRemoteId == null ? "" : "&friend_remote_id=" + friendRemoteId)
                + (changeType == null ? "" : "&change_type=" + changeType)
                + (page == null ? "" : "&page=" + page);
    }

    private String _handle(
            Model model,
            long viewerId,
            String authKey,
            Integer page,
            String url,
            String pageName,
            Function<PageRequest, Page> function
    ) throws ClientException, ApiException {

        if (vkUserAuthKeyEnabled && authKey == null)
            throw new RuntimeException("Parameter 'auth_key' required.");

        String authKeyGenerated = Hashing.md5()
                .hashString(vkAppId + "_" + viewerId + "_" + vkAppToken, Charset.defaultCharset())
                .toString();
        if (vkUserAuthKeyEnabled && Objects.equals(authKey, authKeyGenerated))
            throw new RuntimeException("'auth_key' wrong.");

        if (page == null)
            page = 1;

        userService.checkAndCreateIfNeeded(viewerId);
        PageRequest pageRequest = new PageRequest(page - 1, ROWS_ON_PAGE);
        Page resultPage = function.apply(pageRequest);
        model.addAttribute("resultList", resultPage.getContent());
        int totalPages = (int) Math.ceil(((double) resultPage.getTotalElements()) / ROWS_ON_PAGE);
        model.addAttribute("totalPages", totalPages);

        if (page < 1 || (page > totalPages && totalPages > 0))
            return vkUserAuthKeyEnabled
                    ? "redirect:/" + url
                    : "redirect:/" + url + "?viewer_id=" + viewerId;

        return pageName;
    }

    private UserXtrCounters getUser(Integer viewerId) throws ClientException, ApiException {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        ServiceActor actor = new ServiceActor(vkAppId, vkAppToken);
        List<String> viewerIds = Collections.singletonList(viewerId.toString());
        return vk.users().get(actor).userIds(viewerIds).execute().get(0);
    }

}
