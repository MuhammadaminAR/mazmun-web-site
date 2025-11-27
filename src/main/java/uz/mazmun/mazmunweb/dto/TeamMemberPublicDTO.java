package uz.mazmun.mazmunweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberPublicDTO {
    private Long id;
    private String fullName;
    private String roleTitle;
    private String photoUrl;
    private List<SocialDTO> socials;
    private Integer order;
}
