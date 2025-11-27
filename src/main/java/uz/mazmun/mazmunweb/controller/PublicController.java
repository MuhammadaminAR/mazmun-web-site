package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mazmun.mazmunweb.dto.*;
import uz.mazmun.mazmunweb.entity.ProjectImage;
import uz.mazmun.mazmunweb.enums.ProjectImageSlot;
import uz.mazmun.mazmunweb.repo.ProjectImageRepository;
import uz.mazmun.mazmunweb.repo.ProjectRepository;
import uz.mazmun.mazmunweb.service.HomeKpiService;
import uz.mazmun.mazmunweb.service.MediaService;
import uz.mazmun.mazmunweb.service.PostService;
import uz.mazmun.mazmunweb.service.ProjectService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PublicController {
    private final PostService postService;
    private final ProjectService projectService;
    private final MediaService mediaService;
    private final ProjectImageRepository projectImageRepository;
    private final ProjectRepository projectRepository;


    @GetMapping("/posts")
    public Page<PostListItemDTO> posts(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return postService.listPublished(page, size).map(p -> {
            String coverUrl = (p.getPhoto() == null)
                    ? null
                    : mediaService.presignGetUrl(p.getPhoto().getId());

            String publishedAtIso = (p.getPublishedAt() == null) ? null : p.getPublishedAt().toString();

            return new PostListItemDTO(
                    p.getSlug(),
                    p.getTitle(),
                    p.getSummary(),
                    publishedAtIso,
                    coverUrl,
                    p.getHashTag()
            );
        });
    }

    @GetMapping(value = "/project/count", produces = MediaType.TEXT_PLAIN_VALUE)
    public String projectCount() {
        return String.valueOf(projectRepository.count());
    }


    @GetMapping("/posts/search")
    public Page<PostListItemDTO> searchPosts(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "9") int size,
                                             @RequestParam(required = false) String q,
                                             @RequestParam(required = false, name="category") String category,
                                             @RequestParam(required = false, name="tag") String tag,
                                             @RequestParam(defaultValue = "publishedAt,desc") String sort){
        return postService.searchPublic(page, size, sort, q, category, tag)
                .map(p -> {
                    String coverUrl = (p.getPhoto()==null) ? null : mediaService.presignGetUrl(p.getPhoto().getId());
                    String publishedAtIso = (p.getPublishedAt()==null) ? null : p.getPublishedAt().toString();
                    return new PostListItemDTO(p.getSlug(), p.getTitle(), p.getSummary(), publishedAtIso, coverUrl, p.getHashTag());
                });
    }







    @GetMapping("/posts/{slug}")
    public ResponseEntity<PostDetailDTO> post(@PathVariable String slug) {
        return postService.getBySlug(slug)
                .map(p -> {
                    MediaDTO cover = (p.getPhoto() == null) ? null
                            : new MediaDTO(
                            p.getPhoto().getId(),
                            mediaService.presignGetUrl(p.getPhoto().getId()),
                            p.getPhoto().getContentType(),
                            p.getPhoto().getSize()
                    );

                    PostDetailDTO dto = new PostDetailDTO(
                            p.getSlug(),
                            p.getTitle(),
                            p.getContent(),
                            (p.getPublishedAt() == null ? null : p.getPublishedAt().toString()),
                            cover,
                            p.getHashTag()
                    );

                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping("/projects")
    public Page<Map<String,Object>> projects(@RequestParam(defaultValue="0") int page,
                                             @RequestParam(defaultValue="10") int size){
        return projectService.listPublished(page,size).map(pr -> {
            String coverUrl = projectImageRepository
                    .findFirstByProjectIdAndSlotOrderBySortOrderAsc(pr.getId(), ProjectImageSlot.COVER)
                    .map(pi -> mediaService.presignGetUrl(pi.getMedia().getId()))
                    .orElse(null);

            return Map.of(
                    "slug", pr.getSlug(),
                    "overview", pr.getDescription(),
                    "title", pr.getTitle(),
                    "publishedAt", pr.getPublishedAt(),
                    "coverUrl", coverUrl
            );
        });
    }








    @GetMapping("/projects/{slug}")
    public ResponseEntity<ProjectDetailDTO> project(@PathVariable String slug) {
        return projectService.getBySlug(slug)
                .map(pr -> {
                    var imgs = projectImageRepository
                            .findAllByProjectIdOrderBySortOrderAsc(pr.getId()).stream()
                            .map(pi -> new ProjectImageDTO(
                                    pi.getId(),
                                    mediaService.presignGetUrl(pi.getMedia().getId()),
                                    pi.getSlot(),
                                    pi.getSortOrder(),
                                    pi.getAltText(),
                                    pi.getCaption()
                            ))
                            .toList();

                    String coverUrl = projectImageRepository
                            .findFirstByProjectIdAndSlotOrderBySortOrderAsc(pr.getId(), ProjectImageSlot.COVER)
                            .map(pi -> mediaService.presignGetUrl(pi.getMedia().getId()))
                            .orElse(null);

                    var dto = new ProjectDetailDTO(
                            pr.getSlug(),
                            pr.getClientName(),
                            pr.getLocation(),
                            pr.getService(),
                            pr.getDuration(),
                            pr.getTitle(),
                            pr.getDescription(),
                            pr.getPublishedAt() == null ? null : pr.getPublishedAt().toString(),
                            imgs,
                            pr.getApproach(),
                            pr.getConclusion(),
                            pr.getHowWeStarted()
                    );
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
