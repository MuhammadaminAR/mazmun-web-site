package uz.mazmun.mazmunweb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mazmun.mazmunweb.entity.HomeKpi;

import java.util.List;
import java.util.Optional;

public interface HomeKpiRepository extends JpaRepository<HomeKpi, Long> {
    List<HomeKpi> findAllByEnabledTrueOrderBySortOrderAsc();
    List<HomeKpi> findAllByOrderBySortOrderAsc();     // listAll() uchun
    boolean existsByCode(String code);                // create() uchun
    Optional<HomeKpi> findByCode(String code);
}