package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mazmun.mazmunweb.dto.PricingFeatureDto;
import uz.mazmun.mazmunweb.dto.PricingPlanResponseDto;
import uz.mazmun.mazmunweb.dto.PricingPlanUpsertDto;
import uz.mazmun.mazmunweb.service.PricingFeatureService;
import uz.mazmun.mazmunweb.service.PricingPlanService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pricing")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
public class PricingAdminController {

    private final PricingPlanService planService;
    private final PricingFeatureService featureService;

    // ---- FEATURE CRUD (jadval satrlari) ----

    @GetMapping("/features")
    public ResponseEntity<List<PricingFeatureDto>> getAllFeatures() {
        return ResponseEntity.ok(featureService.getAll());
    }

    @PostMapping("/features")
    public ResponseEntity<PricingFeatureDto> createFeature(@RequestBody PricingFeatureDto dto) {
        return ResponseEntity.ok(featureService.create(dto));
    }

    @PutMapping("/features/{id}")
    public ResponseEntity<PricingFeatureDto> updateFeature(@PathVariable Long id,
                                                           @RequestBody PricingFeatureDto dto) {
        return ResponseEntity.ok(featureService.update(id, dto));
    }

    @DeleteMapping("/features/{id}")
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        featureService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---- PLAN CRUD (Bronze/Silver/Gold/Platinum/Customise) ----

    @GetMapping("/plans")
    public ResponseEntity<List<PricingPlanResponseDto>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllForAdmin());
    }

    @PostMapping("/plans")
    public ResponseEntity<PricingPlanResponseDto> createPlan(@RequestBody PricingPlanUpsertDto dto) {
        return ResponseEntity.ok(planService.createPlan(dto));
    }

    @PutMapping("/plans/{id}")
    public ResponseEntity<PricingPlanResponseDto> updatePlan(@PathVariable Long id,
                                                             @RequestBody PricingPlanUpsertDto dto) {
        try {
            System.out.println("name: " + dto.getName());
            System.out.println("popular: " + dto.getPopular());
            return ResponseEntity.ok(planService.updatePlan(id, dto));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/plans/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}
