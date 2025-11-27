package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uz.mazmun.mazmunweb.entity.User;
import uz.mazmun.mazmunweb.repo.UserRepository;
import uz.mazmun.mazmunweb.service.JwtService;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final org.springframework.security.authentication.AuthenticationManager authManager;
    private final UserRepository users;
    private final JwtService jwt;

    public record LoginDTO(String username, String password) {}

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody LoginDTO dto){
        var result = authManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        dto.username(), dto.password()
                )
        );
        String username = result.getName();
        User user = users.findByUsername(username).orElseThrow();
        String access  = jwt.generateToken(user);
        String refresh = jwt.generateRefreshToken(user);
        return Map.of("accessToken", access, "refreshToken", refresh);
    }

    @GetMapping("/me")
    public Map<String,Object> me(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String username = auth.getName();
        User user = users.findByUsername(username).orElseThrow();

        var roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "roles", roles
        );
    }
}
