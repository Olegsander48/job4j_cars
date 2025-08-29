package ru.job4j.cars.utility;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PhotoSaver {
    public static void savePhoto(MultipartFile file) throws IOException {
        Path filePath = Paths.get("src/main/resources/static/files", file.getOriginalFilename());
        file.transferTo(filePath);
    }
}
