package sat.recruitment.api.model;

import java.util.function.Predicate;

import lombok.Getter;

@Getter
public class UserPredicate {

    private final Predicate<User> predicate;

    public UserPredicate(User user) {
        Predicate<User> hasSameEmailOrPhone = a -> a.getEmail().equals(user.getEmail()) ||
                a.getPhone().equals(user.getPhone());
        Predicate<User> hasSameNameAndAddress = a -> a.getName().equals(user.getName()) &&
                a.getAddress().equals(user.getAddress());
        this.predicate = hasSameEmailOrPhone.or(hasSameNameAndAddress);
    }

}
