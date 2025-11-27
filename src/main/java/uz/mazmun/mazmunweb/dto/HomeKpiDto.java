package uz.mazmun.mazmunweb.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeKpiDto {
    private Long id;
    private String code;
    private Integer value;
    private String suffix;
    private String label;
    private String iconClass;
    private Integer sortOrder;
    private Boolean enabled;
    private Boolean computed;
}
