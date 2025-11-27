package uz.mazmun.mazmunweb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mazmun.mazmunweb.entity.PostCategory;

import java.util.Optional;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Integer> {
  Optional<PostCategory> findBySlug(String slug);
  boolean existsBySlug(String slug);
}