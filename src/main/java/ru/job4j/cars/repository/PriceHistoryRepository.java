package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.PriceHistory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PriceHistoryRepository {
    private CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param priceHistory история изменения цены.
     * @return история с id.
     */
    public PriceHistory create(PriceHistory priceHistory) {
        crudRepository.run(session -> session.save(priceHistory));
        return priceHistory;
    }

    /**
     * Обновить в базе историю изменения цены.
     * @param priceHistory история.
     */
    public void update(PriceHistory priceHistory) {
        crudRepository.run(session -> session.merge(priceHistory));
    }

    /**
     * Удалить историю изменения по id.
     * @param priceHistoryId ID
     */
    public void delete(int priceHistoryId) {
        crudRepository.run(
                "delete from PriceHistory where id = :priceHistoryId",
                Map.of("priceHistoryId", priceHistoryId)
        );
    }

    /**
     * Список истории изменений, отсортированных по id.
     * @return список объявлений.
     */
    public List<PriceHistory> findAllOrderById() {
        return crudRepository.query("from PriceHistory order by id asc", PriceHistory.class);
    }

    /**
     * Найти историю изменений цены по ID
     * @param priceHistoryId ID
     * @return история.
     */
    public Optional<PriceHistory> findById(int priceHistoryId) {
        return crudRepository.optional(
                "from PriceHistory where id = :priceHistoryId", PriceHistory.class,
                Map.of("priceHistoryId", priceHistoryId)
        );
    }

    /**
     * Найти самую последнюю запись изменения цены по postId
     * @param postId Id поста (публикации, объявления)
     * @return самая последняя запись.
     */
    public Optional<PriceHistory> findNewestByCarId(int postId) {
        List<PriceHistory> priceHistoryList = crudRepository.query(
                "from PriceHistory ph where ph.post.id = :fPostId order by created desc", PriceHistory.class,
                Map.of("fPostId", postId));
        return priceHistoryList.stream().findFirst();
    }
}
