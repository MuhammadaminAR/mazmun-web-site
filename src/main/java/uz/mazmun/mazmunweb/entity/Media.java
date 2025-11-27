package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name="media")
public class Media {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Diskdagi nisbiy yo‘l: posts/2025/09/uuid.jpg */
    @Column(nullable = false, unique = true, length = 512)
    private String key;

    @Column(nullable = false, length = 255)
    private String filename;

    @Column(nullable = false, length = 100)
    private String contentType;   // image/jpeg | image/png | ...

    @Column(nullable = false)
    private Long size;

    private Long ownerId;         // xohishga ko‘ra

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(); // Agar LocalDateTime bo'lsa: LocalDateTime.now()
        }
    }

}

