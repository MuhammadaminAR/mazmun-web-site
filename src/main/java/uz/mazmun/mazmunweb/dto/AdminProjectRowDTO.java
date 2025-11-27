package uz.mazmun.mazmunweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AdminProjectRowDTO {
    private Integer id;
    private String slug;
    private String client;
    private String title;
    private String status;        // DRAFT/PUBLISHED
    private String publishedAt;   // ISO string yoki null
    private String coverUrl;      // "/api/media/{id}/view"
}