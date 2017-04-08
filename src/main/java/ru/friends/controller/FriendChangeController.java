package ru.friends.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.friends.service.FriendChangeService;

@Controller
public class FriendChangeController {

    @Autowired
    FriendChangeService friendChangeService;

    @RequestMapping(value = "/friend/change/list", method = RequestMethod.GET)
    public String getFriendChangeList(Model model) {
        friendChangeService.findByUserId(1974730L);
        return "friend_change_list";
    }

}
