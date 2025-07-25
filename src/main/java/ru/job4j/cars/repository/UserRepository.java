package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, userId);
            session.delete(user);
            session.getTransaction().commit();
        }
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        List<User> result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
             result = session.createQuery("from ru.job4j.cars.model.User ORDER BY id", User.class).list();
            session.getTransaction().commit();
        }
        return result;
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        User user;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            user = session.get(User.class, userId);
            session.getTransaction().commit();
        }
        return Optional.of(user);
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        List<User> result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session
                    .createQuery("from ru.job4j.cars.model.User WHERE login LIKE :key", User.class)
                    .setParameter("key", "%" + key + "%").list();
            session.getTransaction().commit();
        }
        return result;
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Optional<User> result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session
                    .createQuery("from ru.job4j.cars.model.User WHERE login = :login", User.class)
                    .setParameter("login", login).uniqueResultOptional();
            session.getTransaction().commit();
        }
        return result;
    }
}
