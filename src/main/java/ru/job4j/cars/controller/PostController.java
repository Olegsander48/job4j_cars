package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.CarPost;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.service.carpost.CarPostService;
import ru.job4j.cars.utility.PhotoUtility;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private CarRepository carRepository;
    private PhotoUtility photoSaver;
    private CarPostService carPostService;

    @GetMapping
    public String getAllPosts(Model model) {
        model.addAttribute("carPosts", carPostService.findAllOrderById());
        model.addAttribute("carBrands", carRepository.findAllCarBrands());
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
        var carPost = carPostService.findById(id);
        if (carPost.isEmpty()) {
            model.addAttribute("message", "No car post with id " + id);
            return "fragments/errors/404";
        }
        model.addAttribute("carPost", carPost.get());
        return "posts/info";
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

    @GetMapping("/edit/{id}")
    public String getEditPage(@PathVariable int id, Model model, @SessionAttribute User user) {
        var carPost = carPostService.findById(id);
        if (carPost.isEmpty()) {
            model.addAttribute("message", "No car post with id " + id);
            return "fragments/errors/404";
        }
        if (carPost.get().getUserId() != user.getId()) {
            model.addAttribute("message", "No permission to edit this car post");
            return "fragments/errors/403";
        }
        model.addAttribute("carPost", carPost.get());
        return "posts/edit";
    }

    @PostMapping("/edit")
    public String updatePost(@ModelAttribute CarPost carPost, Model model, @RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                photoSaver.savePhoto(file);
                carPost.setPhotoPath(file.getOriginalFilename());
            }
            carPostService.update(carPost);
            return "redirect:/posts";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
    }
}
