package platform.messagingservice.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import platform.messagingservice.api.utils.UserTestBuilder;
import sat.recruitment.api.exceptions.IllegalArgumentException;
import sat.recruitment.api.model.User;
import sat.recruitment.api.model.UserType;
import sat.recruitment.api.repository.UserRepository;
import sat.recruitment.api.service.UserService;
import sat.recruitment.api.service.impl.UserServiceImpl;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createTest() {
        User user = new UserTestBuilder()
                .addUserType(UserType.NORMAL)
                .addMoney(2d)
                .build();
        when(userRepository.save(any())).thenReturn(user);
        User newUser = userService.create(user);
        assertNotNull(newUser, "Is not null");
    }

    @Test
    void calculateMoneyForUserC1Test() {
        // case 1: Normal with money > 100
        User user = new UserTestBuilder()
                .addUserType(UserType.NORMAL)
                .addMoney(200d)
                .build();
        createCalculateMoneyForUserTest(224d, user);
    }

    @Test
    void calculateMoneyForUserC2Test() {
        // case 2: Normal with money > 10
        User user = new UserTestBuilder()
                .addUserType(UserType.NORMAL)
                .addMoney(20d)
                .build();
        createCalculateMoneyForUserTest(36d, user);
    }

    @Test
    void calculateMoneyForUserC3Test() {
        // case 3: SuperUser with money > 100
        User user = new UserTestBuilder()
                .addUserType(UserType.SUPERUSER)
                .addMoney(200d)
                .build();
        createCalculateMoneyForUserTest(240d, user);
    }

    @Test
    void calculateMoneyForUserC4Test() {
        // case 4: Premium with money > 100
        User user = new UserTestBuilder()
                .addUserType(UserType.PREMIUM)
                .addMoney(200d)
                .build();
        createCalculateMoneyForUserTest(600d, user);
    }

    @Test
    void calculateMoneyForUserC5Test() {
        // case 5: no UserType
        User user = new UserTestBuilder()
                .addMoney(300d)
                .build();
        createCalculateMoneyForUserTest(user.getMoney(), user);
    }

    @Test
    void calculateMoneyForUserC6Test() {
        // case 6: Normal with no money
        User user = new UserTestBuilder()
                .addUserType(UserType.NORMAL)
                .build();
        createCalculateMoneyForUserTest(user.getMoney(), user);
    }

    @Test
    void calculateMoneyForUserC7Test() {
        // case 7: User with no money and no UserType
        User user = new UserTestBuilder()
                .build();
        createCalculateMoneyForUserTest(0d, user);
    }

    @Test
    void createUserWithoutName() {
        User user = new UserTestBuilder()
                .withOutName()
                .build();
        createUserTestWithErrors( user, "The name is required");
    }

    @Test
    void createTestWithSameNameError() {
        User user = new UserTestBuilder().build();
        when(userRepository.findByUser(user)).thenReturn(Optional.of(user));
        createUserTestWithErrors( user, "User is duplicated");
    }

    private void createUserTestWithErrors(User user, String message) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.create(user));
        Assertions.assertThat(exception.getMessage()).contains(message);
    }

    private void createCalculateMoneyForUserTest(Double expectedMoney, User user) {
        Double calculatedMoney = userService.calculateMoneyForUser(user);
        assertEquals(expectedMoney, calculatedMoney);
    }

}
