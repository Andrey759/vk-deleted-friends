package ru.friends.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.friends.model.domain.ChangeType;
import ru.friends.service.FriendChangeService;
import ru.friends.service.UserService;

import java.net.URISyntaxException;

@Controller
@Slf4j
public class FriendChangeController extends AbstractController {

    @Autowired
    UserService userService;
    @Autowired
    FriendChangeService friendChangeService;

    @Value("${vk.user.default.id:}")
    Long vkUserDefaultId;

    @GetMapping("/")
    public String getIndexPage(
            @RequestParam(name = "viewer_id", required = false) Long viewerId,
            @RequestParam(name = "auth_key", required = false) String authKey
    ) {

        if (viewerId == null && vkUserDefaultId != null)
            viewerId = vkUserDefaultId;

        if (viewerId == null)
            throw new RuntimeException("Parameter 'view_id' required.");

        userService.checkAndCreateIfNeeded(viewerId);
        return "redirect:/friend-change-list?" + getBaseUrlParams(viewerId, authKey);
    }

    @GetMapping("/friend-change-list")
    public String getFriendChangeList(
            Model model,
            @RequestParam(name = "viewer_id", required = false) Long viewerId,
            @RequestParam(name = "auth_key", required = false) String authKey,
            @RequestParam(required = false) Integer page,
            @RequestParam(name = "change_type", required = false) ChangeType changeType
    ) throws URISyntaxException, ClientException, ApiException {

        return super.handle(
                model, viewerId, authKey, null, changeType, page,"friend-change-list", "friend_change_list",
                pageRequest -> changeType == null
                        ? friendChangeService.findByUserId(viewerId, pageRequest)
                        : friendChangeService.findByUserIdAndChangeType(viewerId, changeType, pageRequest)
        );
    }

}
