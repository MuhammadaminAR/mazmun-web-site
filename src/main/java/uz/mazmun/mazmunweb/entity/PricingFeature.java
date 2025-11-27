package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pricing_features")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kod: TEACH_CONCEPT, LICENSE_DOCS, ERP_SETUP va hokazo
    @Column(nullable = false, unique = true, length = 64)
    private String code;

    // Jadvalda ko‘rinadigan nom
    @Column(nullable = false, length = 255)
    private String title;

    // Guruh: "TA’LIM jarayoni", "Hujjatlar va tashkiliy tuzilma", ...
    @Column(nullable = false, length = 128)
    private String categoryName;

    // Jadval ichida tartib
    private Integer displayOrder;

    // === YANGI: konstruktor uchun bazaviy narx ===
    @Column(precision = 12, scale = 2)
    private BigDecimal basePriceAmount;    // ixtiyoriy, null bo‘lishi mumkin

    @Column(length = 64)
    private String basePriceUnit;          // "USD", "so‘m / loyiha", ...
}
