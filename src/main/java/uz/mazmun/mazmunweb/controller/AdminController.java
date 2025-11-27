package uz.mazmun.mazmunweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uz.mazmun.mazmunweb.dto.*;
import uz.mazmun.mazmunweb.entity.PostCategory;
import uz.mazmun.mazmunweb.entity.Role;
import uz.mazmun.mazmunweb.entity.User;
import uz.mazmun.mazmunweb.enums.ProjectImageSlot;
import uz.mazmun.mazmunweb.enums.RoleName;
import uz.mazmun.mazmunweb.repo.*;
import uz.mazmun.mazmunweb.service.*;

import java.security.SecureRandom;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@EnableMethodSecurity
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PostService postService;
    private final ProjectRepository projectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final MediaService mediaService;
    private final ProjectService projectService;
    private final PostCategoryRepository postCategoryRepository;
    private final CategoryService categoryService;
    private TrustedUserLogoService trustedUserLogoService;


    // Bu endpoint PUBLIC bo'lishi kerak: SecurityConfig'da permitAll GET /api/admin/invite/accept



    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/users/promote")
    public Map<String, Object> promote(@Valid @RequestBody PromoteDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(dto.getEmail());
                    return userRepository.save(u);
                });

        Role adminRole = Optional.ofNullable(roleRepository.findByRoleName(RoleName.ROLE_ADMIN))
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName(RoleName.ROLE_ADMIN);
                    return roleRepository.save(r);
                });

        if (user.getRoles() == null) user.setRoles(new ArrayList<>());
        boolean hasAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getRoleName() == RoleName.ROLE_ADMIN);
        if (!hasAdmin) {
            user.getRoles().add(adminRole);
            userRepository.save(user);
        }
        return Map.of("status", "promoted", "email", user.getEmail());
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/users/demote")
    public Map<String, Object> demote(@Valid @RequestBody PromoteDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRoles() != null) {
            user.getRoles().removeIf(r -> r.getRoleName() == RoleName.ROLE_ADMIN);
            userRepository.save(user);
        }
        return Map.of("status", "demoted", "email", user.getEmail());
    }

    private String generateToken() {
        byte[] b = new byte[16]; // 128-bit
        new SecureRandom().nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/posts")
    public Map<String,Object> createPost(@RequestBody PostCreateDTO dto,
                                         @AuthenticationPrincipal User admin){
        var p = postService.create(dto, admin.getId());
        return Map.of("id", p.getId(), "slug", p.getSlug());
    }

//    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
//    @PutMapping("/posts/{id}")
//    public Map<String,Object> updatePost(@PathVariable Integer id, @RequestBody PostUpdateDTO dto){
//        var p = postService.update(id, dto);
//        return Map.of("id", p.getId(), "slug", p.getSlug(), "status", p.getStatus());
//    }
//
//    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
//    @DeleteMapping("/posts/{id}")
//    public void deletePost(@PathVariable Integer id){ postService.delete(id); }

    // Projects
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/admin-projects")
    public Page<AdminProjectRowDTO> adminProjects(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return projectRepository.findAll(PageRequest.of(page, size))
                .map(pr -> {
                    // COVER ni topib, presigned public URL yasaymiz
                    String coverUrl = projectImageRepository
                            .findFirstByProjectIdAndSlotOrderBySortOrderAsc(pr.getId(), ProjectImageSlot.COVER)
                            .map(pi -> mediaService.presignGetUrl(pi.getMedia().getId()))
                            .orElse(null);

                    return new AdminProjectRowDTO(
                            pr.getId(),
                            pr.getSlug(),
                            pr.getClientName(),
                            pr.getTitle(),
                            pr.getStatus() == null ? null : pr.getStatus().name(),
                            pr.getPublishedAt() == null ? null : pr.getPublishedAt().toString(),
                            coverUrl
                    );
                });
    }


    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/projects")
    public Map<String,Object> createProject(@RequestBody ProjectCreateDTO dto){
        var p = projectService.create(dto);
        return Map.of("id", p.getId(), "slug", p.getSlug());
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PutMapping("/projects/{id}")
    public Map<String,Object> updateProject(@PathVariable Integer id, @RequestBody ProjectUpdateDTO dto){
        var p = projectService.update(id, dto);
        return Map.of("id", p.getId(), "slug", p.getSlug(), "status", p.getStatus());
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/projects/{id}")
    public void deleteProject(@PathVariable Integer id){ projectService.delete(id); }

    @PostMapping ("/categories")
    public PostCategory create(@RequestBody PostCategoryDTO dto){
      return   categoryService.create(dto);
    }
     @PutMapping("/categories/{id}")
    public PostCategory update(@PathVariable Integer id, @RequestBody PostCategory dto){
        var c = postCategoryRepository.findById(id).orElseThrow();
        c.setName(dto.getName());
        return postCategoryRepository.save(c);
    }

    @DeleteMapping("/categories/{id}")
    public void delete(@PathVariable Integer id)
    {
        PostCategory postCategory = postCategoryRepository.findById(id).orElseThrow();
        postCategory.setDeleted(true);
        postCategoryRepository.save(postCategory);
    }
}
