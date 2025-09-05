package ru.job4j.cars.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

class UserRepositoryTest {
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository = new UserRepository(
                new CrudRepository(
                        new HibernateConfiguration().sf()));
    }

    /**
     * Test case: When creating a user, then it can be found in the database with the same login.
     */
    @Test
    void whenCreateUserThenDbHasSameUser() {
        User user = new User("testLogin@gmail.com", "TestPassword", null);
        userRepository.create(user);
        Optional<User> result = userRepository.findById(user.getId());
        assertThat(result).isPresent()
                .map(User::getLogin)
                .hasValue(user.getLogin());
    }

    /**
     * Test case: When creating the same user again, then the database still contains the same user without duplication.
     */
    @Test
    void whenCreateUserThenCreateAgainThenDbHasSameUser() {
        User user = new User("testLogin@gmail.com", "TestPassword", null);
        userRepository.create(user);
        userRepository.create(user);
        Optional<User> result = userRepository.findById(user.getId());
        assertThat(result).isPresent()
                .map(User::getLogin)
                .hasValue(user.getLogin());
    }

    /**
     * Test case: When adding three users, then the database contains exactly three users.
     */
    @Test
    void whenAdd3UsersThenDbHas3Users() {
        User user1 = new User("testLogin1@gmail.com", "TestPassword1", null);
        User user2 = new User("testLogin2@gmail.com", "TestPassword2", null);
        User user3 = new User("testLogin3@gmail.com", "TestPassword3", null);
        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(user3);
        List<User> result = userRepository.findAllOrderById();
        assertThat(result).hasSize(3)
                .extracting(User::getLogin)
                .containsExactlyInAnyOrder(user1.getLogin(), user2.getLogin(), user3.getLogin());
    }

    /**
     * Test case: When updating a user, then the database contains the updated login.
     */
    @Test
    void whenUpdateUserThenDbHasSameUser() {
        User user = new User("testLogin@gmail.com", "TestPassword", null);
        userRepository.create(user);
        user.setLogin("changedLogin");
        userRepository.update(user);
        Optional<User> result = userRepository.findById(user.getId());
        assertThat(result).isPresent()
                .map(User::getLogin)
                .hasValue(user.getLogin());
    }

    /**
     * Test case: When deleting a user, then the database is empty.
     */
    @Test
    void whenDeleteUserThenDbEmpty() {
        User user = new User("testLogin@gmail.com", "TestPassword", null);
        userRepository.create(user);
        userRepository.delete(user.getId());
        List<User> result = userRepository.findAllOrderById();
        assertThat(result).isEmpty();
    }

    /**
     * Test case: When deleting a user by a non-existing ID, then the database remains unchanged.
     */
    @Test
    void whenDeleteUserByNotExistingIdThenDbNotEmpty() {
        User user = new User(1, "testLogin@gmail.com", "TestPassword", null);
        userRepository.create(user);
        userRepository.delete(222);
        List<User> result = userRepository.findAllOrderById();
        assertThat(result).isNotEmpty()
                .hasSize(1)
                .map(User::getLogin)
                .containsExactly(user.getLogin());
    }

    /**
     * Test case: When finding users by login containing 'test', then two users are returned.
     */
    @Test
    void whenFindByLoginLikeTestThenDbHasTwoUsers() {
        User user1 = new User("testLogin1@gmail.com", "TestPassword1", null);
        User user2 = new User("testLogin2@gmail.com", "TestPassword2", null);
        userRepository.create(user1);
        userRepository.create(user2);
        List<User> result = userRepository.findByLikeLogin("test");
        assertThat(result).isNotEmpty()
                .hasSize(2)
                .extracting(User::getLogin)
                .contains(user1.getLogin(), user2.getLogin());
    }

    /**
     * Test case: When finding users by login containing 'Ivan', then no users are returned.
     */
    @Test
    void whenFindByNameLikeIvanThenDbHasNoSuchElements() {
        User user1 = new User("testLogin1@gmail.com", "TestPassword1", null);
        User user2 = new User("testLogin2@gmail.com", "TestPassword2", null);
        userRepository.create(user1);
        userRepository.create(user2);
        List<User> result = userRepository.findByLikeLogin("Ivan");
        assertThat(result).isEmpty();
    }

    /**
     * Test case: When finding a user by exact login, then the corresponding user is returned.
     */
    @Test
    void whenFindByLoginTestThenDbHasUser() {
        User user1 = new User("testLogin1@gmail.com", "TestPassword1", null);
        User user2 = new User("test", "TestPassword2", null);
        User user3 = new User("testLogin3@gmail.com", "TestPassword3", null);
        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(user3);
        Optional<User> result = userRepository.findByLogin("test");
        assertThat(result).isPresent()
                .map(User::getLogin)
                .hasValue(user2.getLogin());
    }

    /**
     * Test case: When finding a user by login 'Andrey', then no users are returned.
     */
    @Test
    void whenFindByLoginAndreyThenDbHasNoSuchElements() {
        User user1 = new User("testLogin1@gmail.com", "TestPassword1", null);
        User user2 = new User("testLogin2@gmail.com", "TestPassword2", null);
        User user3 = new User("testLogin3@gmail.com", "TestPassword3", null);
        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(user3);
        Optional<User> result = userRepository.findByLogin("Andrey");
        assertThat(result).isEmpty();
    }

    /**
     * Test case: When finding a user by login and password (case-insensitive), then the correct user is returned.
     */
    @Test
    void whenFindByLoginAndPasswordTestThenDbHasUser() {
        User user1 = new User("testLogin1@gmail.com", "test1", "TestPassword1");
        User user2 = new User("testLogin2@gmail.com", "test2", "TestPassword2");
        User user3 = new User("testLogin3@gmail.com", "test3", "TestPassword3");
        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(user3);
        Optional<User> result = userRepository.findByLoginAndPassword("testLOGIN2@gmail.com", "TestPaSSWOrd2");
        assertThat(result).isPresent()
                .get()
                .isEqualTo(user2);
    }
}