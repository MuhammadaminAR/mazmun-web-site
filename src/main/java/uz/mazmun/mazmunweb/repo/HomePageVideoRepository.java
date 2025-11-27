package uz.mazmun.mazmunweb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mazmun.mazmunweb.entity.HomePageVideo;

import java.util.Optional;

public interface HomePageVideoRepository extends JpaRepository<HomePageVideo, Long> {
    Optional<HomePageVideo> findTopByOrderByIdAsc();
}