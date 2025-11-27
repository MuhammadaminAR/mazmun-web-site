package uz.mazmun.mazmunweb.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.mazmun.mazmunweb.entity.Project;
import uz.mazmun.mazmunweb.entity.ProjectImage;
import uz.mazmun.mazmunweb.enums.ProjectImageSlot;
import uz.mazmun.mazmunweb.enums.Status;

import java.util.List;
import java.util.Optional;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
    List<ProjectImage> findAllByProjectIdOrderBySortOrderAsc(Integer projectId);

    // cover rasmini eng tepadagisini olish uchun
    Optional<ProjectImage> findFirstByProjectIdAndSlotOrderBySortOrderAsc(
            Integer projectId, ProjectImageSlot slot
    );

}