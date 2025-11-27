package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.mazmun.mazmunweb.entity.PostCategory;
import uz.mazmun.mazmunweb.repo.PostCategoryRepository;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryPublicController {

    private final PostCategoryRepository repo;

    @GetMapping
    public Page<PostCategory> list(@RequestParam(defaultValue="0") int page,
                                   @RequestParam(defaultValue="12") int size,
                                   @RequestParam(defaultValue="name,asc") String sort) {
        String[] parts = sort.split(",");
        Sort s = Sort.by(parts[0]);
        if (parts.length>1 && parts[1].equalsIgnoreCase("asc")) s = s.ascending(); else s = s.descending();
        return repo.findAll(PageRequest.of(page, size, s));
    }

}

