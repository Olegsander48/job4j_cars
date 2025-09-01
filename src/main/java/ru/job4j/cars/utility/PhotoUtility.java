package ru.job4j.cars.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PhotoUtility {
    @Value("${app.upload.path}")
    private String photoPath;

    public void savePhoto(MultipartFile file) throws IOException, InterruptedException {
        Path filePath = Paths.get(photoPath, file.getOriginalFilename());
        Files.createDirectories(filePath.getParent());
        file.transferTo(filePath);
        Thread.sleep(1);
    }

    public void deletePhoto(String photoPath) throws IOException {
        Path filePath = Paths.get(this.photoPath, photoPath);
        Files.deleteIfExists(filePath);
    }
}
