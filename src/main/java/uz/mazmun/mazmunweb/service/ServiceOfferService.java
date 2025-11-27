package uz.mazmun.mazmunweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mazmun.mazmunweb.entity.Media;
import uz.mazmun.mazmunweb.entity.Post;
import uz.mazmun.mazmunweb.entity.ServiceOffer;
import uz.mazmun.mazmunweb.enums.Status;
import uz.mazmun.mazmunweb.repo.MediaRepository;
import uz.mazmun.mazmunweb.repo.ServiceOfferRepository;
import uz.mazmun.mazmunweb.service.util.SlugUtil;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ServiceOfferService {
    private final ServiceOfferRepository repo;
    private final MediaRepository mediaRepo;

    @Transactional
    public ServiceOffer create(ServiceOffer s, Long heroId, Long leftId, Long rightId){
        s.setSlug(SlugUtil.toSlug(s.getTitle()));
        s.setStatus(Status.DRAFT);
        s.setCreatedAt(LocalDateTime.now());
        s.setUpdatedAt(LocalDateTime.now());

        if(heroId!=null)  s.setHeroPhoto(mediaRepo.findById(heroId).orElseThrow());
        if(leftId!=null)  s.setImageLeft(mediaRepo.findById(leftId).orElseThrow());
        if(rightId!=null) s.setImageRight(mediaRepo.findById(rightId).orElseThrow());

        return repo.save(s);
    }

    @Transactional
    public void softDelete(Integer id){
        ServiceOffer p = repo.findById(id).orElseThrow();
        p.setDeleted(true);
        repo.save(p);
    }

    @Transactional
    public ServiceOffer update(Integer id, ServiceOffer patch, Long heroId, Long leftId, Long rightId){
        ServiceOffer s = repo.findById(id).orElseThrow();
        if(patch.getTitle()!=null){ s.setTitle(patch.getTitle()); s.setSlug(SlugUtil.toSlug(patch.getTitle())); }
        if(patch.getSubtitle()!=null)  s.setSubtitle(patch.getSubtitle());
        if(patch.getOverview()!=null)  s.setOverview(patch.getOverview());
        if(patch.getBullets()!=null)   s.setBullets(patch.getBullets());
        if(patch.getQuoteText()!=null) s.setQuoteText(patch.getQuoteText());
        if(patch.getQuoteAuthor()!=null) s.setQuoteAuthor(patch.getQuoteAuthor());
        if(patch.getQuoteRole()!=null)   s.setQuoteRole(patch.getQuoteRole());
        if(patch.getStatus()!=null) {
            s.setStatus(patch.getStatus());
            if (patch.getStatus()==Status.PUBLISHED && s.getPublishedAt()==null) {
                s.setPublishedAt(LocalDateTime.now());
            }
        }
        if(heroId!=null)  s.setHeroPhoto(heroId==0? null : mediaRepo.findById(heroId).orElseThrow());
        if(leftId!=null)  s.setImageLeft(leftId==0? null : mediaRepo.findById(leftId).orElseThrow());
        if(rightId!=null) s.setImageRight(rightId==0? null : mediaRepo.findById(rightId).orElseThrow());

        s.setUpdatedAt(LocalDateTime.now());
        return repo.save(s);
    }

    public Page<ServiceOffer> listPublished(int page, int size){
        return repo.findAllByStatusOrderByPublishedAtDesc(Status.PUBLISHED, PageRequest.of(page, size));
    }

    public ServiceOffer getBySlug(String slug){ return repo.findBySlug(slug).orElseThrow(); }
}
