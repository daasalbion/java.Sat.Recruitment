package platform.messagingservice.api.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import platform.messagingservice.api.utils.UserTestBuilder;
import sat.recruitment.api.model.User;
import sat.recruitment.api.repository.UserRepository;
import sat.recruitment.api.repository.impl.UserRepositoryImpl;

class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository = new UserRepositoryImpl("src/test/resources/users.txt");
    }

    @Test
    void saveTestOK() {
        User newUser = userRepository.save(new UserTestBuilder().build());
        assertNotNull(newUser, "User is not null");
    }

    @Test
    void findByUserTestOK() {
        // create user
        User user = userRepository.save(new UserTestBuilder().build());
        // find it
        Optional<User> findUser = userRepository.findByUser(user);
        assertNotNull(findUser, "User is not null");
    }

    @Test
    void findByUserTestNotFoundOK() {
        // create user
        User searchUser = new UserTestBuilder().anotherUser().build();
        // find it
        Optional<User> user = userRepository.findByUser(searchUser);
        assertTrue(user.isEmpty(), "User is not null");
    }

    @Test
    void findAllOK() {
        userRepository.save(new UserTestBuilder().build());
        List<User> users = userRepository.findAll();
        assertNotNull(users, "list not null");
        assertTrue(users.size() > 0, "list not empty");
    }
}
