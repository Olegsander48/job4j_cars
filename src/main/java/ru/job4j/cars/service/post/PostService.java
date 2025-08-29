package ru.job4j.cars.service.post;

import ru.job4j.cars.dto.CarPost;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.service.CrudService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface PostService extends CrudService<Post> {
    List<Post> findByLikeDescription(String description);

    Optional<Post> findByDescription(String description);

    List<Post> findByLastDay();

    List<Post> findByNotNullPhotoPath();

    List<Post> findByCarBrand(String brand);

    List<CarPost> findALlCarPosts();

    @Transactional
    void saveCarPost(CarPost carPost);
}
