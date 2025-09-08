package ru.job4j.cars.service.carpost;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.CarPost;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.car.CarService;
import ru.job4j.cars.service.engine.EngineService;
import ru.job4j.cars.service.owner.OwnerService;
import ru.job4j.cars.service.post.PostService;
import ru.job4j.cars.service.pricehistory.PriceHistoryService;
import ru.job4j.cars.service.user.UserService;
import ru.job4j.cars.utility.PhotoUtility;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class SimpleCarPostService implements CarPostService {
    private final PostService postService;
    private final CarService carService;
    private final EngineService engineService;
    private final OwnerService ownerService;
    private final UserService userService;
    private final PriceHistoryService priceHistoryService;
    private final PhotoUtility photoUtility;

    @Override
    @Transactional
    public CarPost create(CarPost carPost) {
        User user = userService.findById(carPost.getUserId()).orElseThrow(NoSuchElementException::new);
        Owner owner = ownerService.findByUserId(user.getId())
                .orElseGet(() -> ownerService.create(new Owner(user.getName(), user)));
        Engine engine = engineService.findById(carPost.getEngineId())
                .orElseGet(() -> engineService.create(new Engine(carPost.getEngine())));

        Car car = carService.create(new Car(carPost.getBrand(),
                                                carPost.getModel(),
                                                engine,
                                                owner,
                                                new HashSet<>(List.of(owner)),
                                                carPost.getCarBody()));
        Post post = postService.create(new Post(
                carPost.getDescription(),
                carPost.getCreated(),
                carPost.getPhotoPath(),
                user,
                new ArrayList<>(),
                car));
        PriceHistory priceHistory = priceHistoryService.create(
                new PriceHistory(carPost.getPrice(), carPost.getPrice(), carPost.getCreated(), post));
        post.getPriceHistory().add(priceHistory);
        return carPost;
    }

    @Override
    @Transactional
    public CarPost create(CarPost entity, MultipartFile file) throws IOException, InterruptedException {
        photoUtility.savePhoto(file);
        entity.setPhotoPath(file.getOriginalFilename());
        return create(entity);
    }

    @Override
    @Transactional
    public void update(CarPost entity) {
        if (findById(entity.getId()).isEmpty()) {
            throw new NoSuchElementException("Car post not found");
        }
        Optional<Engine> engine = engineService.findById(entity.getEngineId());
        if (engine.isPresent()) {
            engine.get().setName(entity.getEngine());
            engineService.update(engine.get());
        }

        Optional<Post> post = postService.findById(entity.getId());
        if (post.isPresent()) {
            post.get().setDescription(entity.getDescription());
            post.get().setPhotoPath(entity.getPhotoPath());
            postService.update(post.get());
        }

        Optional<Car> carOptional = carService.findById(entity.getCarId());
        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            car.setCarBody(entity.getCarBody());
            car.setModel(entity.getModel());
            car.setBrand(entity.getBrand());
            carService.update(car);
        }

        Optional<PriceHistory> priceHistory = priceHistoryService.findNewestByPostId(entity.getId());
        if (priceHistory.isPresent() && priceHistory.get().getAfter() != entity.getPrice()) {
            priceHistoryService.create(new PriceHistory(
                    priceHistory.get().getAfter(),
                    entity.getPrice(),
                    entity.getCreated(),
                    post.get()));
        }
    }

    @Override
    @Transactional
    public void update(CarPost entity, MultipartFile file) throws IOException, InterruptedException {
        if (!file.isEmpty()) {
            photoUtility.savePhoto(file);
            entity.setPhotoPath(file.getOriginalFilename());
        }
        update(entity);
    }

    @Override
    @Transactional
    public void delete(int id, String photoPath) throws IOException {
        photoUtility.deletePhoto(photoPath);
        delete(id);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Optional<CarPost> carPost = findById(id);
        if (carPost.isEmpty()) {
            throw new NoSuchElementException("No car post found with id " + id);
        }
        priceHistoryService.deleteByPostId(id);
        postService.delete(id);
        Optional<Car> carOptional = carService.findByBrandAndModel(carPost.get().getBrand(), carPost.get().getModel());
        carOptional.ifPresent(car -> carService.delete(car.getId()));
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
        return postService.findAllOrderById().stream()
                .map(post -> new CarPost(
                        post.getId(),
                        post.getCar().getBrand(),
                        post.getCar().getModel(),
                        post.getCar().getEngine().getId(),
                        post.getCar().getEngine().getName(),
                        priceHistoryService.findNewestByPostId(post.getId())
                                .orElseThrow(NoSuchElementException::new)
                                .getAfter(),
                        post.getPhotoPath(),
                        post.getDescription(),
                        post.getUser().getId(),
                        post.getCar().getId(),
                        post.getCar().getCarBody(),
                        post.getCreated()))
                .toList();
    }
}
