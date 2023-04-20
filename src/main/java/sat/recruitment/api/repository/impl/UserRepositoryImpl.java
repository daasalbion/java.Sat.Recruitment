package sat.recruitment.api.repository.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import sat.recruitment.api.model.User;
import sat.recruitment.api.model.UserPredicate;
import sat.recruitment.api.model.UserType;
import sat.recruitment.api.repository.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private static final String ERROR_IN_DATASOURCE = "Error in %s occurred";

    private final String datasource;

    public UserRepositoryImpl(@Value("${datasource}") String datasource) {
        this.datasource = datasource;
    }

    @Override
    public User save(User user) {
        File file = new File(datasource);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (file.length() != 0) writer.newLine();
            writer.write(user.toString());
        } catch (IOException e) {
            LOGGER.error(String.format(ERROR_IN_DATASOURCE, datasource), e);
        }

        return user;
    }

    @Override
    public Optional<User> findByUser(User newUser) {
        Predicate<User> predicate = new UserPredicate(newUser).getPredicate();

        try (BufferedReader br = new BufferedReader(new FileReader(datasource))) {
            Stream<String> linesStream = br.lines();
            return linesStream.map(this::string2User)
                    .filter(predicate)
                    .findFirst();
        } catch (IOException e) {
            LOGGER.error(String.format(ERROR_IN_DATASOURCE, datasource), e);
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(datasource))) {
            Stream<String> linesStream = br.lines();
            return linesStream.map(this::string2User).collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error(String.format(ERROR_IN_DATASOURCE, datasource), e);
        }

        return users;
    }

    private User string2User(String strLine) {
        String[] line = strLine.split(",");
        User.UserBuilder userBuilder = new User.UserBuilder(line[0], line[1], line[2], line[3]);
        UserType userType = UserType.getValue(line[4]);
        userBuilder.setUserType(userType);
        userBuilder.setMoney(Double.valueOf(line[5]));

        return userBuilder.build();
    }

}
