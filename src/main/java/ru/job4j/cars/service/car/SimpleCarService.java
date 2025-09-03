package ru.job4j.cars.service.car;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.CarRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleCarService implements CarService {
    private CarRepository carRepository;

    @Override
    public Car create(Car entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        return carRepository.create(entity);
    }

    @Override
    @Transactional
    public void update(Car entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        if (findByBrandAndModel(entity.getBrand(), entity.getModel()).isEmpty()) {
            throw new NoSuchElementException("Car does not exist");
        }
        carRepository.update(entity);
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be negative or zero");
        }
        if (findById(id).isEmpty()) {
            throw new NoSuchElementException("Car does not exist");
        }
        carRepository.delete(id);
    }

    @Override
    public Optional<Car> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be negative or zero");
        }
        return carRepository.findById(id);
    }

    @Override
    public List<Car> findAllOrderById() {
        return carRepository.findAllOrderById();
    }

    @Override
    public List<Car> findByLikeBrandAndModel(String brand, String model) {
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be null/empty");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be null/empty");
        }
        return carRepository.findByLikeBrandAndModel(brand, model);
    }

    @Override
    public Optional<Car> findByBrandAndModel(String brand, String model) {
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be null/empty");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be null/empty");
        }
        return carRepository.findByBrandAndModel(brand, model);
    }

    @Override
    public List<String> findAllCarBrands() {
        return carRepository.findAllCarBrands();
    }
}
