package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import uz.mazmun.mazmunweb.enums.Status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// TeamMember.java
@Entity
@Table(name = "team_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=120)
    private String fullName;

    @Column(nullable=false, length=140)
    private String roleTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    private Media photo;              // <— Media bilan bog‘lanadi

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status = Status.DRAFT;

    @Column(nullable=false)
    private Integer orderIndex = 0;

    private Boolean deleted = false;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamSocialLink> socials = new ArrayList<>();

    public void setSocials(List<TeamSocialLink> items){
        this.socials.clear();
        if(items!=null){
            items.forEach(i -> i.setMember(this));
            this.socials.addAll(items);
        }
    }
}
