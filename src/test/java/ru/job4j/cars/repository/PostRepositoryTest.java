package ru.job4j.cars.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class PostRepositoryTest {
    private PostRepository postRepository;
    private CarRepository carRepository;

    @BeforeEach
    void beforeEach() {
        postRepository = new PostRepository(
                new CrudRepository(
                        new HibernateConfiguration().sf()));
        carRepository = new CarRepository(
                new CrudRepository(
                        new HibernateConfiguration().sf()));
    }

    @Test
    void whenCreatePostThenDbHasSamePost() {
        Post post = new Post("test description");
        postRepository.create(post);
        Optional<Post> result = postRepository.findById(post.getId());
        assertThat(result).isPresent()
                .map(Post::getDescription)
                .hasValue(post.getDescription());
    }

    @Test
    void whenAdd3PostsThenDbHas3Posts() {
        Post post1 = new Post("test description1");
        Post post2 = new Post("test description2");
        Post post3 = new Post("test description3");
        postRepository.create(post1);
        postRepository.create(post2);
        postRepository.create(post3);
        List<Post> result = postRepository.findAllOrderById();
        assertThat(result).hasSize(3)
                .extracting(Post::getDescription)
                .containsExactlyInAnyOrder(post1.getDescription(), post2.getDescription(), post3.getDescription());
    }

    @Test
    void whenUpdatePostThenDbHasSamePost() {
        Post post = new Post("test description");
        postRepository.create(post);
        post.setDescription("new description");
        postRepository.update(post);
        Optional<Post> result = postRepository.findById(post.getId());
        assertThat(result).isPresent()
                .map(Post::getDescription)
                .hasValue(post.getDescription());
    }

    @Test
    void whenDeletePostThenDbEmpty() {
        Post post = new Post("test description");
        postRepository.create(post);
        postRepository.delete(post.getId());
        List<Post> result = postRepository.findAllOrderById();
        assertThat(result).isEmpty();
    }

    @Test
    void whenDeletePostByNotExistingIdThenDbNotEmpty() {
        Post post = new Post("test description");
        post.setId(1);
        postRepository.create(post);
        postRepository.delete(222);
        List<Post> result = postRepository.findAllOrderById();
        assertThat(result).isNotEmpty()
                .hasSize(1)
                .map(Post::getDescription)
                .containsExactly(post.getDescription());
    }

    @Test
    void whenFindByDescriptionLikeTestThenDbHasTwoPosts() {
        Post post1 = new Post("test description1");
        Post post2 = new Post("test description2");
        postRepository.create(post1);
        postRepository.create(post2);
        List<Post> result = postRepository.findByLikeDescription("test");
        assertThat(result).isNotEmpty()
                .hasSize(2)
                .extracting(Post::getDescription)
                .contains(post1.getDescription(), post2.getDescription());
    }

    @Test
    void whenFindByDescriptionLikeNullThenDbHasNoSuchElements() {
        Post post1 = new Post("test description1");
        Post post2 = new Post("test description2");
        postRepository.create(post1);
        postRepository.create(post2);
        List<Post> result = postRepository.findByLikeDescription("Null");
        assertThat(result).isEmpty();
    }

    @Test
    void whenFindByDescriptionTestThenDbHasPost() {
        Post post1 = new Post("test description1");
        Post post2 = new Post("test");
        Post post3 = new Post("test description3");
        postRepository.create(post1);
        postRepository.create(post2);
        postRepository.create(post3);
        Optional<Post> result = postRepository.findByDescription("test");
        assertThat(result).isPresent()
                .map(Post::getDescription)
                .hasValue(post2.getDescription());
    }

    @Test
    void whenFindByPostNullThenDbHasNoSuchElements() {
        Post post1 = new Post("test description1");
        Post post2 = new Post("test description2");
        Post post3 = new Post("test description3");
        postRepository.create(post1);
        postRepository.create(post2);
        postRepository.create(post3);
        Optional<Post> result = postRepository.findByDescription("Null");
        assertThat(result).isEmpty();
    }

    @Test
    void whenFindByLastDayThenDbHasOnePost() {
        Post post1 = new Post("test description1");
        post1.setCreated(LocalDateTime.now());
        Post post2 = new Post("test description2");
        post2.setCreated(LocalDateTime.of(2020, 6, 12, 5, 12));
        postRepository.create(post1);
        postRepository.create(post2);
        List<Post> result = postRepository.findByLastDay();
        assertThat(result).isNotEmpty()
                .hasSize(1)
                .extracting(Post::getDescription)
                .containsOnly(post1.getDescription());
    }

    @Test
    void whenFindByLastDayThenDbHasNoSuchElements() {
        Post post1 = new Post("test description1");
        post1.setCreated(LocalDateTime.of(2010, 6, 12, 5, 12));
        Post post2 = new Post("test description2");
        post2.setCreated(LocalDateTime.of(2020, 6, 12, 5, 12));
        postRepository.create(post1);
        postRepository.create(post2);
        List<Post> result = postRepository.findByLastDay();
        assertThat(result).isEmpty();
    }

    @Test
    void whenFindByNotNullPhotoPathThenDbHasOnePost() {
        Post post1 = new Post("test description1");
        post1.setPhotoPath("C//test.jpg");
        Post post2 = new Post("test description2");
        post2.setPhotoPath("C//projects/job4j_cars/test.jpg");
        Post post3 = new Post("test description3");
        postRepository.create(post1);
        postRepository.create(post2);
        postRepository.create(post3);
        List<Post> result = postRepository.findByNotNullPhotoPath();
        assertThat(result).isNotEmpty()
                .hasSize(2)
                .extracting(Post::getPhotoPath)
                .containsOnly(post1.getPhotoPath(), post2.getPhotoPath());
    }

    @Test
    void whenFindByNotNullPhotoPathThenDbHasNoSuchElements() {
        Post post1 = new Post("test description1");
        Post post2 = new Post("test description2");
        Post post3 = new Post("test description3");
        postRepository.create(post1);
        postRepository.create(post2);
        postRepository.create(post3);
        List<Post> result = postRepository.findByNotNullPhotoPath();
        assertThat(result).isEmpty();
    }

    @Test
    void whenFindByCarBrandMercedesThenDbHasOnePost() {
        Car car1 = new Car("Mercedes", null, null, null);
        Car car2 = new Car("BMW", null, null, null);
        Car car3 = new Car("Audi", null, null, null);
        carRepository.create(car1);
        carRepository.create(car2);
        carRepository.create(car3);

        Post post1 = new Post("test description1");
        post1.setCar(car1);
        Post post2 = new Post("test description2");
        post2.setCar(car2);
        Post post3 = new Post("test description3");
        post3.setCar(car3);
        postRepository.create(post1);
        postRepository.create(post2);
        postRepository.create(post3);
        List<Post> result = postRepository.findByCarBrand("Mercedes");
        assertThat(result).isNotEmpty()
                .hasSize(1)
                .extracting(Post::getDescription)
                .containsOnly(post1.getDescription());
    }

    @Test
    void whenFindByCarBrandOpelThenDbHasNoSuchElements() {
        Car car1 = new Car("Mercedes", null, null, null);
        Car car2 = new Car("BMW", null, null, null);
        Car car3 = new Car("Audi", null, null, null);
        carRepository.create(car1);
        carRepository.create(car2);
        carRepository.create(car3);

        Post post1 = new Post("test description1");
        post1.setCar(car1);
        Post post2 = new Post("test description2");
        post2.setCar(car2);
        Post post3 = new Post("test description3");
        post3.setCar(car3);
        postRepository.create(post1);
        postRepository.create(post2);
        postRepository.create(post3);
        List<Post> result = postRepository.findByCarBrand("Opel");
        assertThat(result).isEmpty();
    }
}