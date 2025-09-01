package ru.job4j.cars.service.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.*;

import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
public class SimplePostService implements PostService {
    private PostRepository postRepository;

    @Override
    @Transactional
    public Post create(Post entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }
        if (findByDescription(entity.getDescription()).isPresent()) {
            throw new IllegalArgumentException("Post already exists");
        }
        return postRepository.create(entity);
    }

    @Override
    @Transactional
    public void update(Post entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }
        if (findByDescription(entity.getDescription()).isEmpty()) {
            throw new NoSuchElementException("Post does not exist");
        }
        postRepository.update(entity);
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be negative or zero");
        }
        if (findById(id).isEmpty()) {
            throw new NoSuchElementException("Post does not exist");
        }
        postRepository.delete(id);
    }

    @Override
    public Optional<Post> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be negative or zero");
        }
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAllOrderById() {
        return postRepository.findAllOrderById();
    }

    @Override
    public List<Post> findByLikeDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null/empty");
        }
        return postRepository.findByLikeDescription(description.toLowerCase().trim());
    }

    @Override
    public Optional<Post> findByDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null/empty");
        }
        return postRepository.findByDescription(description.toLowerCase().trim());
    }

    @Override
    public List<Post> findByLastDay() {
        return postRepository.findByLastDay();
    }

    @Override
    public List<Post> findByNotNullPhotoPath() {
        return postRepository.findByNotNullPhotoPath();
    }

    @Override
    public List<Post> findByCarBrand(String brand) {
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("brand cannot be null/empty");
        }
        return postRepository.findByCarBrand(brand.toLowerCase().trim());
    }
}
