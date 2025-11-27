package uz.mazmun.mazmunweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.mazmun.mazmunweb.enums.ProjectStatus;
import uz.mazmun.mazmunweb.enums.Status;

import javax.sql.rowset.spi.SyncResolver;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, length = 255)
    private String title;

    @OneToMany(mappedBy="project", cascade=ALL, orphanRemoval=true)
    private List<ProjectImage> images= new ArrayList<>();


    @Column(nullable=false)
    private boolean deleted = false;

    @Column(nullable=false, unique=true, length=160)
    private String slug;

    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private Status status = Status.DRAFT;

    private LocalDateTime publishedAt;

    private String clientName;

    private String location;

    @Column(columnDefinition = "text", nullable = false)
    private String description;

    private String service;

    private String duration;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(columnDefinition = "text", nullable = false)
    private String approach;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @Column(columnDefinition = "text", nullable = false)
    private String conclusion;

    @Column(columnDefinition = "text", nullable = false)
    private String howWeStarted;
}
