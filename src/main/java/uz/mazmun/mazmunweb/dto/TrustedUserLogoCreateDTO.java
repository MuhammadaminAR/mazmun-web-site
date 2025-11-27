package uz.mazmun.mazmunweb.dto;

import lombok.Data;

@Data
public class TrustedUserLogoCreateDTO {
    private Integer coverMediaId;
    private String username;
    private String status;
}
