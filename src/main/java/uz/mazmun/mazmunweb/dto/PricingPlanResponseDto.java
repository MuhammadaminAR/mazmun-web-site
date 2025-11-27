package uz.mazmun.mazmunweb.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PricingPlanResponseDto {

    private Long id;
    private String name;
    private String slug;
    private String code;
    private String tagline;

    private BigDecimal priceAmount;
    private String priceUnit;

    private Integer displayOrder;
    private boolean popular;
    private boolean custom;
    private boolean active;

    private List<PlanFeatureAvailabilityDto> features;
}