package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OwnerRepository {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param owner владелец.
     * @return владелец с id.
     */
    public Owner create(Owner owner) {
        crudRepository.run(session -> session.save(owner));
        return owner;
    }

    /**
     * Обновить в базе владелеца.
     * @param owner владелец.
     */
    public void update(Owner owner) {
        crudRepository.run(session -> session.merge(owner));
    }

    /**
     * Удалить владелеца по id.
     * @param ownerId ID
     */
    public void delete(int ownerId) {
        crudRepository.run(
                "delete from Owner where id = :fId",
                Map.of("fId", ownerId)
        );
    }

    /**
     * Список владелецев отсортированных по id.
     * @return список владелецев.
     */
    public List<Owner> findAllOrderById() {
        return crudRepository.query("from Owner order by id asc", Owner.class);
    }

    /**
     * Найти владельца по ID
     * @param ownerId ID
     * @return владелец.
     */
    public Optional<Owner> findById(int ownerId) {
        return crudRepository.optional(
                "from Owner where id = :fId", Owner.class,
                Map.of("fId", ownerId)
        );
    }

    /**
     * Список владельцев с name LIKE %key%
     * @param name имя
     * @return список владельцев.
     */
    public List<Owner> findByLikeName(String name) {
        return crudRepository.query(
                "from Owner where name like :fName", Owner.class,
                Map.of("fName", "%" + name + "%")
        );
    }

    /**
     * Найти владельца по name.
     * @param name имя владельца.
     * @return Optional of owner.
     */
    public Optional<Owner> findByName(String name) {
        return crudRepository.optional(
                "from Owner where name = :fName", Owner.class,
                Map.of("fName", name)
        );
    }

    /**
     * Найти владельца по user id.
     * @param userId айди пользователя.
     * @return Optional of owner.
     */
    public Optional<Owner> findByUserId(int userId) {
        List<Owner> ownerList = crudRepository.query(
                "from Owner where user.id = :userId", Owner.class,
                Map.of("userId", userId)
        );
        return ownerList.stream().findFirst();
    }
}
