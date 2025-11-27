package uz.mazmun.mazmunweb.dto;

import lombok.Data;

@Data
public class TrustedUserLogoDTO {
    private Integer id;
    private String username;
    private String status;   // "DRAFT" | "PUBLISHED"
    private Integer logoId;  // mediaId
    private String logoUrl;
}
