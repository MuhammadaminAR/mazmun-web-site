package uz.mazmun.mazmunweb.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PricingPlanUpsertDto {

    private String name;
    private String slug;
    private String code;
    private String tagline;

    private BigDecimal priceAmount;
    private String priceUnit;

    private Integer displayOrder;
    private Boolean popular;
    private Boolean custom;
    private Boolean active;

    private List<PlanFeatureAvailabilityDto> features;
}
