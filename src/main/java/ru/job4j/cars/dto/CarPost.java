package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class CarPost {
    private int id;
    private String model;
    private String engine;
    private long price;
    private String photoPath;
    private String description;
    private int userId;
    private LocalDateTime created;

    public CarPost() {
        created = LocalDateTime.now();
    }

    public String getFormattedCreationDate() {
        return created.format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }
}
