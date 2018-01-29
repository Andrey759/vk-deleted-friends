package ru.friends.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WelcomeMessageController extends AbstractController {

    @GetMapping("/welcome-message")
    public String getIndexPage(
            Model model,
            @RequestParam(name = "viewer_id", required = false) Long viewerId,
            @RequestParam(name = "auth_key", required = false) String authKey
    ) {

        validate(viewerId, authKey);

        model.addAttribute("baseUrlParams", getBaseUrlParams(viewerId, authKey));

        return "welcome_message";
    }

}
