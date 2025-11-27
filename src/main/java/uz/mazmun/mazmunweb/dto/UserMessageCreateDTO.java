package uz.mazmun.mazmunweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserMessageCreateDTO {

    @NotBlank
    @Size(max = 160)
    private String fullName;

    @NotBlank
    @Size(max = 160)
    private String contact;

    @NotBlank
    private String message;
}
