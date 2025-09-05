package ru.job4j.cars.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.user.UserService;
import java.util.Optional;

class UserControllerTest {
    private UserController userController;
    private UserService userService;
    private MockHttpSession mockHttpSession;

    @BeforeEach
    void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        mockHttpSession = new MockHttpSession();
    }

    /**
     * Test case: Return registration page when requested.
     */
    @Test
    void whenRequestUserRegistrationPageThenGetPage() {
        var view = userController.getRegistrationPage();

        assertThat(view).isEqualTo("users/register");
    }

    /**
     * Test case: Save a user to the database and verify that the same user is passed to the service and redirected correctly.
     */
    @Test
    void whenPostUserWithAllFieldsThenSameDataAndRedirectToVacanciesPage() {
        var user = new User(1, "example@test.com", "Nikolay", "qwerty123");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.create(userArgumentCaptor.capture())).thenReturn(user);

        var model = new ConcurrentModel();
        var view = userController.register(user, model);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/posts");
        assertThat(actualUser).isEqualTo(user);
    }

    /**
     * Test case: Save a user with an existing email and verify that an exception is shown on the error page.
     */
    @Test
    void whenPostExistingUserWithAllFieldsThenGetErrorPage() {
        var expectedException = new RuntimeException("User already exists");
        when(userService.create(any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = userController.register(new User(), model);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(actualExceptionMessage).isEqualTo("User already exists");
    }

    /**
     * Test case: Return login page when requested.
     */
    @Test
    void whenRequestUserLoginPageThenGetPage() {
        var view = userController.getLoginPage();

        assertThat(view).isEqualTo("users/login");
    }

    /**
     * Test case: Log in with an existing user and verify redirection to sessions and user stored in session.
     */
    @Test
    void whenLoginExistingUserThenGetRedirectionAndSameUser() {
        var user = new User(1, "Aleks prig", "example@test.com", "qwerty123");
        var expectedUser = Optional.of(user);
        when(userService.findByLoginAndPassword(user.getLogin(), user.getPassword())).thenReturn(expectedUser);

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, mockHttpSession);
        var actualUser = mockHttpSession.getAttribute("user");

        assertThat(view).isEqualTo("redirect:/posts");
        assertThat(actualUser).isEqualTo(user);
    }

    /**
     * Test case: Attempt login with invalid credentials and verify error message is shown on login page.
     */
    @Test
    void whenLoginNonExistingUserThenGetRedirectionAndErrorMessage() {
        when(userService.findByLoginAndPassword(any(), any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.loginUser(new User(), model, mockHttpSession);
        var actualExceptionMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualExceptionMessage).isEqualTo("Login or password is incorrect");
    }

    /**
     * Test case: Log out the current user and verify redirection to login page and session invalidation.
     */
    @Test
    void whenLogoutFromAccountThenGetRedirectionToLoginPage() {
        var view = userController.logout(mockHttpSession);

        assertThat(view).isEqualTo("redirect:/users/login");
        assertThat(mockHttpSession.isInvalid()).isTrue();
    }
}