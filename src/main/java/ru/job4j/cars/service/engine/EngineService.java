package ru.job4j.cars.service.engine;

import ru.job4j.cars.model.Engine;
import ru.job4j.cars.service.CrudService;
import java.util.List;
import java.util.Optional;

public interface EngineService extends CrudService<Engine> {
    List<Engine> findByLikeName(String name);

    Optional<Engine> findByName(String name);
}
