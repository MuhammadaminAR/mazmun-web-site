package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.mazmun.mazmunweb.enums.Status;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrustedUserLogo {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String username;

    @ManyToOne(fetch = LAZY)
    private Media logo;

    private Boolean deleted = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status = Status.DRAFT;
}
