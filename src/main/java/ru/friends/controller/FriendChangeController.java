package ru.friends.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.friends.model.domain.ChangeType;
import ru.friends.service.FriendChangeService;

@Controller
@Slf4j
public class FriendChangeController extends AbstractController {

    @Autowired
    FriendChangeService friendChangeService;

    @GetMapping("/friend-change-list")
    public String getFriendChangeList(
            Model model,
            @RequestParam(name = "viewer_id") long viewerId,
            @RequestParam(name = "auth_key", required = false) String authKey,
            @RequestParam(required = false) Integer page,
            @RequestParam(name = "change_type", required = false) ChangeType changeType
    ) {

        return super.handlePageWithDataTable(
                model, viewerId, authKey, null, changeType, page,"friend-change-list", "friend_change_list",
                pageRequest -> changeType == null
                        ? friendChangeService.findByUserId(viewerId, pageRequest)
                        : friendChangeService.findByUserIdAndChangeType(viewerId, changeType, pageRequest)
        );
    }

}
