package uz.mazmun.mazmunweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mazmun.mazmunweb.dto.ProjectCreateDTO;
import uz.mazmun.mazmunweb.dto.ProjectUpdateDTO;
import uz.mazmun.mazmunweb.entity.Project;
import uz.mazmun.mazmunweb.entity.ProjectImage;
import uz.mazmun.mazmunweb.enums.Status;
import uz.mazmun.mazmunweb.repo.MediaRepository;
import uz.mazmun.mazmunweb.repo.ProjectImageRepository;
import uz.mazmun.mazmunweb.repo.ProjectRepository;
import uz.mazmun.mazmunweb.service.util.SlugUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepo;
    private final ProjectImageRepository imgRepo;
    private final MediaRepository mediaRepo;


    private LocalDateTime parseDateOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        return LocalDate.parse(s).atStartOfDay();
    }

    private String ensureUniqueSlug(String base) {
        String s = SlugUtil.toSlug(base);
        String orig = s; int i = 2;
        while (projectRepo.findBySlug(s).isPresent()) {
            s = orig + "-" + i++;
        }
        return s;
    }



    @Transactional
    public Project create(ProjectCreateDTO dto) {
        Project p = new Project();
        p.setTitle(dto.getTitle());
        p.setSlug(ensureUniqueSlug(dto.getTitle()));
        p.setDescription(dto.getOverview());
        p.setConclusion(dto.getConclusion());
        p.setHowWeStarted(dto.getHowWeStarted());
        // yangi maydonlar
        p.setClientName(dto.getClientName());
        p.setLocation(dto.getLocation());
        p.setService(dto.getService());
        p.setDuration(dto.getDuration());
        p.setApproach(dto.getApproach());
        p.setProjectStatus(dto.getProjectStatus());

        p.setStartDate(parseDateOrNull(dto.getStartDate()));
        p.setEndDate(parseDateOrNull(dto.getEndDate()));
        if (p.getEndDate() == null && p.getStartDate() != null) {
            p.setEndDate(p.getStartDate());
        }

        // status/publishedAt
        p.setStatus(Status.DRAFT);
        if (dto.getStatus() != null) {
            Status st = Status.valueOf(dto.getStatus());
            p.setStatus(st);
            if (st == Status.PUBLISHED) {
                p.setPublishedAt(LocalDateTime.now());
            }
        }

        projectRepo.save(p);

        if (dto.getImages()!=null) {
            for (var img : dto.getImages()) {
                ProjectImage pi = new ProjectImage();
                pi.setProject(p);
                pi.setMedia(mediaRepo.findById(img.getMediaId()).orElseThrow());
                pi.setSlot(img.getSlot());
                pi.setSortOrder(img.getSortOrder()==null?0:img.getSortOrder());
                pi.setAltText(img.getAltText());
                pi.setCaption(img.getCaption());
                imgRepo.save(pi);
            }
        }
        return p;
    }


    @Transactional
    public Project update(Integer id, ProjectUpdateDTO dto) {
        Project p = projectRepo.findById(id).orElseThrow();

        if (dto.getTitle()!=null) {
            p.setTitle(dto.getTitle());
            p.setSlug(ensureUniqueSlug(dto.getTitle()));
        }
        if (dto.getConclusion()!=null) p.setConclusion(dto.getConclusion());
        if (dto.getHowWeStarted()!=null) p.setHowWeStarted(dto.getHowWeStarted());
        if (dto.getOverview()!=null) p.setDescription(dto.getOverview());
        if (dto.getClientName()!=null) p.setClientName(dto.getClientName());
        if (dto.getLocation()!=null)   p.setLocation(dto.getLocation());
        if (dto.getService()!=null)    p.setService(dto.getService());
        if (dto.getDuration()!=null)   p.setDuration(dto.getDuration());
        if (dto.getApproach()!=null)   p.setApproach(dto.getApproach());
        if (dto.getProjectStatus()!=null) p.setProjectStatus(dto.getProjectStatus());

        if (dto.getStartDate()!=null) p.setStartDate(parseDateOrNull(dto.getStartDate()));
        if (dto.getEndDate()!=null)   p.setEndDate(parseDateOrNull(dto.getEndDate()));

        if (dto.getStatus()!=null) {
            Status st = Status.valueOf(dto.getStatus());
            p.setStatus(st);
            if (st==Status.PUBLISHED && p.getPublishedAt()==null) {
                p.setPublishedAt(LocalDateTime.now());
            }
        }

        if (dto.getImages()!=null) {
            p.getImages().clear();
            imgRepo.deleteAll(imgRepo.findAllByProjectIdOrderBySortOrderAsc(id));
            for (var img : dto.getImages()) {
                ProjectImage pi = new ProjectImage();
                pi.setProject(p);
                pi.setMedia(mediaRepo.findById(img.getMediaId()).orElseThrow());
                pi.setSlot(img.getSlot());
                pi.setSortOrder(img.getSortOrder()==null?0:img.getSortOrder());
                pi.setAltText(img.getAltText());
                pi.setCaption(img.getCaption());
                imgRepo.save(pi);
            }
        }
        return projectRepo.save(p);
    }


    private LocalDateTime parseDateTimeOrMidnight(String s, boolean isStart) {
        if (s == null || s.isBlank()) {
            // *** Variant A: default qo'yish
            return isStart ? LocalDateTime.now() : LocalDateTime.now().plusDays(1);
            // *** Variant B (majburiy bo‘lsa): throw new IllegalArgumentException("start/end date required");
        }
        try {
            if (s.length() == 10) { // "YYYY-MM-DD"
                return LocalDate.parse(s).atStartOfDay();
            }
            return LocalDateTime.parse(s);
        } catch (Exception e) {
            // format xato bo‘lsa ham default qo‘yish yoki throw
            return isStart ? LocalDateTime.now() : LocalDateTime.now().plusDays(1);
        }
    }



    @Transactional public void delete(Integer id){
        Project project = projectRepo.findById(id).orElseThrow();
        project.setDeleted(true);
        projectRepo.save(project);
    }

    // public side
    public Page<Project> listPublished(int page, int size){
        return projectRepo.findAllByStatusOrderByPublishedAtDesc(Status.PUBLISHED, PageRequest.of(page,size));
    }
    public Optional<Project> getBySlug(String slug) {
        return projectRepo.findBySlug(slug); // Optional<Project>
    }
}

