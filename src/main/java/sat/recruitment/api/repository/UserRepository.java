package sat.recruitment.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import sat.recruitment.api.model.User;

@Repository
public interface UserRepository {

    User save(User user);
    Optional<User> findByUser(User user);
    List<User> findAll();

}
