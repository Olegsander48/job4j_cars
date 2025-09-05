package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.CarPost;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.car.CarService;
import ru.job4j.cars.service.carpost.CarPostService;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private CarService carService;
    private CarPostService carPostService;

    @GetMapping
    public String getAllPosts(Model model) {
        model.addAttribute("carPosts", carPostService.findAllOrderById());
        model.addAttribute("carBrands", carService.findAllCarBrands());
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
            carPost.setUserId(user.getId());
            carPostService.create(carPost, file);
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
    public String deleteCarPost(@PathVariable int id, Model model, @RequestParam String photoPath,
                                @SessionAttribute User user) {
        try {
            if (!carPostService.checkPermission(id, user)) {
                model.addAttribute("message", "No permission to delete this car post");
                return "fragments/errors/403";
            }
            carPostService.delete(id, photoPath);
            return "redirect:/posts";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(@PathVariable int id, Model model, @SessionAttribute User user) {
        try {
            if (!carPostService.checkPermission(id, user)) {
                model.addAttribute("message", "No permission to edit this car post");
                return "fragments/errors/403";
            }
            var carPost = carPostService.findById(id);
            model.addAttribute("carPost", carPost.get());
            return "posts/edit";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
    }

    @PostMapping("/edit")
    public String updatePost(@ModelAttribute CarPost carPost, Model model, @RequestParam("file") MultipartFile file) {
        try {
            carPostService.update(carPost, file);
            return "redirect:/posts";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
    }
}
