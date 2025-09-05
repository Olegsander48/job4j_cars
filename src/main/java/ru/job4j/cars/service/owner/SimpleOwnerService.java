package ru.job4j.cars.service.owner;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.OwnerRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleOwnerService implements OwnerService {
    private final OwnerRepository ownerRepository;

    @Override
    @Transactional
    public Owner create(Owner entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        Optional<Owner> owner = findByName(entity.getName());
        return owner.orElseGet(() -> ownerRepository.create(entity));
    }

    @Override
    @Transactional
    public void update(Owner entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        if (findById(entity.getId()).isEmpty()) {
            throw new NoSuchElementException("Owner does not exist");
        }
        ownerRepository.update(entity);
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be negative or zero");
        }
        if (findById(id).isEmpty()) {
            throw new NoSuchElementException("Owner does not exist");
        }
        ownerRepository.delete(id);
    }

    @Override
    public Optional<Owner> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be negative or zero");
        }
        return ownerRepository.findById(id);
    }

    @Override
    public List<Owner> findAllOrderById() {
        return ownerRepository.findAllOrderById();
    }

    @Override
    public List<Owner> findByLikeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null/empty");
        }
        return ownerRepository.findByLikeName(name.toLowerCase().trim());
    }

    @Override
    public Optional<Owner> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null/empty");
        }
        return ownerRepository.findByName(name.toLowerCase().trim());
    }

    @Override
    public Optional<Owner> findByUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("UserId cannot be negative or zero");
        }
        return ownerRepository.findByUserId(userId);
    }
}
