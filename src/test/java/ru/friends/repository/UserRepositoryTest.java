package ru.friends.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.friends.model.dto.User;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles({"test", "dev"})
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void contextText() {
        assertNotNull(userRepository);
    }

    @Test
    public void tableIsNotEmptyTest() {
        Iterable<User> users = userRepository.findAll();
        assertNotNull(users);
        assertTrue(users.iterator().hasNext());
    }

}
