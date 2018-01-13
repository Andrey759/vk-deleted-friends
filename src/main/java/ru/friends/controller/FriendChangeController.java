package ru.friends.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.friends.model.domain.ChangeType;
import ru.friends.service.FriendChangeService;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;

@Controller
@Slf4j
public class FriendChangeController extends AbstractController {

    @Autowired
    FriendChangeService friendChangeService;

    @GetMapping("/")
    public String getIndexPage() {
        if (!vkUserAuthKeyEnabled && vkUserDefaultId != null)
            return "redirect:/friend-change-list?viewer_id=" + vkUserDefaultId;

        return "redirect:/friend-change-list";
    }

    // For local testing
    @GetMapping("/friend-change-list")
    public String getFriendChangeList(
            Model model,
            HttpServletRequest request,
            @RequestParam(name = "viewer_id", required = false) Long viewerId,
            @RequestParam(name = "auth_key", required = false) String authKey,
            @RequestParam(required = false) Integer page,
            @RequestParam(name = "change_type", required = false) ChangeType changeType
    ) throws URISyntaxException, ClientException, ApiException {

        return super.handle(
                model, request, viewerId, authKey, page, null, changeType,"friend-change-list", "friend_change_list",
                pageRequest -> changeType == null
                        ? friendChangeService.findByUserId(viewerId, pageRequest)
                        : friendChangeService.findByUserIdAndChangeType(viewerId, changeType, pageRequest)
        );
    }

}
