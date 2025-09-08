package ru.job4j.cars.service.carpost;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.CarPost;
import ru.job4j.cars.service.CrudService;

import java.io.IOException;

public interface CarPostService extends CrudService<CarPost> {
    void update(CarPost entity, MultipartFile file) throws IOException, InterruptedException;

    void delete(int id, String photoPath) throws IOException;

    CarPost create(CarPost entity, MultipartFile file) throws IOException, InterruptedException;
}
