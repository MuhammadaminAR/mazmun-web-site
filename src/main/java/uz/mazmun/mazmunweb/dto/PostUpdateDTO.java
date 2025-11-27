package uz.mazmun.mazmunweb.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostUpdateDTO {
    private String title;
    private String summary;
    private String content;
    private Integer coverMediaId;
    private List<Integer> categoryIds;
    private String status;            // "DRAFT" | "PUBLISHED"
    private String hashTag;
}
