package ru.job4j.cars.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {
    private UserRepository userRepository;

    @Override
    @Transactional
    public User create(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if (findByLogin(entity.getLogin()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        return userRepository.create(entity);
    }

    @Override
    @Transactional
    public void update(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if (findByLogin(entity.getLogin()).isEmpty()) {
            throw new NoSuchElementException("User not found");
        }
        userRepository.update(entity);
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be negative or zero");
        }
        if (findById(id).isEmpty()) {
            throw new NoSuchElementException("User not found");
        }
        userRepository.delete(id);
    }

    @Override
    public Optional<User> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be negative or zero");
        }
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAllOrderById() {
        return userRepository.findAllOrderById();
    }

    @Override
    public List<User> findByLikeLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("key cannot be null/empty");
        }
        return userRepository.findByLikeLogin(login.toLowerCase().trim());
    }

    @Override
    public Optional<User> findByLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("login cannot be null/empty");
        }
        return userRepository.findByLogin(login.toLowerCase().trim());
    }
}
