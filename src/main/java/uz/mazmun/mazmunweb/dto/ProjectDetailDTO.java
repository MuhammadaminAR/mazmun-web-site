package uz.mazmun.mazmunweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetailDTO {
    private String slug;
    private String clientName;
    private String location;
    private String service;
    private String duration;
    private String title;
    private String overview;
    private String publishedAtIso;
    private List<ProjectImageDTO> images;
    private String approach;
    private String conclusion;
    private String howWeStarted;
}
