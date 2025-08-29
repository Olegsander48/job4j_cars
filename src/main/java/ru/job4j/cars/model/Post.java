package ru.job4j.cars.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
    private LocalDateTime created;

    @Column(name = "photo_path")
    private String photoPath;

    @ManyToOne
    @JoinColumn(name = "auto_user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<PriceHistory> priceHistory;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    public Post(String description, LocalDateTime created, String photoPath, User user, List<PriceHistory> priceHistory, Car car) {
        this.description = description;
        this.created = created;
        this.photoPath = photoPath;
        this.user = user;
        this.priceHistory = priceHistory;
        this.car = car;
    }

    public Post(String description) {
        this.description = description;
    }
}
