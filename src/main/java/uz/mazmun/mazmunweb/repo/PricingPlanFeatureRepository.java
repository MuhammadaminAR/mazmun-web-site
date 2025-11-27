package uz.mazmun.mazmunweb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.mazmun.mazmunweb.entity.PricingPlanFeature;

import java.util.List;

public interface PricingPlanFeatureRepository extends JpaRepository<PricingPlanFeature, Long> {
  List<PricingPlanFeature> findByPlanIdIn(Iterable<Long> planIds);


  List<PricingPlanFeature> findByPlanId(Long planId);
  @Modifying
  @Transactional
  @Query("delete from PricingPlanFeature m where m.plan.id = :planId")
  void deleteByPlanId(@Param("planId") Long planId);

}