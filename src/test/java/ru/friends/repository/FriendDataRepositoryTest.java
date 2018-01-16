package ru.friends.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles({"dev", "prod", "test"})
public class FriendDataRepositoryTest {

    @Autowired
    FriendDataRepository friendDataRepository;

    @Test
    public void contextText() {
        assertNotNull(friendDataRepository);
    }

}
