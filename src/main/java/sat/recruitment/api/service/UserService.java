package sat.recruitment.api.service;

import java.util.List;

import sat.recruitment.api.model.User;

public interface UserService {

    User create(User user);
    void validate(User user);
    Double calculateMoneyForUser(User user);
    List<User> getAll();

}
