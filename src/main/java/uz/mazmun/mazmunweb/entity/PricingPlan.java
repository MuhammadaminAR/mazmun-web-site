package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pricing_plans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ADMIN panelda ko‘rinadigan nom: Bronze, Silver, ...
    @Column(nullable = false)
    private String name;

    // Frontend URL uchun: bronze, silver, customise...
    @Column(nullable = false, unique = true, length = 64)
    private String slug;

    // Kod: BRONZE, SILVER, GOLD, PLATINUM, CUSTOM
    @Column(nullable = false, unique = true, length = 32)
    private String code;

    // Qisqa tagline: "Start paketi", "Ko‘p tanlanadigan", va hokazo
    @Column(length = 255)
    private String tagline;

    // Asosiy narx (masalan, USD bo‘yicha)
    @Column(precision = 12, scale = 2)
    private BigDecimal priceAmount;

    // "bir martalik loyiha", "kompleks loyiha", "kelishuv asosida" kabi
    @Column(length = 128)
    private String priceUnit;

    // Admin panelda tartiblash uchun (1=Bronze, 2=Silver,...)
    private Integer displayOrder;

    // UI uchun: "Ko‘p tanlanadi" belgisini chiqarish
    private boolean popular;

    // Customise tarifmi yoki standartmi
    private boolean custom;

    // Faol yoki yo‘q (old tarifni o‘chirib qo‘yish uchun)
    private boolean active = true;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PricingPlanFeature> featureMappings;

    // PricingPlan.java
    @Column(precision = 12, scale = 2)
    private BigDecimal compareAtPriceAmount; // eski narx (ixtiyoriy, null bo‘lishi mumkin)


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
