package ru.friends.controller;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @Value("${vk.app.id}")
    int vkAppId;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getLoginPage(Model model) {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        model.addAttribute("vkAppId", vkAppId);
        return "login";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String postLoginPage(Model model) {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        // TODO: validate and redirect
        return "login";
    }

}
