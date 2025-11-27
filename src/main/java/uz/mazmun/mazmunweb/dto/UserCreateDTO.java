package uz.mazmun.mazmunweb.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String confirmPassword;
    private Boolean admin;
}
