package ru.job4j.cars.service.carpost;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.CarPost;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.*;

import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
public class SimpleCarPostService implements CarPostService {
    private PostRepository postRepository;
    private CarRepository carRepository;
    private EngineRepository engineRepository;
    private OwnerRepository ownerRepository;
    private UserRepository userRepository;
    private PriceHistoryRepository priceHistoryRepository;

    @Override
    @Transactional
    public CarPost create(CarPost carPost) {
        User user = userRepository.findById(carPost.getUserId()).orElseThrow(NoSuchElementException::new);
        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseGet(() -> ownerRepository.create(new Owner(user.getName(), user)));
        Engine engine = engineRepository.findByName(carPost.getEngine())
                .orElseGet(() -> engineRepository.create(new Engine(carPost.getEngine())));

        Car car = carRepository.create(new Car(carPost.getBrand(),
                                                carPost.getModel(),
                                                engine,
                                                owner,
                                                new HashSet<>(List.of(owner)),
                                                carPost.getCarBody()));
        Post post = postRepository.create(new Post(
                carPost.getDescription(),
                carPost.getCreated(),
                carPost.getPhotoPath(),
                user,
                new ArrayList<>(),
                car));
        PriceHistory priceHistory = priceHistoryRepository.create(
                new PriceHistory(carPost.getPrice(), carPost.getPrice(), carPost.getCreated(), post));
        post.getPriceHistory().add(priceHistory);
        return carPost;
    }

    @Override
    public void update(CarPost entity) {

    }

    @Override
    @Transactional
    public void delete(int id) {
        Optional<CarPost> carPost = findById(id);
        if (carPost.isEmpty()) {
            throw new NoSuchElementException("No car post found with id " + id);
        }
        priceHistoryRepository.deleteByPostId(id);
        postRepository.delete(id);
        Optional<Car> carOptional = carRepository.findByBrandAndModel(carPost.get().getBrand(), carPost.get().getModel());
        carOptional.ifPresent(car -> carRepository.delete(car.getId()));
    }

    @Override
    @Transactional
    public Optional<CarPost> findById(int id) {
        return findAllOrderById().stream()
                .filter(carPost -> carPost.getId() == id)
                .findFirst();
    }

    @Override
    @Transactional
    public List<CarPost> findAllOrderById() {
        return postRepository.findAllOrderById().stream()
                .map(post -> new CarPost(
                        post.getId(),
                        post.getCar().getBrand(),
                        post.getCar().getModel(),
                        post.getCar().getEngine().getName(),
                        priceHistoryRepository.findNewestByCarId(post.getId())
                                .orElseThrow(NoSuchElementException::new)
                                .getAfter(),
                        post.getPhotoPath(),
                        post.getDescription(),
                        post.getUser().getId(),
                        post.getCar().getCarBody(),
                        post.getCreated()))
                .toList();
    }
}
