package ru.job4j.cars.service.owner;

import ru.job4j.cars.model.Owner;
import ru.job4j.cars.service.CrudService;

import java.util.List;
import java.util.Optional;

public interface OwnerService extends CrudService<Owner> {
    List<Owner> findByLikeName(String name);

    Optional<Owner> findByName(String name);

    Optional<Owner> findByUserId(int userId);
}
