package uz.mazmun.mazmunweb.controller;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.mazmun.mazmunweb.dto.ChangePasswordDTO;
import uz.mazmun.mazmunweb.entity.User;
import uz.mazmun.mazmunweb.repo.UserRepository;
import uz.mazmun.mazmunweb.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@MultipartConfig
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/password")
    public ResponseEntity<?> changeMyPassword(@RequestBody ChangePasswordDTO dto,
                                              Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(400).body(Map.of("error", "Old password incorrect"));
        }
        if (dto.getNewPassword() == null || dto.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password too short"));
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/profile/logout")
    public ResponseEntity<?> logout(){
        return ResponseEntity.ok("You have been logged out");
    }
}
