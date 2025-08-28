package ru.job4j.cars.service.user;

import ru.job4j.cars.model.User;
import ru.job4j.cars.service.CrudService;

import java.util.List;
import java.util.Optional;

public interface UserService extends CrudService<User> {
    List<User> findByLikeLogin(String key);

    Optional<User> findByLogin(String login);
}
