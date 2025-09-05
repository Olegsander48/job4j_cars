package ru.job4j.cars.service.pricehistory;

import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.service.CrudService;
import java.util.Optional;

public interface PriceHistoryService extends CrudService<PriceHistory> {
    Optional<PriceHistory> findNewestByPostId(int postId);

    void deleteByPostId(int postId);
}
