package ru.friends.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
            Model model,
            @RequestParam(name = "viewer_id", required = false) Long viewerId,
            @RequestParam(name = "auth_key", required = false) String authKey
    ) {

        if (viewerId == null && vkUserDefaultId != null)
            viewerId = vkUserDefaultId;

        if (viewerId == null)
            throw new RuntimeException("Parameter 'viewer_id' required.");

        validate(viewerId, authKey);

        model.addAttribute("baseUrlParams", getBaseUrlParams(viewerId, authKey));

        if (userService.isUserExists(viewerId)) {
            userService.updateLastEntryForUser(viewerId);
            model.addAttribute("urlToRedirect", "friend-change-list");
            return "js_redirect";
        } else {
            userService.createAndHandleUser(viewerId);
            model.addAttribute("urlToRedirect", "friend-change-list");
            return "js_redirect";
        }
    }

}
