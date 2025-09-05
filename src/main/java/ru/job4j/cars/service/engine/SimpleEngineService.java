package ru.job4j.cars.service.engine;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.EngineRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleEngineService implements EngineService {
    private final EngineRepository engineRepository;

    @Override
    @Transactional
    public Engine create(Engine entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Engine cannot be null");
        }
        Optional<Engine> engine = findByName(entity.getName());
        return engine.orElseGet(() -> engineRepository.create(entity));
    }

    @Override
    @Transactional
    public void update(Engine entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Engine cannot be null");
        }
        if (findById(entity.getId()).isEmpty()) {
            throw new NoSuchElementException("Engine does not exist");
        }
        engineRepository.update(entity);
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be negative or zero");
        }
        if (findById(id).isEmpty()) {
            throw new NoSuchElementException("Engine does not exist");
        }
        engineRepository.delete(id);
    }

    @Override
    public Optional<Engine> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be negative or zero");
        }
        return engineRepository.findById(id);
    }

    @Override
    public List<Engine> findAllOrderById() {
        return engineRepository.findAllOrderById();
    }

    @Override
    public List<Engine> findByLikeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return engineRepository.findByLikeName(name.toLowerCase().trim());
    }

    @Override
    public Optional<Engine> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return engineRepository.findByName(name.toLowerCase().trim());
    }
}
