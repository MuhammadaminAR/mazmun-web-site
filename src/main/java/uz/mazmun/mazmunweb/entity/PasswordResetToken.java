package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** DB’da faqat token hash saqlanadi (plain token emailga ketadi) */
    @Column(nullable = false, length = 64)
    private String tokenHash; // Base64Url(SHA-256(token))

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    private LocalDateTime usedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
