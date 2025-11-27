package uz.mazmun.mazmunweb.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PricingFeatureDto {
    private Long id;
    private String code;
    private String title;
    private String categoryName;
    private Integer displayOrder;

    // Konstruktor uchun bazaviy narx
    private BigDecimal basePriceAmount; // masalan 20, 30
    private String basePriceUnit;
}
