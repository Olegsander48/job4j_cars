package ru.job4j.cars.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Optional;

class OwnerRepositoryTest {
    private OwnerRepository ownerRepository;

    @BeforeEach
    void beforeEach() {
        ownerRepository = new OwnerRepository(
                new CrudRepository(
                        new HibernateConfiguration().sf()));
    }

    @Test
    void whenCreateOwnerThenDbHasSameOwner() {
        Owner owner = new Owner("Aleks", null);
        ownerRepository.create(owner);
        Optional<Owner> result = ownerRepository.findById(owner.getId());
        assertThat(result).isPresent()
                .map(Owner::getName)
                .hasValue(owner.getName());
    }

    @Test
    void whenAdd3OwnersThenDbHas3Owners() {
        Owner owner1 = new Owner("Aleks", null);
        Owner owner2 = new Owner("Vladimir", null);
        Owner owner3 = new Owner("Ivan", null);
        ownerRepository.create(owner1);
        ownerRepository.create(owner2);
        ownerRepository.create(owner3);
        List<Owner> result = ownerRepository.findAllOrderById();
        assertThat(result).hasSize(3)
                .extracting(Owner::getName)
                .containsExactlyInAnyOrder(owner1.getName(), owner2.getName(), owner3.getName());
    }

    @Test
    void whenUpdateOwnerThenDbHasSameOwner() {
        Owner owner = new Owner("Aleks", null);
        ownerRepository.create(owner);
        owner.setName("Aleksey Ivanovich");
        ownerRepository.update(owner);
        Optional<Owner> result = ownerRepository.findById(owner.getId());
        assertThat(result).isPresent()
                .map(Owner::getName)
                .hasValue(owner.getName());
    }

    @Test
    void whenDeleteOwnerThenDbEmpty() {
        Owner owner = new Owner("Aleks", null);
        ownerRepository.create(owner);
        ownerRepository.delete(owner.getId());
        List<Owner> result = ownerRepository.findAllOrderById();
        assertThat(result).isEmpty();
    }

    @Test
    void whenDeleteOwnerByNotExistingIdThenDbNotEmpty() {
        Owner owner = new Owner(1, "Aleks", null);
        ownerRepository.create(owner);
        ownerRepository.delete(222);
        List<Owner> result = ownerRepository.findAllOrderById();
        assertThat(result).isNotEmpty()
                .hasSize(1)
                .map(Owner::getName)
                .containsExactly(owner.getName());
    }

    @Test
    void whenFindByNameLikeAleksThenDbHasTwoOwners() {
        Owner owner1 = new Owner("Aleks", null);
        Owner owner2 = new Owner("Aleksandr", null);
        ownerRepository.create(owner1);
        ownerRepository.create(owner2);
        List<Owner> result = ownerRepository.findByLikeName("Aleks");
        assertThat(result).isNotEmpty()
                .hasSize(2)
                .extracting(Owner::getName)
                .contains(owner1.getName(), owner2.getName());
    }

    @Test
    void whenFindByNameLikeIvanThenDbHasNoSuchElements() {
        Owner owner1 = new Owner("Aleks", null);
        Owner owner2 = new Owner("Aleksandr", null);
        ownerRepository.create(owner1);
        ownerRepository.create(owner2);
        List<Owner> result = ownerRepository.findByLikeName("Ivan");
        assertThat(result).isEmpty();
    }

    @Test
    void whenFindByNameAlekseyThenDbHasOwner() {
        Owner owner1 = new Owner("Aleks", null);
        Owner owner2 = new Owner("Aleksandr", null);
        Owner owner3 = new Owner("Aleksey", null);
        ownerRepository.create(owner1);
        ownerRepository.create(owner2);
        ownerRepository.create(owner3);
        Optional<Owner> result = ownerRepository.findByName("Aleksey");
        assertThat(result).isPresent()
                .map(Owner::getName)
                .hasValue(owner3.getName());
    }

    @Test
    void whenFindByNameIvanThenDbHasNoSuchElements() {
        Owner owner1 = new Owner("Aleks", null);
        Owner owner2 = new Owner("Aleksandr", null);
        Owner owner3 = new Owner("Aleksey", null);
        ownerRepository.create(owner1);
        ownerRepository.create(owner2);
        ownerRepository.create(owner3);
        Optional<Owner> result = ownerRepository.findByName("Ivan");
        assertThat(result).isEmpty();
    }
}