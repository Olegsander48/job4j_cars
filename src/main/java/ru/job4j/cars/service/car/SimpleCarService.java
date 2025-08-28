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
    @Transactional
    public Car create(Car entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        if (findByName(entity.getName()).isPresent()) {
            throw new IllegalArgumentException("Car already exists");
        }
        return carRepository.create(entity);
    }

    @Override
    @Transactional
    public void update(Car entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        if (findByName(entity.getName()).isEmpty()) {
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
    public List<Car> findByLikeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null/empty");
        }
        return carRepository.findByLikeName(name.toLowerCase().trim());
    }

    @Override
    public Optional<Car> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null/empty");
        }
        return carRepository.findByName(name.toLowerCase().trim());
    }
}
