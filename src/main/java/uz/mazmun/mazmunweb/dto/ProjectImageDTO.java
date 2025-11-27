package uz.mazmun.mazmunweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import uz.mazmun.mazmunweb.enums.ProjectImageSlot;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectImageDTO {
    private Long id;
    private String url;
    private ProjectImageSlot slot;
    private Integer sortOrder;
    private String altText;
    private String caption;
}
