package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.mazmun.mazmunweb.enums.Status;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOffer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 160)
    private String slug;                    // URL-friendly nom

    @Column(nullable = false, length = 160)
    private String title;                   // masalan: "Risk Assessment"

    @Column(nullable=false)
    private boolean deleted = false;

    @Column(length = 220)
    private String subtitle;                // qisqa tagline (ixtiyoriy)

    @Column(columnDefinition = "text")
    private String overview;

    @Column(columnDefinition = "text")
    private String bullets;

    @Column(columnDefinition = "text")
    private String quoteText;
    @Column(columnDefinition = "text")
    private String quoteAuthor;             // "Amira Johnson"
    @Column(columnDefinition = "text")
    private String quoteRole;             // "CEO"

    // Agar qachondir service sahifasida cover/hero rasmni API’dan bermoqchi bo‘lsak:
    @ManyToOne(fetch = LAZY, optional = true)
    @JoinColumn(name = "hero_photo_id")
    private Media heroPhoto;

    @ManyToOne(fetch = LAZY, optional = true)
    @JoinColumn(name = "quoteAuthorPhoto_id")
    private Media quoteAuthorPhoto;

    // Ikki yonma-yon rasm (3-rasmdagi bo‘lim uchun)
    @ManyToOne(fetch = LAZY, optional = true)
    @JoinColumn(name = "image_left_id")
    private Media imageLeft;

    @ManyToOne(fetch = LAZY, optional = true)
    @JoinColumn(name = "image_right_id")
    private Media imageRight;

    @ManyToOne(fetch = LAZY, optional = true)
    @JoinColumn(name = "icon_id")
    private Media icon;


    // Holat va vaqt maydonlari — Project/Post dagi kabi
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;                  // DRAFT | PUBLISHED

    private LocalDateTime publishedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
