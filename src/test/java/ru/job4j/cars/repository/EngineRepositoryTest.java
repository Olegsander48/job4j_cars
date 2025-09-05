package ru.job4j.cars.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Optional;

class EngineRepositoryTest {
    private EngineRepository engineRepository;

    @BeforeEach
    void beforeEach() {
        engineRepository = new EngineRepository(
                new CrudRepository(
                        new HibernateConfiguration().sf()));
    }

    /**
     * Test case: When creating an engine, then it can be found in the database with the same values.
     */
    @Test
    void whenCreateCarThenDbHasSameCar() {
        Engine engine = new Engine("V12");
        engineRepository.create(engine);
        Optional<Engine> result = engineRepository.findById(engine.getId());
        assertThat(result).isPresent()
                .get()
                .isEqualTo(engine);
    }

    /**
     * Test case: When adding three engines, then the database contains exactly three engines in insertion order.
     */
    @Test
    void whenAdd3EnginesThenDbHas3Engines() {
        Engine engine1 = new Engine("V12");
        Engine engine2 = new Engine("V8");
        Engine engine3 = new Engine("V10");
        engineRepository.create(engine1);
        engineRepository.create(engine2);
        engineRepository.create(engine3);
        List<Engine> result = engineRepository.findAllOrderById();
        assertThat(result).hasSize(3)
                .isEqualTo(List.of(engine1, engine2, engine3));
    }

    /**
     * Test case: When updating an engine, then the database reflects the updated values.
     */
    @Test
    void whenUpdateEngineThenDbHasSameEngine() {
        Engine engine = new Engine("V12");
        engineRepository.create(engine);
        engine.setName("V12 biturbo");
        engineRepository.update(engine);
        Optional<Engine> result = engineRepository.findById(engine.getId());
        assertThat(result).isPresent()
                .get()
                .isEqualTo(engine);
    }

    /**
     * Test case: When deleting an engine, then the database becomes empty.
     */
    @Test
    void whenDeleteEngineThenDbEmpty() {
        Engine engine = new Engine("V12");
        engineRepository.create(engine);
        engineRepository.delete(engine.getId());
        List<Engine> result = engineRepository.findAllOrderById();
        assertThat(result).isEmpty();
    }

    /**
     * Test case: When deleting an engine by a non-existing id, then the database remains unchanged.
     */
    @Test
    void whenDeleteEngineByNotExistingIdThenDbNotEmpty() {
        Engine engine = new Engine(1, "V12");
        engineRepository.create(engine);
        engineRepository.delete(12);
        List<Engine> result = engineRepository.findAllOrderById();
        assertThat(result).isNotEmpty()
                .hasSize(1)
                .containsOnly(engine);
    }

    /**
     * Test case: When searching for engines with name containing "V12",
     * then two matching engines are found.
     */
    @Test
    void whenFindByNameLikeV12ThenDbHasTwoEngines() {
        Engine engine1 = new Engine("V12 biturbo");
        Engine engine2 = new Engine("V8");
        Engine engine3 = new Engine("Yamaha V12 compressor");
        engineRepository.create(engine1);
        engineRepository.create(engine2);
        engineRepository.create(engine3);
        List<Engine> result = engineRepository.findByLikeName("V12");
        assertThat(result).isNotEmpty()
                .hasSize(2)
                .contains(engine1, engine3);
    }

    /**
     * Test case: When searching for engines with name containing "i4",
     * then no engines are found in the database.
     */
    @Test
    void whenFindByNameI4ThenDbHasNoSuchElements() {
        Engine engine1 = new Engine("V12");
        Engine engine2 = new Engine("V8");
        Engine engine3 = new Engine("V10");
        engineRepository.create(engine1);
        engineRepository.create(engine2);
        engineRepository.create(engine3);
        List<Engine> result = engineRepository.findByLikeName("i4");
        assertThat(result).isEmpty();
    }

    /**
     * Test case: When searching for engine with name "V12",
     * then the corresponding engine is found in the database.
     */
    @Test
    void whenFindByNameV12ThenDbHasEngines() {
        Engine engine1 = new Engine("V12");
        Engine engine2 = new Engine("V8");
        Engine engine3 = new Engine("V10");
        engineRepository.create(engine1);
        engineRepository.create(engine2);
        engineRepository.create(engine3);
        Optional<Engine> result = engineRepository.findByName("V12");
        assertThat(result).isPresent()
                .get()
                .isEqualTo(engine1);
    }

    /**
     * Test case: When searching for engine with name "V6",
     * then no engines are found in the database.
     */
    @Test
    void whenFindByNameV6ThenDbHasNoSuchElements() {
        Engine engine1 = new Engine("V12");
        Engine engine2 = new Engine("V8");
        Engine engine3 = new Engine("V10");
        engineRepository.create(engine1);
        engineRepository.create(engine2);
        engineRepository.create(engine3);
        Optional<Engine> result = engineRepository.findByName("V6");
        assertThat(result).isEmpty();
    }
}