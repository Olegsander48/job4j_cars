package ru.job4j.cars.service.car;

import ru.job4j.cars.model.Car;
import ru.job4j.cars.service.CrudService;

import java.util.List;
import java.util.Optional;

public interface CarService extends CrudService<Car> {
    List<Car> findByLikeBrandAndModel(String brand, String model);

    Optional<Car> findByBrandAndModel(String brand, String model);

    List<String> findAllCarBrands();
}
