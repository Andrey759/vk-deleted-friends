package ru.friends.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.friends.model.dto.data.FriendData;

import java.util.List;

import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles({"test", "dev", "prod"})
public class ExternalRequestServiceTest {
    //private static final long TEST_ID = 16583872L;
    private static final long TEST_ID = 1L;

    @Autowired
    ExternalRequestService externalRequestService;

    @Test
    public void contextText() {
        assertNotNull(externalRequestService);
    }


    @Test
    public void testExternalRequest() {
        List<FriendData> friends = externalRequestService.loadFriendsById(TEST_ID);
        assertNotNull(friends);
    }

}
