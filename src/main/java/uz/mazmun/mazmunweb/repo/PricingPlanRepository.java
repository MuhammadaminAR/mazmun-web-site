package uz.mazmun.mazmunweb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mazmun.mazmunweb.entity.PricingPlan;

import java.util.List;
import java.util.Optional;

public interface PricingPlanRepository extends JpaRepository<PricingPlan, Long> {
  Optional<PricingPlan> findBySlugAndActiveTrue(String slug);

  List<PricingPlan> findAllByActiveTrueOrderByDisplayOrderAsc();
}