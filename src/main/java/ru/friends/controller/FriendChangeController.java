package ru.friends.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.friends.model.dto.FriendChange;
import ru.friends.service.FriendChangeService;

import java.util.List;

@Controller
@Slf4j
public class FriendChangeController {

    @Autowired
    FriendChangeService friendChangeService;

    @RequestMapping(value = "/friend/change/list", method = RequestMethod.GET)
    public String getFriendChangeList(Model model) {
        // TODO: FriendChangeVo
        List<FriendChange> friendChanges = friendChangeService.findByUserId(1974730L);
        model.addAttribute("friendChanges", friendChanges);
        return "friend_change_list";
    }

}
