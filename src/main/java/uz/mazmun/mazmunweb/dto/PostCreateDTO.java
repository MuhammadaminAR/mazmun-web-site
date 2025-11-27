package uz.mazmun.mazmunweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDTO {
    private String title;
    private String summary;           // ixtiyoriy
    private String content;
    private Integer coverMediaId;     // /api/media/commit dan keladi
    private List<Integer> categoryIds;
    private String hashTag;
}

