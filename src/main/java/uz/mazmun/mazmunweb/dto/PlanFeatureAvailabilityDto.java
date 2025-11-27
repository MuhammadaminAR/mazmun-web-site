package uz.mazmun.mazmunweb.dto;

import lombok.Data;
import uz.mazmun.mazmunweb.enums.FeatureAvailability;

import java.math.BigDecimal;

@Data
public class PlanFeatureAvailabilityDto {
    private Long featureId;
    private FeatureAvailability availability;
    private String note;        // ixtiyoriy
}
