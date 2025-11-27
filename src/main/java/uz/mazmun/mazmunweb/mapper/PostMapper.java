package uz.mazmun.mazmunweb.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.mazmun.mazmunweb.dto.PostResDTO;
import uz.mazmun.mazmunweb.entity.Post;
import uz.mazmun.mazmunweb.entity.PostCategory;
import uz.mazmun.mazmunweb.service.MediaService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostMapper {
    private final MediaService mediaService;

    public PostResDTO toDto(Post p) {
        Long mid = (p.getPhoto() != null ? p.getPhoto().getId() : null);
        String url = (mid != null ? mediaService.presignGetUrl(mid) : null);

        List<Integer> catIds = (p.getCategories() == null)
                ? List.of()
                : p.getCategories().stream().map(PostCategory::getId).toList();

        return PostResDTO.builder()
                .id(p.getId())
                .title(p.getTitle())
                .slug(p.getSlug())
                .summary(p.getSummary())
                .content(p.getContent())
                .status(p.getStatus().name())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .publishedAt(p.getPublishedAt())
                .categoryIds(catIds)
                .coverMediaId(mid)
                .coverUrl(url)
                .hashTag(p.getHashTag())
                .build();
    }
}
