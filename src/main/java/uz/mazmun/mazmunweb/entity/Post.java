package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.mazmun.mazmunweb.enums.Status;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "text", nullable = false)
    private String content;

    @Column(nullable=false)
    private boolean deleted = false;

    // ManyToMany — agar sendagi tanlov shu bo‘lsa
    @ManyToMany
    @JoinTable(
            name = "post_categories",
            joinColumns = @JoinColumn(name="post_id"),
            inverseJoinColumns = @JoinColumn(name="category_id")
    )
    private Set<PostCategory> categories = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status = Status.DRAFT;

    private LocalDateTime publishedAt;

    @Column(nullable=false, unique=true, length=160)
    private String slug;

    private String summary;
    private String hashTag;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name="photo_id")
    private Media photo;

    @Column(nullable=false)
    private LocalDateTime createdAt;

    @Column(nullable=false)
    private LocalDateTime updatedAt;

    /* ---- lifecycle ---- */
    @PrePersist
    public void prePersist() {
        if (content == null) content = "";
        if (status == null) status = Status.DRAFT;
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
