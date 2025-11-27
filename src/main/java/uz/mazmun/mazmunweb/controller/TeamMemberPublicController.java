package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.mazmun.mazmunweb.dto.TeamMemberPublicDTO;
import uz.mazmun.mazmunweb.service.TeamMemberService;

import java.util.List;

// Public
@RestController
@RequiredArgsConstructor
public class TeamMemberPublicController {
    private final TeamMemberService svc;

    @GetMapping("/api/team-members")
    public List<TeamMemberPublicDTO> list(){
        return svc.listPublic();
    }
}

