package ru.job4j.cars.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.CarPost;
import ru.job4j.cars.model.User;
import ru.job4j.cars.security.AccessService;
import ru.job4j.cars.service.car.CarService;
import ru.job4j.cars.service.carpost.CarPostService;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

class PostControllerTest {
    private CarPostService carPostService;
    private CarService carService;
    private PostController postController;
    private AccessService accessService;
    private User user;
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        carPostService = Mockito.mock(CarPostService.class);
        carService = Mockito.mock(CarService.class);
        accessService = Mockito.mock(AccessService.class);

        file = mock(MultipartFile.class);
        user = new User("Aleks", "Aliev", "Sqwerty123");
        postController = new PostController(carService, carPostService, accessService);

    }

    /**
     * Test case: When getting all posts, then car posts and car brands are added to the model and the correct view is returned.
     */
    @Test
    void whenGetAllPostsThenPostsAndCarBrandsAddedToModelAndListViewReturned() {
        var posts = List.of(new CarPost());
        var carBrands = List.of("BMW", "Mercedes", "Audi");
        when(carPostService.findAllOrderById()).thenReturn(posts);
        when(carService.findAllCarBrands()).thenReturn(carBrands);

        var model = new ConcurrentModel();
        var view = postController.getAllPosts(model);
        var actualPosts = model.getAttribute("carPosts");
        var actualCarBrands = model.getAttribute("carBrands");

        assertThat(view).isEqualTo("posts/list");
        assertThat(actualPosts).isEqualTo(posts);
        assertThat(actualCarBrands).isEqualTo(carBrands);
    }

    /**
     * Test case: When getting the creation page, then an empty CarPost is added to the model and the "create" view is returned.
     */
    @Test
    void whenGetCreationPageThenEmptyCarPostAddedToModelAndCreateViewReturned() {
        var carPost = new CarPost();

        var model = new ConcurrentModel();
        var view = postController.getCreationPage(model);
        var actualCarPost = model.getAttribute("carPost");

        assertThat(view).isEqualTo("posts/create");
        assertThat(actualCarPost).isEqualTo(carPost);
    }

    /**
     * Test case: When a post is created successfully, then it redirects to the posts page.
     */
    @Test
    void whenCreatePostThenRedirectToPostsPage() throws IOException, InterruptedException {
        var carPost = new CarPost();
        when(carPostService.create(any(), any())).thenReturn(carPost);

        var model = new ConcurrentModel();
        var view = postController.createCarPost(carPost, file, user, model);

        assertThat(view).isEqualTo("redirect:/posts");
    }

    /**
     * Test case: When creating a post throws an exception, then the error view is returned with an error message.
     */
    @Test
    void whenCreatePostThrowsExceptionThenErrorViewReturned() throws IOException, InterruptedException {
        var expectedException = new NoSuchElementException("No users found");
        var carPost = new CarPost();
        when(carPostService.create(any(), any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = postController.createCarPost(carPost, file, user, model);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(errorMessage).isEqualTo(expectedException.getMessage());
    }

    /**
     * Test case: When getting the info page for an existing post, then the car post is added to the model and the "info" view is returned.
     */
    @Test
    void whenGetInfoPageThenCarPostAddedToModelAndInfoViewReturned() {
        var carPost = new CarPost();
        when(carPostService.findById(1)).thenReturn(Optional.of(carPost));

        var model = new ConcurrentModel();
        var view = postController.getInfoPage(1, model);
        var actualCarPost = model.getAttribute("carPost");

        assertThat(view).isEqualTo("posts/info");
        assertThat(actualCarPost).isEqualTo(carPost);
    }

    /**
     * Test case: When getting the info page for a non-existent post, then the 404 error view is returned.
     */
    @Test
    void whenGetInfoPageThenErrorViewReturned() {
        when(carPostService.findById(1000)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = postController.getInfoPage(1000, model);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(errorMessage).isEqualTo("No car post with id 1000");
    }

    /**
     * Test case: When a car post is deleted successfully, then it redirects to the posts page.
     */
    @Test
    void whenDeleteCarPostThenRedirectToPostsPage() {
        when(accessService.checkPermission(1, user)).thenReturn(true);

        var model = new ConcurrentModel();
        var view = postController.deleteCarPost(1, model, "testPath", user);

        assertThat(view).isEqualTo("redirect:/posts");
    }

    /**
     * Test case: When a user tries to delete a post without permission, then the 403 error view is returned.
     */
    @Test
    void whenDeleteCarPostThenErrorViewWithNoPermissionReturned() {
        when(accessService.checkPermission(1, user)).thenReturn(false);

        var model = new ConcurrentModel();
        var view = postController.deleteCarPost(1, model, "testPath", user);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/403");
        assertThat(errorMessage).isEqualTo("No permission to delete this car post");
    }

    /**
     * Test case: When a user tries to delete a non-existent car post, then the 404 error view is returned.
     */
    @Test
    void whenDeleteCarPostThenErrorViewWithNoCarFoundExceptionReturned() throws IOException {
        when(accessService.checkPermission(1000, user)).thenReturn(true);
        Mockito.doThrow(new NoSuchElementException("No car post found with id 1000"))
                .when(carPostService).delete(1000, "testPath");

        var model = new ConcurrentModel();
        var view = postController.deleteCarPost(1000, model, "testPath", user);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(errorMessage).isEqualTo("No car post found with id 1000");
    }

    /**
     * Test case: When getting the edit page for an existing post with permission, then the car post is added to the model and the "edit" page is returned.
     */
    @Test
    void whenGetEditPageThenCarPostAddedToModelAndReturnedEditPage() {
        var carPost = Optional.of(new CarPost());
        when(accessService.checkPermission(1, user)).thenReturn(true);
        when(carPostService.findById(1)).thenReturn(carPost);

        var model = new ConcurrentModel();
        var view = postController.getEditPage(1, model, user);
        var actualCarPost = model.getAttribute("carPost");

        assertThat(view).isEqualTo("posts/edit");
        assertThat(actualCarPost).isEqualTo(carPost.get());
    }

    /**
     * Test case: When a user tries to get the edit page for a post without permission, then the 403 error view is returned.
     */
    @Test
    void whenGetCarPostThenErrorViewWithNoPermissionReturned() {
        when(accessService.checkPermission(1, user)).thenReturn(false);

        var model = new ConcurrentModel();
        var view = postController.getEditPage(1, model, user);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/403");
        assertThat(errorMessage).isEqualTo("No permission to edit this car post");
    }

    /**
     * Test case: When a user tries to get the edit page for a non-existent post, then the 404 error view is returned.
     */
    @Test
    void whenGetCarPostThenErrorViewWithNoCarFoundExceptionReturned() {
        when(accessService.checkPermission(1000, user)).thenReturn(true);

        var model = new ConcurrentModel();
        var view = postController.getEditPage(1000, model, user);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(errorMessage).isEqualTo("No value present");
    }

    /**
     * Test case: When a post is updated successfully, then it redirects to the posts page.
     */
    @Test
    void whenUpdatePostThenRedirectToPosts() throws IOException, InterruptedException {
        CarPost post = new CarPost();
        Mockito.doNothing().when(carPostService).update(any(), any());

        var model = new ConcurrentModel();
        var view = postController.updatePost(post, model, file);

        assertThat(view).isEqualTo("redirect:/posts");
    }

    /**
     * Test case: When updating a post throws an exception (e.g., post not found), then the 404 error view is returned.
     */
    @Test
    void whenUpdatePostThenErrorViewReturned() throws IOException, InterruptedException {
        CarPost post = new CarPost();
        Mockito.doThrow(new NoSuchElementException("Car post not found"))
                .when(carPostService).update(post, file);

        var model = new ConcurrentModel();
        var view = postController.updatePost(post, model, file);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(errorMessage).isEqualTo("Car post not found");
    }

}