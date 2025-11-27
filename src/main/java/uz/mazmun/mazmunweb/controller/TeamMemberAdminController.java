package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mazmun.mazmunweb.dto.TeamMemberCreateDTO;
import uz.mazmun.mazmunweb.dto.TeamMemberDTO;
import uz.mazmun.mazmunweb.service.TeamMemberService;

// Admin
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class TeamMemberAdminController {
    private final TeamMemberService svc;

    @PostMapping("/team-member")
    public ResponseEntity<TeamMemberDTO> create(@RequestBody TeamMemberCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(svc.create(dto));
    }

    @GetMapping("/team-members")
    public Page<TeamMemberDTO> list(@PageableDefault(size=10, sort="orderIndex") Pageable p){
        return svc.list(p);
    }

    @GetMapping("/team-member/{id}")
    public TeamMemberDTO one(@PathVariable Long id){ return svc.one(id); }

    @PatchMapping("/team-member/{id}")
    public TeamMemberDTO patch(@PathVariable Long id, @RequestBody TeamMemberCreateDTO dto){
        return svc.patch(id, dto);
    }

    @DeleteMapping("/team-member/{id}")
    public void delete(@PathVariable Long id){ svc.delete(id); }
}
