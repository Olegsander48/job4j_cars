package ru.job4j.cars.security;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.CarPost;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.carpost.CarPostService;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccessService {
    private final CarPostService carPostService;

    @Transactional
    public boolean checkPermission(int carPostId, User user) {
        Optional<CarPost> carPost = carPostService.findById(carPostId);
        if (carPost.isEmpty()) {
            throw new NoSuchElementException("No car post found with id " + carPostId);
        }
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        return carPost.get().getUserId() == user.getId();
    }
}
