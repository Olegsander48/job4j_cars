package ru.job4j.cars.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.configuration.HibernateConfiguration;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class PriceHistoryRepositoryTest {
    private PriceHistoryRepository priceHistoryRepository;
    private PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        priceHistoryRepository = new PriceHistoryRepository(
                new CrudRepository(
                        new HibernateConfiguration().sf()));
        postRepository = new PostRepository(
                new CrudRepository(
                        new HibernateConfiguration().sf()));
    }

    @Test
    void whenCreatePriceHistoryThenDbHasSamePriceHistory() {
        PriceHistory priceHistory = new PriceHistory(1200, 1500, LocalDateTime.now());
        priceHistoryRepository.create(priceHistory);
        Optional<PriceHistory> result = priceHistoryRepository.findById(priceHistory.getId());
        assertThat(result).isPresent()
                .get()
                .isEqualTo(priceHistory);
    }

    @Test
    void whenAdd3PriceHistoriesThenDbHas3PriceHistories() {
        PriceHistory priceHistory1 = new PriceHistory(1200, 1500, LocalDateTime.now());
        PriceHistory priceHistory2 = new PriceHistory(1800, 2500, LocalDateTime.now());
        PriceHistory priceHistory3 = new PriceHistory(5200, 6500, LocalDateTime.now());
        priceHistoryRepository.create(priceHistory1);
        priceHistoryRepository.create(priceHistory2);
        priceHistoryRepository.create(priceHistory3);
        List<PriceHistory> result = priceHistoryRepository.findAllOrderById();
        assertThat(result).hasSize(3)
                .containsOnly(priceHistory1, priceHistory2, priceHistory3);
    }

    @Test
    void whenUpdatePriceHistoryThenDbHasSamePriceHistory() {
        PriceHistory priceHistory = new PriceHistory(1200, 1500, LocalDateTime.now());
        priceHistoryRepository.create(priceHistory);
        priceHistory.setBefore(1000);
        priceHistoryRepository.update(priceHistory);
        Optional<PriceHistory> result = priceHistoryRepository.findById(priceHistory.getId());
        assertThat(result).isPresent()
                .get()
                .isEqualTo(priceHistory);
    }

    @Test
    void whenDeletePriceHistoryThenDbEmpty() {
        PriceHistory priceHistory = new PriceHistory(1200, 1500, LocalDateTime.now());
        priceHistoryRepository.create(priceHistory);
        priceHistoryRepository.delete(priceHistory.getId());
        List<PriceHistory> result = priceHistoryRepository.findAllOrderById();
        assertThat(result).isEmpty();
    }

    @Test
    void whenDeletePriceHistoryByNotExistingIdThenDbNotEmpty() {
        PriceHistory priceHistory = new PriceHistory(1, 1200, 1500, LocalDateTime.now(), null);
        priceHistoryRepository.create(priceHistory);
        priceHistoryRepository.delete(222);
        List<PriceHistory> result = priceHistoryRepository.findAllOrderById();
        assertThat(result).isNotEmpty()
                .hasSize(1)
                .containsOnly(priceHistory);
    }

    @Test
    void whenFindNewestByPostIdThenGetSecondPriceHistory() {
        Post post = new Post("test post");
        postRepository.create(post);

        PriceHistory priceHistory1 = new PriceHistory(1200, 1500, LocalDateTime.of(2020, 1, 1, 1, 1));
        PriceHistory priceHistory2 = new PriceHistory(1800, 2500, LocalDateTime.of(2023, 1, 1, 1, 1));
        PriceHistory priceHistory3 = new PriceHistory(5200, 6500, LocalDateTime.of(2021, 1, 1, 1, 1));

        priceHistory1.setPost(post);
        priceHistory2.setPost(post);
        priceHistory3.setPost(post);

        priceHistoryRepository.create(priceHistory1);
        priceHistoryRepository.create(priceHistory2);
        priceHistoryRepository.create(priceHistory3);
        Optional<PriceHistory> result = priceHistoryRepository.findNewestByCarId(post.getId());
        assertThat(result).isPresent()
                .get()
                .isEqualTo(priceHistory2);
    }

    @Test
    void whenDeleteByPostIdThenDbEmpty() {
        Post post = new Post("test post");
        postRepository.create(post);

        PriceHistory priceHistory1 = new PriceHistory(1200, 1500, LocalDateTime.of(2020, 1, 1, 1, 1));
        PriceHistory priceHistory2 = new PriceHistory(1800, 2500, LocalDateTime.of(2023, 1, 1, 1, 1));
        PriceHistory priceHistory3 = new PriceHistory(5200, 6500, LocalDateTime.of(2021, 1, 1, 1, 1));

        priceHistory1.setPost(post);
        priceHistory2.setPost(post);
        priceHistory3.setPost(post);

        priceHistoryRepository.create(priceHistory1);
        priceHistoryRepository.create(priceHistory2);
        priceHistoryRepository.create(priceHistory3);
        priceHistoryRepository.deleteByPostId(post.getId());
        List<PriceHistory> result = priceHistoryRepository.findAllOrderById();
        assertThat(result).isEmpty();
    }

    @Test
    void whenDeleteByPostId200ThenDbNotEmpty() {
        Post post = new Post("test post");
        postRepository.create(post);

        PriceHistory priceHistory1 = new PriceHistory(1200, 1500, LocalDateTime.of(2020, 1, 1, 1, 1));
        PriceHistory priceHistory2 = new PriceHistory(1800, 2500, LocalDateTime.of(2023, 1, 1, 1, 1));
        PriceHistory priceHistory3 = new PriceHistory(5200, 6500, LocalDateTime.of(2021, 1, 1, 1, 1));

        priceHistory1.setPost(post);
        priceHistory2.setPost(post);
        priceHistory3.setPost(post);

        priceHistoryRepository.create(priceHistory1);
        priceHistoryRepository.create(priceHistory2);
        priceHistoryRepository.create(priceHistory3);
        priceHistoryRepository.deleteByPostId(200);
        List<PriceHistory> result = priceHistoryRepository.findAllOrderById();
        assertThat(result).isNotEmpty()
                .hasSize(3)
                .containsOnly(priceHistory1, priceHistory2, priceHistory3);
    }

}