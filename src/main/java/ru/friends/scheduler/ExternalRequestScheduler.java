package ru.friends.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.friends.model.domain.IntervalType;
import ru.friends.service.FriendHandlingService;

@Component
@ConditionalOnProperty("externalRequestScheduler.enabled")
public class ExternalRequestScheduler {

    @Autowired
    FriendHandlingService friendHandlingService;

    @Scheduled(cron = "${cron.IntervalType.EVERY_NIGHT}")
    public void handleFriendListForAllUsers() {
        friendHandlingService.updateFriendsForUsersWithMetrics(IntervalType.EVERY_NIGHT);
    }

}
