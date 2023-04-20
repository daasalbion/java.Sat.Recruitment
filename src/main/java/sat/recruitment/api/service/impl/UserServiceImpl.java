package sat.recruitment.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.stereotype.Service;

import sat.recruitment.api.exceptions.IllegalArgumentException;
import sat.recruitment.api.model.User;
import sat.recruitment.api.repository.UserRepository;
import sat.recruitment.api.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private static final double NORMAL_CASE1_PERCENTAGE_GIFT = 0.12d;
    private static final double NORMAL_CASE2_PERCENTAGE_GIFT = 0.8d;
    private static final double SUPER_USER_PERCENTAGE_GIFT = 0.2d;
    private static final double PREMIUM_PERCENTAGE_GIFT = 2.0d;
    private static final double THRESHOLD_MONEY = 100d;

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        validate(user);
        User.UserBuilder userBuilder =
                new User.UserBuilder(user.getName(), user.getEmail(), user.getAddress(), user.getPhone());
        userBuilder.setUserType(user.getUserType());
        userBuilder.setMoney(calculateMoneyForUser(user));
        User newUser = userBuilder.build();

        return userRepository.save(newUser);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Double calculateMoneyForUser(User user) {
        double money = (user.getMoney() != null) ? user.getMoney() : 0d;
        if (user.getUserType() == null) return money;

        double percentage = 0d;

        switch (user.getUserType()) {
            case NORMAL -> {
                if (money > THRESHOLD_MONEY) {
                    percentage = NORMAL_CASE1_PERCENTAGE_GIFT;
                } else if (money > 10) {
                    percentage = NORMAL_CASE2_PERCENTAGE_GIFT;
                }
            }
            case SUPERUSER -> {
                if (money > THRESHOLD_MONEY) {
                    percentage = SUPER_USER_PERCENTAGE_GIFT;
                }
            }
            case PREMIUM -> {
                if (money > THRESHOLD_MONEY) {
                    percentage = PREMIUM_PERCENTAGE_GIFT;
                }
            }
            default -> percentage = 0d;
        }

        double gift = money * percentage;
        return money + gift;
    }

    @Override
    public void validate(User newUser) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        String errors = validator.validate(newUser)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors);
        }

        if (userRepository.findByUser(newUser).isPresent()) {
            throw new IllegalArgumentException("User is duplicated");
        }
    }
}
