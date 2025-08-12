package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class EngineRepository {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param engine двигатель.
     * @return двигатель с id.
     */
    public Engine create(Engine engine) {
        crudRepository.run(session -> session.save(engine));
        return engine;
    }

    /**
     * Обновить в базе двигатель.
     * @param engine двигатель.
     */
    public void update(Engine engine) {
        crudRepository.run(session -> session.merge(engine));
    }

    /**
     * Удалить двигатель по id.
     * @param engineId ID
     */
    public void delete(int engineId) {
        crudRepository.run(
                "delete from Engine where id = :fId",
                Map.of("fId", engineId)
        );
    }

    /**
     * Список двигателей отсортированных по id.
     * @return список двигателей.
     */
    public List<Engine> findAllOrderById() {
        return crudRepository.query("from Engine order by id asc", Engine.class);
    }

    /**
     * Найти двигатель по ID
     * @param engineId ID
     * @return двигатель.
     */
    public Optional<Engine> findById(int engineId) {
        return crudRepository.optional(
                "from Engine where id = :fId", Engine.class,
                Map.of("fId", engineId)
        );
    }

    /**
     * Список двигателей по name LIKE %key%
     * @param name имя
     * @return список двигателей.
     */
    public List<Engine> findByLikeName(String name) {
        return crudRepository.query(
                "from Engine where name like :fName", Engine.class,
                Map.of("fName", "%" + name + "%")
        );
    }

    /**
     * Найти двигатель по name.
     * @param name модель двигатель.
     * @return Optional of engine.
     */
    public Optional<Engine> findByName(String name) {
        return crudRepository.optional(
                "from Engine where name = :fName", Engine.class,
                Map.of("fName", name)
        );
    }
}
