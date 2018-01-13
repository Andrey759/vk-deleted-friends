package ru.friends.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.friends.service.DataChangeService;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;

@Controller
@Slf4j
public class DataChangeController extends AbstractController {

    @Autowired
    DataChangeService dataChangeService;

    // For local testing
    @GetMapping("/data-change-list")
    public String getDataChangeList(
            Model model,
            HttpServletRequest request,
            @RequestParam(name = "viewer_id", required = false) Long viewerId,
            @RequestParam(name = "auth_key", required = false) String authKey,
            @RequestParam(required = false) Integer page,
            @RequestParam(name = "friend_remote_id", required = false) Long friendRemoteId
    ) throws URISyntaxException, ClientException, ApiException {

        return super.handle(
                model, request, viewerId, authKey, page, friendRemoteId, null,"data-change-list", "data_change_list",
                pageRequest -> friendRemoteId == null
                        ? dataChangeService.findByUserId(viewerId, pageRequest)
                        : dataChangeService.findByFriendRemoteId(viewerId, friendRemoteId, pageRequest)
        );
    }

}
