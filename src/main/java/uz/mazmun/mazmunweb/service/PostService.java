package uz.mazmun.mazmunweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mazmun.mazmunweb.dto.PostCreateDTO;
import uz.mazmun.mazmunweb.dto.PostUpdateDTO;
import uz.mazmun.mazmunweb.entity.Media;
import uz.mazmun.mazmunweb.entity.Post;
import uz.mazmun.mazmunweb.entity.PostCategory;
import uz.mazmun.mazmunweb.enums.Status;
import uz.mazmun.mazmunweb.repo.MediaRepository;
import uz.mazmun.mazmunweb.repo.PostCategoryRepository;
import uz.mazmun.mazmunweb.repo.PostRepository;
import uz.mazmun.mazmunweb.service.util.SlugUtil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final MediaRepository mediaRepo;
    private final PostCategoryRepository categoryRepo;


    private String nn(String s){ return (s==null || s.isBlank()) ? null : s.trim(); }

    // faqat ruxsat qilingan field’lar
    private static final Set<String> ALLOWED_SORTS = Set.of("publishedAt","createdAt","title");

    private Sort parseSort(String sort){
        if (sort == null || sort.isBlank()) {
            return Sort.by("publishedAt").descending();
        }
        String[] p = sort.split(",", 2);
        String field = p[0].trim();
        String dir   = (p.length > 1 ? p[1].trim() : "desc");

        if (!ALLOWED_SORTS.contains(field)) {
            field = "publishedAt";
        }
        Sort s = Sort.by(field);
        return "asc".equalsIgnoreCase(dir) ? s.ascending() : s.descending();
    }

    public Page<Post> searchPublic(int page, int size, String sort, String q, String category, String tag){
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        return postRepo.searchPublished(nn(q), nn(category), nn(tag), pageable);
    }



    public Page<Post> listAll(org.springframework.data.domain.Pageable pageable){
//        return postRepo.findAll(pageable);
        return postRepo.findAllByDeletedFalse(pageable);
    }
    public Post get(Integer id){ return postRepo.findById(id).orElseThrow(); }

    @Transactional
    public Post setStatus(Integer id, Status status){
        Post p = postRepo.findById(id).orElseThrow();
        p.setStatus(status);
        if(status==Status.PUBLISHED && p.getPublishedAt()==null){
            p.setPublishedAt(java.time.LocalDateTime.now());
        }
        return postRepo.save(p);
    }


    @Transactional
    public Post create(PostCreateDTO dto, Integer authorId) {
        Post p = new Post();
        p.setTitle(dto.getTitle());
        p.setSlug(generateUniqueSlug(dto.getTitle()));
        p.setSummary(dto.getSummary());
        p.setContent(dto.getContent() == null ? "" : dto.getContent());
        p.setStatus(Status.PUBLISHED);
        p.setPublishedAt(LocalDateTime.now());

        if (dto.getCoverMediaId() != null) {
            Media m = mediaRepo.findById(dto.getCoverMediaId().longValue()).orElseThrow();
            p.setPhoto(m);
        }
        if (dto.getHashTag()!=null) p.setHashTag(dto.getHashTag());

        // ✅ Kategoriyalarni kollektsiya ichida addAll() bilan qo‘shamiz
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            var cats = categoryRepo.findAllById(dto.getCategoryIds()); // Iterable<Integer> bo‘lsin!
            p.getCategories().clear();
            p.getCategories().addAll(cats);
        }

        return postRepo.save(p); // join-table yozuvlari shu payt yaratiladi
    }


    @Transactional
    public Post update(Integer id, PostUpdateDTO dto) {
        Post p = postRepo.findById(id).orElseThrow();

        if (dto.getTitle() != null) {
            p.setTitle(dto.getTitle());
            p.setSlug(generateUniqueSlug(dto.getTitle(), id));
        }
        if (dto.getSummary() != null) p.setSummary(dto.getSummary());
        if (dto.getContent() != null) p.setContent(dto.getContent());
        if (dto.getHashTag()!=null) p.setHashTag(dto.getHashTag());


        // BUGFIX: oldin dto.getContent() tekshirilib, coverMediaId o‘qilayotgan edi
        if (dto.getCoverMediaId() != null) {
            Media m = mediaRepo.findById(dto.getCoverMediaId().longValue()).orElseThrow();
            p.setPhoto(m);
        }

        if (dto.getCategoryIds() != null) {
            var cats = new HashSet<PostCategory>(categoryRepo.findAllById(dto.getCategoryIds()));
            p.setCategories(cats);
        }

        if (dto.getStatus() != null) {
            Status st = Status.valueOf(dto.getStatus());
            p.setStatus(st);
            if (st == Status.PUBLISHED && p.getPublishedAt() == null) {
                p.setPublishedAt(LocalDateTime.now());
            }
            // DRAFT ga qaytganda publishedAt’ni saqlab qolish – xohishga ko‘ra, o‘chirib yuborsang ham bo‘ladi
        }

        // updatedAt ni entity @PreUpdate to‘ldiradi
        return postRepo.save(p);
    }

    @Transactional public void delete(Integer id) { postRepo.deleteById(id); }

    // public side
    public Page<Post> listPublished(int page, int size) {
        return postRepo.findAllByStatusAndDeletedFalseOrderByPublishedAtDesc(Status.PUBLISHED,
                PageRequest.of(page, size));
    }

    @Transactional
    public void softDelete(Integer id){
        Post p = postRepo.findById(id).orElseThrow();
        p.setDeleted(true);
        postRepo.save(p);
    }

    public Optional<Post> getBySlug(String slug) {
        return postRepo.findBySlug(slug);
    }

    /* --------- helpers --------- */
    private String generateUniqueSlug(String title){ return generateUniqueSlug(title, null); }
    private String generateUniqueSlug(String title, Integer selfId){
        String base = SlugUtil.toSlug(title);
        String slug = base;
        int i = 2;
        while (true) {
            var opt = postRepo.findBySlug(slug);
            if (opt.isEmpty() || (selfId != null && opt.get().getId().equals(selfId))) return slug;
            slug = base + "-" + i++;
        }
    }
}
