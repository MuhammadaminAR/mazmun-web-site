package uz.mazmun.mazmunweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import uz.mazmun.mazmunweb.enums.ProjectImageSlot;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectImageCreateDTO {
    private Long mediaId;
    private ProjectImageSlot slot;
    private Integer sortOrder;
    private String altText;
    private String caption;
}
