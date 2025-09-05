package ru.job4j.cars.service.pricehistory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.PriceHistoryRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimplePriceHistoryService implements PriceHistoryService {
    private final PriceHistoryRepository priceHistoryRepository;

    @Override
    public PriceHistory create(PriceHistory entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Price history cannot be null");
        }
        return priceHistoryRepository.create(entity);
    }

    @Override
    @Transactional
    public void update(PriceHistory entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Price history cannot be null");
        }
        if (findById(entity.getId()).isEmpty()) {
            throw new NoSuchElementException("Price history does not exist");
        }
        priceHistoryRepository.update(entity);
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be negative or zero");
        }
        if (findById(id).isEmpty()) {
            throw new NoSuchElementException("Price history does not exist");
        }
        priceHistoryRepository.delete(id);
    }

    @Override
    public Optional<PriceHistory> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cannot be negative or zero");
        }
        return priceHistoryRepository.findById(id);
    }

    @Override
    public List<PriceHistory> findAllOrderById() {
        return priceHistoryRepository.findAllOrderById();
    }

    @Override
    public Optional<PriceHistory> findNewestByPostId(int carId) {
        return priceHistoryRepository.findNewestByPostId(carId);
    }

    @Override
    public void deleteByPostId(int postId) {
        priceHistoryRepository.deleteByPostId(postId);
    }
}
