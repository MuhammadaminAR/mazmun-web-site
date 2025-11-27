package uz.mazmun.mazmunweb.service;

import org.springframework.http.ResponseEntity;

public interface EmailService {
    ResponseEntity<?> sendEmail(String to, String subject, String text);
}
