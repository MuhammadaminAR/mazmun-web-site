package uz.mazmun.mazmunweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.mazmun.mazmunweb.dto.PostCategoryDTO;
import uz.mazmun.mazmunweb.entity.PostCategory;
import uz.mazmun.mazmunweb.repo.PostCategoryRepository;
import uz.mazmun.mazmunweb.service.util.SlugUtil;

@Service
@RequiredArgsConstructor
public class CategoryService {


    private final PostCategoryRepository postCategoryRepository;

    public PostCategory create(PostCategoryDTO dto) {
        PostCategory postCategory = new PostCategory();
        postCategory.setName(dto.getName());
        postCategory.setDeleted(false);
        postCategory.setSlug(SlugUtil.toSlug(dto.getName()));
        return postCategoryRepository.save(postCategory);
    }
}
