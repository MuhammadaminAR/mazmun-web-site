package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.mazmun.mazmunweb.enums.FeatureAvailability;

@Entity
@Table(
        name = "pricing_plan_features",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"plan_id", "feature_id"})
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingPlanFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id")
    private PricingPlan plan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "feature_id")
    private PricingFeature feature;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private FeatureAvailability availability;

    @Column(length = 255)
    private String note;

    // ESKI: priceAmount, priceUnit MAYDONLARINI O‘CHIRAMIZ
}
