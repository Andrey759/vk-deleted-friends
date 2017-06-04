package ru.friends.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.friends.model.domain.IntervalType;
import ru.friends.model.dto.data.FriendData;
import ru.friends.repository.UserRepository;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles({"test", "dev"})
public class ExternalRequestServiceTest {
    //private static final long TEST_ID = 16583872L;
    private static final long TEST_ID = 1974730L;

    @Autowired
    ExternalRequestService externalRequestService;
    @Autowired
    FriendHandlingService friendHandlingService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void test() {
        List<FriendData> friends = externalRequestService.loadFriendsById(TEST_ID);
        assertNotNull(friends);
        assertFalse(friends.isEmpty());
    }

    @Test
    @Ignore
    public void initTest() {
        friendHandlingService.initFriendListForNewUser(TEST_ID);
    }

    @Test
    @Ignore
    public void highLoadInitTest() {
        friendHandlingService.generateUsersAndLoadFriendsForEach();
    }

    @Test
    @Ignore
    public void updateFriendsTest() {
        friendHandlingService.updateFriendsForUser(TEST_ID);
    }

    @Test
    @Ignore
    public void updateFriendsForAllUsersTest() {
        friendHandlingService.updateFriendsForUsersWithMetrics(IntervalType.EVERY_NIGHT);
    }
}
