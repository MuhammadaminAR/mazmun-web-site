package uz.mazmun.mazmunweb.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PricingMatrixResponse {

    // tariflar ro‘yxati
    private List<PricingPlanResponseDto> plans;

    // barcha feature’lar (gruppalash uchun)
    private List<PricingFeatureDto> features;

    // planId-featureId -> availability
    private Map<Long, Map<Long, PlanFeatureAvailabilityDto>> matrix;
}
