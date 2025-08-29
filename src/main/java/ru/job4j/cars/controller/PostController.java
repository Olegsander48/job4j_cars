package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.CarPost;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.post.PostService;
import ru.job4j.cars.utility.PhotoSaver;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private PostService postService;

    @GetMapping
    public String getAllPosts(Model model) {
        model.addAttribute("carPosts", postService.findALlCarPosts());
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
            PhotoSaver.savePhoto(file);
            carPost.setPhotoPath(file.getOriginalFilename());
            carPost.setUserId(user.getId());
            postService.saveCarPost(carPost);
            return "redirect:/posts";
        } catch (Exception exception) {
            model.addAttribute("exception", exception);
            return "fragments/errors/404";
        }
    }
}
