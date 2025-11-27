package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.mazmun.mazmunweb.dto.PlanFeatureAvailabilityDto;
import uz.mazmun.mazmunweb.dto.PricingFeatureDto;
import uz.mazmun.mazmunweb.dto.PricingMatrixResponse;
import uz.mazmun.mazmunweb.dto.PricingPlanResponseDto;
import uz.mazmun.mazmunweb.repo.PricingFeatureRepository;
import uz.mazmun.mazmunweb.repo.PricingPlanFeatureRepository;
import uz.mazmun.mazmunweb.service.PricingPlanService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class PricingPublicController {

    private final PricingPlanService planService;
    private final PricingFeatureRepository featureRepository;
    private final PricingPlanFeatureRepository mappingRepository;

    @GetMapping("/matrix")
    public ResponseEntity<PricingMatrixResponse> getMatrix() {
        var plans = planService.getActivePlans();

        // Feature + bazaviy narx
        var features = featureRepository.findAllByOrderByCategoryNameAscDisplayOrderAsc()
                .stream().map(f -> {
                    PricingFeatureDto dto = new PricingFeatureDto();
                    dto.setId(f.getId());
                    dto.setCode(f.getCode());
                    dto.setTitle(f.getTitle());
                    dto.setCategoryName(f.getCategoryName());
                    dto.setDisplayOrder(f.getDisplayOrder());
                    dto.setBasePriceAmount(f.getBasePriceAmount());
                    dto.setBasePriceUnit(f.getBasePriceUnit());
                    return dto;
                }).toList();

        var planIds = plans.stream().map(PricingPlanResponseDto::getId).toList();
        var mappings = mappingRepository.findByPlanIdIn(planIds);

        Map<Long, Map<Long, PlanFeatureAvailabilityDto>> matrix = new HashMap<>();

        mappings.forEach(m -> {
            matrix.computeIfAbsent(m.getPlan().getId(), k -> new HashMap<>());
            PlanFeatureAvailabilityDto dto = new PlanFeatureAvailabilityDto();
            dto.setFeatureId(m.getFeature().getId());
            dto.setAvailability(m.getAvailability());
            dto.setNote(m.getNote());
            matrix.get(m.getPlan().getId()).put(m.getFeature().getId(), dto);
        });

        PricingMatrixResponse resp = new PricingMatrixResponse();
        resp.setPlans(plans);
        resp.setFeatures(features);
        resp.setMatrix(matrix);

        return ResponseEntity.ok(resp);
    }
}
