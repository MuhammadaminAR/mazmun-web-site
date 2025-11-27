package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.mazmun.mazmunweb.enums.ProjectImageSlot;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProjectImage {

    @Id
    @GeneratedValue(strategy=IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Project project;

    @ManyToOne(fetch=LAZY)
    private Media media;

    @Enumerated(EnumType.STRING)
    private ProjectImageSlot slot;

    private Integer sortOrder;
    private String altText;
    private String caption;
}
