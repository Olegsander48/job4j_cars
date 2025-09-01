package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.CarPost;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.carpost.CarPostService;
import ru.job4j.cars.service.post.PostService;
import ru.job4j.cars.utility.PhotoUtility;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private PostService postService;
    private PhotoUtility photoSaver;
    private CarPostService carPostService;

    @GetMapping
    public String getAllPosts(Model model) {
        model.addAttribute("carPosts", carPostService.findAllOrderById());
        return "posts/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("carPost", new CarPost());
        return "posts/create";
    }

    @PostMapping("/create")
    public String createCarPost(@ModelAttribute("carPost") CarPost carPost, @RequestParam MultipartFile file,
                                @SessionAttribute User user, Model model) {
        try {
            photoSaver.savePhoto(file);
            carPost.setPhotoPath(file.getOriginalFilename());
            carPost.setUserId(user.getId());
            carPostService.create(carPost);
            return "redirect:/posts";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
    }

    @GetMapping("/info/{id}")
    public String getInfoPage(@PathVariable int id, Model model) {
        try {
            Optional<CarPost> carPost = carPostService.findById(id);
            if (carPost.isEmpty()) {
                throw new NoSuchElementException("No car post with id " + id);
            }
            model.addAttribute("carPost", carPost.get());
            return "posts/info";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCarPost(@PathVariable int id, Model model, @RequestParam String photoPath) {
        try {
            photoSaver.deletePhoto(photoPath);
            carPostService.delete(id);
            return "redirect:/posts";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
    }
}
