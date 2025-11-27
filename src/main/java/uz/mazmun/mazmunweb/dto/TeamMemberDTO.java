package uz.mazmun.mazmunweb.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamMemberDTO {
    private Long id;
    private String fullName;
    private String roleTitle;
    private String status;
    private Integer orderIndex;
    private Integer photoId;     // mediaId
    private String photoUrl;     // /api/media/{id}/view
    private List<SocialDTO> socials;
}
