// web/ServiceOfferPublicController.java
package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mazmun.mazmunweb.dto.ServiceOfferDTO;
import uz.mazmun.mazmunweb.entity.Media;
import uz.mazmun.mazmunweb.entity.ServiceOffer;
import uz.mazmun.mazmunweb.enums.Status;
import uz.mazmun.mazmunweb.repo.ServiceOfferRepository;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceOfferPublicController {

    private final ServiceOfferRepository repo;

    @GetMapping
    public Page<ServiceOfferDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "publishedAt,desc") String sort
    ){
        Sort s = Sort.by(sort.split(",")[0]);
        s = sort.toLowerCase().endsWith(",asc") ? s.ascending() : s.descending();

        return repo.findAllByStatus(Status.PUBLISHED, PageRequest.of(page, size, s))
                .map(this::toDto);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ServiceOfferDTO> one(@PathVariable String slug){
        return repo.findBySlug(slug)
                .filter(it -> it.getStatus()==Status.PUBLISHED)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ====== helpers (mapper YO‘Q) ======
    private ServiceOfferDTO toDto(ServiceOffer s){
        return new ServiceOfferDTO(
                s.getId(),
                s.getSlug(),
                s.getTitle(),
                s.getSubtitle(),
                s.getOverview(),
                splitBullets(s.getBullets()),
                s.getQuoteText(),
                s.getQuoteAuthor(),
                s.getQuoteRole(),
                mediaUrl(s.getHeroPhoto()),
                mediaUrl(s.getQuoteAuthorPhoto()),
                mediaUrl(s.getImageLeft()),
                mediaUrl(s.getImageRight()),
                mediaUrl(s.getIcon()),
                s.getStatus().name(),
                s.getPublishedAt()
        );
    }

    private static List<String> splitBullets(String raw){
        if (raw==null || raw.isBlank()) return List.of();
        return Arrays.stream(raw.split("\\r?\\n|;"))
                .map(String::trim).filter(s->!s.isBlank()).toList();
    }

    private static String mediaUrl(Media m){
        return (m==null) ? null : "/api/media/" + m.getId() + "/view";
    }
}
