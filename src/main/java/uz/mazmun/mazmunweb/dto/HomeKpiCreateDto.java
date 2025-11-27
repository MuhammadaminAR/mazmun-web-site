package uz.mazmun.mazmunweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeKpiCreateDto {
    private String code;
    private Integer value;
    private String suffix;
    private String label;
    private String iconClass;
    private Integer sortOrder;
    private Boolean enabled;
    private Boolean computed;
}
