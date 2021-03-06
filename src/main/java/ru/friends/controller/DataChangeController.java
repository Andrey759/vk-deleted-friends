package ru.friends.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.friends.service.DataChangeService;

@Controller
@Slf4j
public class DataChangeController extends AbstractController {

    @Autowired
    DataChangeService dataChangeService;

    @GetMapping("/data-change-list")
    public String getDataChangeList(
            Model model,
            @RequestParam(name = "viewer_id") long viewerId,
            @RequestParam(name = "auth_key", required = false) String authKey,
            @RequestParam(required = false) Integer page,
            @RequestParam(name = "friend_remote_id", required = false) Long friendRemoteId
    ) {

        return super.handlePageWithDataTable(
                model, viewerId, authKey, friendRemoteId, null, page, "data-change-list", "data_change_list",
                pageRequest -> friendRemoteId == null
                        ? dataChangeService.findByUserId(viewerId, pageRequest)
                        : dataChangeService.findByFriendRemoteId(viewerId, friendRemoteId, pageRequest)
        );
    }

}
