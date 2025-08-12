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
     * @param carId ID
     */
    public void delete(int carId) {
        crudRepository.run(
                "delete from Car where id = :fId",
                Map.of("fId", carId)
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
     * Список автомобилей по name LIKE %key%
     * @param name имя
     * @return список автомобилей.
     */
    public List<Car> findByLikeName(String name) {
        return crudRepository.query(
                "from Car where name like :fName", Car.class,
                Map.of("fName", "%" + name + "%")
        );
    }

    /**
     * Найти автомобиль по name.
     * @param name модель автомобиля.
     * @return Optional of car.
     */
    public Optional<Car> findByName(String name) {
        return crudRepository.optional(
                "from Car where name = :fName", Car.class,
                Map.of("fName", name)
        );
    }
}
