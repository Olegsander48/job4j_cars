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
        Car car = new Car("Mercedes", "CLS 63", null, null, null, null);
        carRepository.create(car);
        Optional<Car> result = carRepository.findById(car.getId());
        assertThat(result).isPresent()
                .map(car1 -> car1.getBrand() + car1.getModel())
                .hasValue(car.getBrand() + car.getModel());
    }

    @Test
    void whenAdd3CarsThenDbHas3Cars() {
        Car car1 = new Car("Mercedes", "CLS 63", null, null, null, null);
        Car car2 = new Car("BMW", "M5", null, null, null, null);
        Car car3 = new Car("Audi", "RS6", null, null, null, null);
        carRepository.create(car1);
        carRepository.create(car2);
        carRepository.create(car3);
        List<Car> result = carRepository.findAllOrderById();
        assertThat(result).hasSize(3)
                .extracting(car -> car.getBrand() + car.getModel())
                .containsExactly(car1.getBrand() + car1.getModel(),
                        car2.getBrand() + car2.getModel(),
                        car3.getBrand() + car3.getModel());
    }

    @Test
    void whenUpdateCarThenDbHasSameCar() {
        Car car = new Car("Mercedes", "CLS 63", null, null, null, null);
        carRepository.create(car);
        car.setModel("s500");
        carRepository.update(car);
        Optional<Car> result = carRepository.findById(car.getId());
        assertThat(result).isPresent()
                .map(car1 -> car1.getBrand() + car1.getModel())
                .hasValue(car.getBrand() + car.getModel());
    }

    @Test
    void whenDeleteCarThenDbEmpty() {
        Car car = new Car("Mercedes", "CLS 63", null, null, null, null);
        carRepository.create(car);
        carRepository.delete(car.getId());
        List<Car> result = carRepository.findAllOrderById();
        assertThat(result).isEmpty();
    }

    @Test
    void whenDeleteCarByNotExistingIdThenDbNotEmpty() {
        Car car = new Car(1, "Mercedes", "CLS 63", null, null, null, null);
        carRepository.create(car);
        carRepository.delete(222);
        List<Car> result = carRepository.findAllOrderById();
        assertThat(result).isNotEmpty()
                .hasSize(1)
                .map(car1 -> car1.getBrand() + car1.getModel())
                .containsExactly(car.getBrand() + car.getModel());
    }

    @Test
    void whenFindByBrandLikeMercedesAndModelLikeClsThenDbHasTwoCars() {
        Car car1 = new Car("Mercedes", "CLS 63s", null, null, null, null);
        Car car2 = new Car("Mercedes", "cls550 usa", null, null, null, null);
        carRepository.create(car1);
        carRepository.create(car2);
        List<Car> result = carRepository.findByLikeBrandAndModel("Mercedes", "cls");
        assertThat(result).isNotEmpty()
                .hasSize(2)
                .extracting(car -> car.getBrand() + car.getModel())
                .containsExactly(car1.getBrand() + car1.getModel(), car2.getBrand() + car2.getModel());
    }

    @Test
    void whenFindByBrandLikeOpelAndModelLikeAstraThenDbHasNoSuchElements() {
        Car car1 = new Car("Mercedes", "CLS 63s", null, null, null, null);
        Car car2 = new Car("Mercedes", "cls550 usa", null, null, null, null);
        carRepository.create(car1);
        carRepository.create(car2);
        List<Car> result = carRepository.findByLikeBrandAndModel("Opel", "astra");
        assertThat(result).isEmpty();
    }

    @Test
    void whenFindByBrandMercedesAndModelGleThenDbHasCar() {
        Car car1 = new Car("Mercedes", "GLE", null, null, null, null);
        Car car2 = new Car("BMW", "M5", null, null, null, null);
        Car car3 = new Car("Audi", "RS6", null, null, null, null);
        carRepository.create(car1);
        carRepository.create(car2);
        carRepository.create(car3);
        Optional<Car> result = carRepository.findByBrandAndModel("mercedes", "gle");
        assertThat(result).isPresent()
                .map(car -> car.getBrand() + car.getModel())
                .hasValue(car1.getBrand() + car1.getModel());
    }

    @Test
    void whenFindByBranMercedesAndModelEqsThenDbHasNoSuchElements() {
        Car car1 = new Car("Mercedes", "GLE", null, null, null, null);
        Car car2 = new Car("BMW", "M5", null, null, null, null);
        Car car3 = new Car("Audi", "RS6", null, null, null, null);
        carRepository.create(car1);
        carRepository.create(car2);
        carRepository.create(car3);
        Optional<Car> result = carRepository.findByBrandAndModel("Mercedes", "eqs");
        assertThat(result).isEmpty();
    }
}