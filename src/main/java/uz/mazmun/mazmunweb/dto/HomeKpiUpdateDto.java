package uz.mazmun.mazmunweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeKpiUpdateDto {
    Integer value;
    String suffix;
    String label;
    String iconClass;
    Integer sortOrder;
    Boolean enabled;
    Boolean computed;
}
