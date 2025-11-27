package uz.mazmun.mazmunweb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mazmun.mazmunweb.entity.PricingFeature;

import java.util.List;
import java.util.Optional;

public interface PricingFeatureRepository extends JpaRepository<PricingFeature, Long> {
  Optional<PricingFeature> findByCode(String code);

  List<PricingFeature> findAllByOrderByCategoryNameAscDisplayOrderAsc();
}