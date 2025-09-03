package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarRepository {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param car автомобиль.
     * @return автомобиль с id.
     */
    public Car create(Car car) {
        crudRepository.run(session -> session.save(car));
        return car;
    }

    /**
     * Обновить в базе автомобиль.
     * @param car автомобиль.
     */
    public void update(Car car) {
        crudRepository.run(session -> session.merge(car));
    }

    /**
     * Удалить автомобиль по id.
     * @param id ID
     */
    public void delete(int id) {
        crudRepository.run(
                "delete from Car where id = :fId",
                Map.of("fId", id)
        );
    }

    /**
     * Список автомобилей отсортированных по id.
     * @return список автомобилей.
     */
    public List<Car> findAllOrderById() {
        return crudRepository.query("from Car order by id asc", Car.class);
    }

    /**
     * Найти автомобиль по ID
     * @param carId ID
     * @return автомобиль.
     */
    public Optional<Car> findById(int carId) {
        return crudRepository.optional(
                "from Car where id = :fId", Car.class,
                Map.of("fId", carId)
        );
    }

    /**
     * Найти автомобиль по бренду и модели, схожими переданным параметрам
     * @param brand бренд автомобиля
     * @param model модель автомобиля
     * @return список автомобилей.
     */
    public List<Car> findByLikeBrandAndModel(String brand, String model) {
        return crudRepository.query(
                "from Car c where LOWER(c.brand) LIKE :fBrand AND LOWER(c.model) LIKE :fModel", Car.class,
                Map.of("fBrand", "%" + brand.toLowerCase().trim() + "%",
                        "fModel", "%" + model.toLowerCase().trim() + "%")
        );
    }

    /**
     * Найти автомобиль по бренду и модели.
     * @param brand бренд автомобиля
     * @param model модель автомобиля
     * @return Optional of car.
     */
    public Optional<Car> findByBrandAndModel(String brand, String model) {
        return crudRepository.optional(
                "from Car c where LOWER(c.brand) = :fBrand AND LOWER(c.model) = :fModel", Car.class,
                Map.of("fBrand", brand.toLowerCase().trim(),
                        "fModel", model.toLowerCase().trim())
        );
    }

    /**
     * Найти все бернды автомобилей
     * @return список брендов в продаже.
     */
    public List<String> findAllCarBrands() {
        return crudRepository.query(
                "select c.brand from Car c", String.class);
    }
}
