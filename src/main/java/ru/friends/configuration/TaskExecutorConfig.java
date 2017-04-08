package ru.friends.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorConfig {

    @Value("${update-friends.task-executor.core-pool-size}")
    private int updateFriendsCorePoolSize;
    @Value("${update-friends.task-executor.max-pool-size}")
    private int updateFriendsMaxPoolSize;
    @Value("${update-friends.task-executor.queue-capacity}")
    private int updateFriendsQueueCapacity;

    @Bean
    public TaskExecutor updateFriendsTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(updateFriendsCorePoolSize);
        taskExecutor.setMaxPoolSize(updateFriendsMaxPoolSize);
        taskExecutor.setQueueCapacity(updateFriendsQueueCapacity);
        return taskExecutor;
    }

}
