package uz.mazmun.mazmunweb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mazmun.mazmunweb.entity.Media;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {
    Optional<Media> findByKey(String key);
    boolean existsById(Long id);
}