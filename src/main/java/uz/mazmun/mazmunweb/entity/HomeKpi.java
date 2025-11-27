package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "home_kpi")
public class HomeKpi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)   // 'projects','satisfaction','trained','experience'
    private String code;

    @Column(nullable = false)
    private Integer value;                     // 12, 99, 350, 10

    private String suffix;                     // "+", "%", "k" ...
    private String label;                      // “mijozlar mamnun”, ...
    private String iconClass;                  // "bi bi-folder2-open"
    private Integer sortOrder = 0;
    private Boolean enabled = true;

    // agar bittasini hisoblab chiqarishni xohlasangiz:
    private Boolean computed = false;          // true bo‘lsa service hisoblaydi
}