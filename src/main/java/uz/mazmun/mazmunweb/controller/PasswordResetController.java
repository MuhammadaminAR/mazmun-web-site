package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uz.mazmun.mazmunweb.entity.PasswordResetToken;
import uz.mazmun.mazmunweb.entity.User;
import uz.mazmun.mazmunweb.repo.PasswordResetTokenRepository;
import uz.mazmun.mazmunweb.repo.UserRepository;
import uz.mazmun.mazmunweb.service.EmailService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${app.password-reset.ttl-minutes:20}")
    private int ttlMinutes;

    // Juda sodda rate-limit (IP/email ga 5 daqiqada 3 marta), ixtiyoriy
    private final Map<String, Integer> bucket = new ConcurrentHashMap<>();
    private final Map<String, Long> bucketTs = new ConcurrentHashMap<>();



    // ---------- 2) RESET (permitAll) ----------
    public record ResetPasswordDTO(String token, String newPassword) {}

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody ResetPasswordDTO dto) {
        if (dto.token()==null || dto.token().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error","Token required"));
        if (dto.newPassword()==null || dto.newPassword().length()<8)
            return ResponseEntity.badRequest().body(Map.of("error","Password too short (min 8)"));

        String tokenHash = sha256Url(dto.token());
        PasswordResetToken prt = tokenRepository.findByTokenHash(tokenHash).orElse(null);
        if (prt == null || prt.isUsed() || LocalDateTime.now().isAfter(prt.getExpiresAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Invalid or expired token"));
        }

        User user = userRepository.findById(prt.getUserId()).orElse(null);
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);

        prt.setUsed(true);
        prt.setUsedAt(LocalDateTime.now());
        tokenRepository.save(prt);

        return ResponseEntity.ok(Map.of("status","ok"));
    }

    // ---------- helpers ----------
    private String genToken() {
        byte[] b = new byte[24]; // 192-bit
        new SecureRandom().nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }
    private String sha256Url(String s) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(s.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(dig);
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}
