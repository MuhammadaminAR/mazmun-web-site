package uz.mazmun.mazmunweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailDTO {
    private String slug;
    private String title;
    private String content;
    private String publishedAtIso;
    private MediaDTO cover;
    private String hashTag;
}
