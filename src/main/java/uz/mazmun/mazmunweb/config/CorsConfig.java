package uz.mazmun.mazmunweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();

        // PROD: aniq domen(lar)ni yozing
        c.setAllowedOrigins(List.of(
                "https://mazmun.uz",
                "https://app.mazmun.uz",
                "http://localhost:8080"    // dev front
        ));

        c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        c.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        c.setExposedHeaders(List.of("Location", "Content-Disposition")); // downloadlar uchun foydali
        c.setAllowCredentials(false); // JWT header bilan ishlasangiz, odatda false
        c.setMaxAge(3600L); // preflight cache: 1 soat

        UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
        s.registerCorsConfiguration("/**", c);
        return s;
    }
}

