package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.*;

// TeamSocialLink.java
// TeamSocialLink.java
@Entity
@Table(name = "team_social_links")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamSocialLink {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=32)
    private String network;        // telegram | linkedin | x | ...

    @Column(nullable=false, length=48)
    private String iconClass;      // bi-telegram | bi-linkedin | ...

    @Column(nullable=false, length=400)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private TeamMember member;
}


