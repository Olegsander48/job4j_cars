package ru.job4j.cars.utility;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PhotoSaver {
    public static void savePhoto(MultipartFile file) throws IOException, InterruptedException {
        Path filePath = Paths.get("uploads/photo", file.getOriginalFilename());
        Files.createDirectories(filePath.getParent());
        file.transferTo(filePath);
        Thread.sleep(1);
    }
}
