package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.mazmun.mazmunweb.service.PostService;
import uz.mazmun.mazmunweb.service.MediaService;

@Controller
@RequiredArgsConstructor
public class PostPageController {

    private final PostService postService;
    private final MediaService mediaService;

    @GetMapping("/post/{slug}")
    public String postPage(@PathVariable String slug, Model model) {

        var p = postService.getBySlug(slug).orElse(null);
        if (p == null) return "errors/404";

        String canonical = "https://mazmunedu.uz/post/" + slug;

        String ogImage = null;
        if (p.getPhoto() != null) {
            ogImage = mediaService.presignGetUrl(p.getPhoto().getId());
        }

        model.addAttribute("title", p.getTitle());
        model.addAttribute("desc",  p.getSummary());
        model.addAttribute("canonical", canonical);
        model.addAttribute("ogImage", ogImage);

        model.addAttribute("content", p.getContent());

        return "post"; // resources/templates/post.html
    }
}
