package uz.mazmun.mazmunweb.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResDTO {
    private Integer id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private List<Integer> categoryIds;

    private Long coverMediaId;  // photo.id
    private String coverUrl;    // presigned GET URL
    private String hashTag;
}
