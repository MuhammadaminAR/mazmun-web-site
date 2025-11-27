package uz.mazmun.mazmunweb.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamMemberCreateDTO {
    private String fullName;
    private String roleTitle;
    private Integer orderIndex;        // optional
    private String status;             // "DRAFT" | "PUBLISHED"
    private Integer photoMediaId;      // Media ID
    private List<SocialDTO> socials;   // 0..4
}
