package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostRepository {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param post объявление.
     * @return объявление с id.
     */
    public Post create(Post post) {
        crudRepository.run(session -> session.save(post));
        return post;
    }

    /**
     * Обновить в базе объявление.
     * @param post объявление.
     */
    public void update(Post post) {
        crudRepository.run(session -> session.merge(post));
    }

    /**
     * Удалить объявление по id.
     * @param postId ID
     */
    public void delete(int postId) {
        crudRepository.run(
                "delete from Post where id = :postId",
                Map.of("postId", postId)
        );
    }

    /**
     * Список объявлений, отсортированных по id.
     * @return список объявлений.
     */
    public List<Post> findAllOrderById() {
        return crudRepository.query("from Post order by id asc", Post.class);
    }

    /**
     * Найти объявление по ID
     * @param postId ID
     * @return объявление.
     */
    public Optional<Post> findById(int postId) {
        return crudRepository.optional(
                "from Post where id = :postId", Post.class,
                Map.of("postId", postId)
        );
    }

    /**
     * Список объявлений по description LIKE %key%
     * @param description описание
     * @return список объявлений.
     */
    public List<Post> findByLikeDescription(String description) {
        return crudRepository.query(
                "from Post where description like :fDescription", Post.class,
                Map.of("fDescription", "%" + description + "%")
        );
    }

    /**
     * Найти объявление по description.
     * @param description описание объявления.
     * @return Optional of Post.
     */
    public Optional<Post> findByDescription(String description) {
        return crudRepository.optional(
                "from Post where description = :fDescription", Post.class,
                Map.of("fDescription", description)
        );
    }

    /**
     * Список объявлений за последний день
     * @return список объявлений.
     */
    public List<Post> findByLastDay() {
        return crudRepository.query(
                "from Post where extract(epoch from (current_timestamp - created)) / 3600 <= 24", Post.class);
    }

    /**
     * Список объявлений за с фотографиями
     * @return список объявлений.
     */
    public List<Post> findByNotNullPhotoPath() {
        return crudRepository.query(
                "from Post WHERE photo_path is NOT NULL", Post.class);
    }

    /**
     * Список объявлений определенной марки
     * @param brand бренд автомобиля
     * @return список объявлений.
     */
    public List<Post> findByCarBrand(String brand) {
        return crudRepository.query(
                "from Post p JOIN FETCH p.car where name LIKE :carName", Post.class,
                Map.of("carName", "%" + brand + "%"));
    }
}
