package uz.mazmun.mazmunweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrustedUserLogoResDTO {
    private Integer id;
    private String username;
    private String logoUrl; // frontga bevosita ko‘rinadigan URL
}

