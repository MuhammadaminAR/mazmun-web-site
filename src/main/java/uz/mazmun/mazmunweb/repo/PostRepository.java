package uz.mazmun.mazmunweb.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mazmun.mazmunweb.entity.Post;
import uz.mazmun.mazmunweb.entity.Project;
import uz.mazmun.mazmunweb.enums.ProjectStatus;
import uz.mazmun.mazmunweb.enums.Status;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findBySlug(String slug);
    Page<Post> findAllByStatusAndDeletedFalseOrderByPublishedAtDesc(Status status, Pageable pageable);

    @Query("""
    select distinct p
    from Post p
    left join p.categories c
    where p.deleted = false
      and p.status = uz.mazmun.mazmunweb.enums.Status.PUBLISHED
      and (
            :q is null or :q = ''
         or lower(p.title) like lower(concat('%', :q, '%'))
         or lower(coalesce(p.summary, '')) like lower(concat('%', :q, '%'))
         or lower(p.content) like lower(concat('%', :q, '%'))
      )
      and (
            :category is null or :category = ''
         or c.slug = :category
      )
      and (
            :tag is null or :tag = ''
         or lower(coalesce(p.hashTag, '')) like lower(concat('%', :tag, '%'))
      )
    """)
    Page<Post> searchPublished(@Param("q") String q,
                               @Param("category") String category,
                               @Param("tag") String tag,
                               Pageable pageable);




}