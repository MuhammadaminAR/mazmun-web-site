// web/admin/ServiceOfferAdminController.java
package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mazmun.mazmunweb.dto.ServiceOfferDTO;
import uz.mazmun.mazmunweb.entity.Media;
import uz.mazmun.mazmunweb.entity.ServiceOffer;
import uz.mazmun.mazmunweb.enums.Status;
import uz.mazmun.mazmunweb.repo.MediaRepository;
import uz.mazmun.mazmunweb.repo.ServiceOfferRepository;
import uz.mazmun.mazmunweb.service.ServiceOfferService;
import uz.mazmun.mazmunweb.service.util.SlugUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/admin/services")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
public class ServiceOfferAdminController {

    private final ServiceOfferRepository repo;
    private final MediaRepository mediaRepo; // <— ID turini Loyihangga moslashtir
    private final ServiceOfferService serviceOfferService;


    // LIST (admin — hamma statuslar yoki filter bilan)
    @GetMapping
    public Page<ServiceOfferDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestParam(required = false) String status // DRAFT | PUBLISHED | null
    ){
        Sort s = Sort.by(sort.split(",")[0]);
        s = sort.toLowerCase().endsWith(",asc") ? s.ascending() : s.descending();
        PageRequest pr = PageRequest.of(page, size, s);

        Page<ServiceOffer> p;
        if (status != null && !status.isBlank()) {
            Status st = Status.valueOf(status.toUpperCase());
            p = repo.findAllByStatus(st, pr);
        } else {
            p = repo.findAll(pr); // JpaRepository default
        }
        return p.map(this::toDto);
    }

    // ONE (admin)
    @GetMapping("/{id}")
    public ServiceOfferDTO one(@PathVariable Integer id){
        return toDto(repo.findById(id).orElseThrow());
    }


    // CREATE
    @PostMapping
    public ServiceOfferDTO create(@RequestBody ServiceUpsert req){
        ServiceOffer s = new ServiceOffer();
        s.setTitle(req.title());
        s.setSlug(SlugUtil.toSlug(req.slug()!=null && !req.slug().isBlank() ? req.slug() : req.title()));
        s.setSubtitle(req.subtitle());
        s.setOverview(req.overview());
        s.setBullets(req.bullets());
        s.setQuoteText(req.quoteText());
        s.setQuoteAuthor(req.quoteAuthor());
        s.setQuoteRole(req.quoteRole());

        Status st = Status.DRAFT;
        if (req.status()!=null) {
            try { st = Status.valueOf(req.status()); } catch (Exception ignored) {}
        }
        s.setStatus(st);
        if (st == Status.PUBLISHED) {
            s.setPublishedAt(LocalDateTime.now());
        }

        s.setCreatedAt(LocalDateTime.now());
        s.setUpdatedAt(LocalDateTime.now());

        if(req.heroPhotoId()!=null)  s.setHeroPhoto(findMedia(req.heroPhotoId()));
        if(req.imageLeftId()!=null)  s.setImageLeft(findMedia(req.imageLeftId()));
        if(req.imageRightId()!=null) s.setImageRight(findMedia(req.imageRightId()));
        if (req.quoteAuthorPhotoId()!=null) s.setQuoteAuthorPhoto(findMedia(req.quoteAuthorPhotoId()));
        if (req.iconId()!=null) s.setIcon(findMedia(req.iconId()));

        return toDto(repo.save(s));
    }

    // UPDATE (partial)
    @PatchMapping("/{id}")
    public ServiceOfferDTO update(@PathVariable Integer id, @RequestBody ServiceUpsert req){
        ServiceOffer s = repo.findById(id).orElseThrow();

        if(req.title()!=null){ s.setTitle(req.title()); }
        if(req.slug()!=null){ s.setSlug(SlugUtil.toSlug(req.slug())); }
        else if(req.title()!=null){ s.setSlug(SlugUtil.toSlug(req.title())); }

        if(req.subtitle()!=null)  s.setSubtitle(req.subtitle());
        if(req.overview()!=null)  s.setOverview(req.overview());
        if(req.bullets()!=null)   s.setBullets(req.bullets());
        if(req.quoteText()!=null) s.setQuoteText(req.quoteText());
        if(req.quoteAuthor()!=null) s.setQuoteAuthor(req.quoteAuthor());
        if(req.quoteRole()!=null)   s.setQuoteRole(req.quoteRole());

        if(req.heroPhotoId()!=null)  s.setHeroPhoto(req.heroPhotoId()==0 ? null : findMedia(req.heroPhotoId()));
        if(req.imageLeftId()!=null)  s.setImageLeft(req.imageLeftId()==0 ? null : findMedia(req.imageLeftId()));
        if(req.imageRightId()!=null) s.setImageRight(req.imageRightId()==0 ? null : findMedia(req.imageRightId()));
        if(req.quoteAuthorPhotoId()!=null) s.setImageRight(req.quoteAuthorPhotoId()==0 ? null : findMedia(req.quoteAuthorPhotoId()));
        if(req.iconId()!=null) s.setImageRight(req.iconId()==0 ? null : findMedia(req.iconId()));

        if(req.status()!=null){
            s.setStatus(Status.valueOf(req.status()));
            if (s.getStatus()==Status.PUBLISHED && s.getPublishedAt()==null)
                s.setPublishedAt(LocalDateTime.now());
        }

        s.setUpdatedAt(LocalDateTime.now());
        return toDto(repo.save(s));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        serviceOfferService.softDelete(id);
        return ResponseEntity.noContent().build();
    }


    private ServiceOfferDTO toDto(ServiceOffer s){
        return new ServiceOfferDTO(
                s.getId(), s.getSlug(), s.getTitle(), s.getSubtitle(),
                s.getOverview(), splitBullets(s.getBullets()),
                s.getQuoteText(), s.getQuoteAuthor(), s.getQuoteRole(),
                mediaUrl(s.getHeroPhoto()),mediaUrl(s.getQuoteAuthorPhoto()), mediaUrl(s.getImageLeft()), mediaUrl(s.getImageRight()),
                mediaUrl(s.getIcon()),s.getStatus().name(), s.getPublishedAt()
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

    private Media findMedia(Long id){ // <-- ID turini moslashtir (Long/Integer)
        return mediaRepo.findById(id).orElseThrow();
    }

    public record ServiceUpsert(
            String title,
            String slug,
            String subtitle,
            String overview,
            String bullets,        // har qatorda bitta bullet
            String quoteText,
            String quoteAuthor,
            String quoteRole,
            Long quoteAuthorPhotoId,
            Long heroPhotoId,      // kerak bo‘lsa Integer qil
            Long imageLeftId,
            Long imageRightId,
            Long iconId,
            String status          // DRAFT/PUBLISHED
    ) {}
}
