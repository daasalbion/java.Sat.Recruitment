package platform.messagingservice.api.utils;

import sat.recruitment.api.model.User;
import sat.recruitment.api.model.User.UserBuilder;
import sat.recruitment.api.model.UserType;

public class UserTestBuilder {

    private final UserBuilder user;

    public UserTestBuilder() {
        this.user = new User.UserBuilder("Derlis", "derlis@gmail.com", "Jasy", "301739");
    }

    public UserTestBuilder addUserType(UserType userType) {
        user.setUserType(userType);
        return this;
    }

    public UserTestBuilder addMoney(Double money) {
        user.setMoney(money);
        return this;
    }

    public UserTestBuilder withOutName() {
        user.setName(null);
        return this;
    }

    public UserTestBuilder withOutEmail() {
        user.setEmail(null);
        return this;
    }

    public UserTestBuilder withOutAddress() {
        user.setAddress(null);
        return this;
    }

    public UserTestBuilder withOutPhone() {
        user.setPhone(null);
        return this;
    }

    public UserTestBuilder anotherUser() {
        user.setName("Thelma");
        user.setEmail("thelma@gmail.com");
        user.setAddress("campo alto");
        user.setPhone("908295");
        return this;
    }

    public User build() {
        return user.build();
    }

}
