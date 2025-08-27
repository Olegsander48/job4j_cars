package ru.job4j.cars.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Optional;

class CarRepositoryTest {
    private CarRepository carRepository;

    @BeforeEach
    void beforeEach() {
        carRepository = new CarRepository(
                new CrudRepository(
                        new HibernateConfiguration().sf()));
    }

    @Test
    void whenCreateCarThenDbHasSameCar() {
        Car car = new Car("Mercedes", null, null, null);
        carRepository.create(car);
        Optional<Car> result = carRepository.findById(car.getId());
        assertThat(result).isPresent()
                .map(Car::getName)
                .hasValue(car.getName());
    }

    @Test
    void whenAdd3CarsThenDbHas3Cars() {
        Car car1 = new Car("Mercedes", null, null, null);
        Car car2 = new Car("BMW", null, null, null);
        Car car3 = new Car("Audi", null, null, null);
        carRepository.create(car1);
        carRepository.create(car2);
        carRepository.create(car3);
        List<Car> result = carRepository.findAllOrderById();
        assertThat(result).hasSize(3)
                .extracting(Car::getName)
                .containsExactlyInAnyOrder(car1.getName(), car2.getName(), car3.getName());
    }

    @Test
    void whenUpdateCarThenDbHasSameCar() {
        Car car = new Car("Mercedes", null, null, null);
        carRepository.create(car);
        car.setName("Mercedes-Benz cls550");
        carRepository.update(car);
        Optional<Car> result = carRepository.findById(car.getId());
        assertThat(result).isPresent()
                .map(Car::getName)
                .hasValue(car.getName());
    }

    @Test
    void whenDeleteCarThenDbEmpty() {
        Car car = new Car("Mercedes", null, null, null);
        carRepository.create(car);
        carRepository.delete(car.getId());
        List<Car> result = carRepository.findAllOrderById();
        assertThat(result).isEmpty();
    }

    @Test
    void whenDeleteCarByNotExistingIdThenDbNotEmpty() {
        Car car = new Car(1, "Mercedes", null, null, null);
        carRepository.create(car);
        carRepository.delete(222);
        List<Car> result = carRepository.findAllOrderById();
        assertThat(result).isNotEmpty()
                .hasSize(1)
                .map(Car::getName)
                .containsExactly(car.getName());
    }

    @Test
    void whenFindByNameLikeMercedesThenDbHasTwoCar() {
        Car car1 = new Car("Mercedes-Benz cls550", null, null, null);
        Car car2 = new Car("Mercedes G63", null, null, null);
        carRepository.create(car1);
        carRepository.create(car2);
        List<Car> result = carRepository.findByLikeName("Mercedes");
        assertThat(result).isNotEmpty()
                .hasSize(2)
                .extracting(Car::getName)
                .contains(car1.getName(), car2.getName());
    }

    @Test
    void whenFindByNameLikeBMWThenDbHasNoSuchElements() {
        Car car1 = new Car("Mercedes-Benz cls550", null, null, null);
        Car car2 = new Car("Mercedes G63", null, null, null);
        carRepository.create(car1);
        carRepository.create(car2);
        List<Car> result = carRepository.findByLikeName("BMW");
        assertThat(result).isEmpty();
    }

    @Test
    void whenFindByNameMercedesThenDbHasCar() {
        Car car1 = new Car("Audi A8", null, null, null);
        Car car2 = new Car("Mercedes", null, null, null);
        Car car3 = new Car("BMW m5", null, null, null);
        carRepository.create(car1);
        carRepository.create(car2);
        carRepository.create(car3);
        Optional<Car> result = carRepository.findByName("Mercedes");
        assertThat(result).isPresent()
                .map(Car::getName)
                .hasValue(car2.getName());
    }

    @Test
    void whenFindByNameOpelThenDbHasNoSuchElements() {
        Car car1 = new Car("Audi A8", null, null, null);
        Car car2 = new Car("Mercedes G63", null, null, null);
        Car car3 = new Car("BMW m5", null, null, null);
        carRepository.create(car1);
        carRepository.create(car2);
        carRepository.create(car3);
        Optional<Car> result = carRepository.findByName("Opel");
        assertThat(result).isEmpty();
    }
}