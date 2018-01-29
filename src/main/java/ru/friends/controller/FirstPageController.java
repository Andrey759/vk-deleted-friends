package ru.friends.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.friends.service.UserService;

@Controller
public class FirstPageController extends AbstractController {

    @Autowired
    UserService userService;

    @Value("${vk.user.default.id:}")
    Long vkUserDefaultId;

    @GetMapping("/")
    public String getFirstPage(
            @RequestParam(name = "viewer_id", required = false) Long viewerId,
            @RequestParam(name = "auth_key", required = false) String authKey
    ) {

        if (viewerId == null && vkUserDefaultId != null)
            viewerId = vkUserDefaultId;

        if (viewerId == null)
            throw new RuntimeException("Parameter 'view_id' required.");

        if (userService.isUserExists(viewerId)) {
            userService.updateLastEntryForUser(viewerId);
            return "redirect:/friend-change-list?" + getBaseUrlParams(viewerId, authKey);
        } else {
            userService.createAndHandleUser(viewerId);
            return "redirect:/welcome-message?" + getBaseUrlParams(viewerId, authKey);
        }
    }

}
