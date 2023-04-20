package sat.recruitment.api.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class User {
    @NotBlank(message = "The name is required")
    private final String name;
    @NotBlank(message = "The email is required")
    @Email(message = "The email format is invalid")
    private final String email;
    @NotBlank(message = "The address is required")
    private final String address;
    @NotBlank(message = "The phone is required")
    private final String phone;
    private final UserType userType;
    private final Double money;

    private User(String name, String email, String address, String phone, UserType userType, Double money) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.userType = userType;
        this.money = money;
    }

    @Override
    public String toString() {
        return name +
                "," + email +
                "," + address +
                "," + phone +
                "," + userType.getName() +
                "," + money;
    }

    public static class UserBuilder {
        private String name;
        private String email;
        private String address;
        private String phone;
        private UserType userType;
        private Double money;

        public UserBuilder(String name, String email, String address, String phone) {
            this.name = name;
            this.email = email;
            this.address = address;
            this.phone = phone;
            this.userType = UserType.NONE;
            this.money = 0d;
        }

        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public UserBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder setUserType(UserType userType) {
            this.userType = (userType == null) ? UserType.NONE : userType;
            return this;
        }

        public UserBuilder setMoney(Double money) {
            this.money = money;
            return this;
        }

        public User build() {
            return new User(name, email, address, phone, userType, money);
        }
    }
}
