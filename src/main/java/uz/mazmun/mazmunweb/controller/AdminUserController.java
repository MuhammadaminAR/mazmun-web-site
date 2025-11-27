package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uz.mazmun.mazmunweb.dto.UpdatePasswordDTO;
import uz.mazmun.mazmunweb.dto.UserCreateDTO;
import uz.mazmun.mazmunweb.dto.UserSummaryDTO;
import uz.mazmun.mazmunweb.entity.Role;
import uz.mazmun.mazmunweb.entity.User;
import uz.mazmun.mazmunweb.enums.RoleName;
import uz.mazmun.mazmunweb.repo.RoleRepository;
import uz.mazmun.mazmunweb.repo.UserRepository;

import java.util.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@EnableMethodSecurity
public class AdminUserController {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder pe;


    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public Map<String,Object> create(@RequestBody UserCreateDTO dto){
        if (users.existsByUsername(dto.getUsername()))
            throw new IllegalArgumentException("Username already exists");
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setPassword(pe.encode(dto.getPassword()));
        u.setFullName(dto.getFullName());
        u.setEmail(dto.getEmail());
//        u.setEnabled(true);

        Role adminRole = roles.findByRoleName(RoleName.ROLE_ADMIN);
        System.out.println("role: "+adminRole.toString());
        if (Boolean.TRUE.equals(dto.getAdmin())) {
            u.setRoles(List.of(adminRole));
        }
        users.save(u);
        return Map.of("id", u.getId(), "username", u.getUsername(), "roles", u.getRoles().stream().map(Role::getAuthority).toList());
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/{id}/grant-admin")
    public void grantAdmin(@PathVariable Integer id){
        User u = users.findById(id).orElseThrow();
        Role adminRole = roles.findByRoleName(RoleName.ROLE_ADMIN);
        if (u.getRoles().stream().noneMatch(r -> r.getRoleName() == RoleName.ROLE_ADMIN)){
            u.getRoles().add(adminRole);
            users.save(u);
        }
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/{id}/revoke-admin")
    public void revokeAdmin(@PathVariable Integer id){
        User u = users.findById(id).orElseThrow();
        u.getRoles().removeIf(r -> r.getRoleName() == RoleName.ROLE_ADMIN);
        users.save(u);
    }


    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/{id}/password")
    public void resetPasswordBySuperAdmin(@PathVariable Integer id,
                                          @RequestBody UpdatePasswordDTO dto) {
        User u = users.findById(id).orElseThrow();
        if (dto.getPassword() == null || dto.getPassword().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password too short");
        }
        u.setPassword(pe.encode(dto.getPassword()));
        users.save(u);
    }


    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/stats")
    public java.util.Map<String, Object> stats(){
        long total = users.countAllUsers();
        long admins = users.countAdmins();
        return java.util.Map.of(
                "totalUsers", total,
                "adminUsers", admins
                // "passwordResetsToday", 0 // agar audit bo'lmasa, hozircha qoldirib turamiz
        );
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public Page<User> list(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String q) {
        Pageable p = PageRequest.of(page, size, Sort.by("id").descending());
        if (q == null || q.isBlank()) return users.findAll(p);
        return users.search(q.toLowerCase(), p); // UserRepository#search bor
    }


}
