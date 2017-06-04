package ru.friends.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.friends.model.domain.IntervalType;
import ru.friends.service.FriendHandlingService;

@Component
@ConditionalOnProperty("externalRequestScheduler.enabled")
@Slf4j
public class ExternalRequestScheduler {

    @Value("${cron.IntervalType.EVERY_NIGHT.enabled}")
    boolean everyNightEnabled;

    @Value("${cron.IntervalType.EVERY_HOUR.enabled}")
    boolean everyHourEnabled;

    @Value("${cron.IntervalType.EVERY_MINUTE.enabled}")
    boolean everyMinuteEnabled;

    @Autowired
    FriendHandlingService friendHandlingService;

    @Scheduled(cron = "${cron.IntervalType.EVERY_NIGHT}")
    public void handleFriendListForAllUsersEveryNight() {
        if (!everyNightEnabled)
            return;

        friendHandlingService.updateFriendsForUsersWithMetrics(IntervalType.EVERY_NIGHT);
    }

    @Scheduled(cron = "${cron.IntervalType.EVERY_HOUR}")
    public void handleFriendListForAllEveryHour() {
        if (!everyHourEnabled)
            return;

        friendHandlingService.updateFriendsForUsersWithMetrics(IntervalType.EVERY_HOUR);
    }

    @Scheduled(cron = "${cron.IntervalType.EVERY_MINUTE}")
    public void handleFriendListForAllEveryMinute() {
        if (!everyMinuteEnabled)
            return;

        friendHandlingService.updateFriendsForUsersWithMetrics(IntervalType.EVERY_MINUTE);
    }

}
