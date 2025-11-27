package uz.mazmun.mazmunweb.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class UploadsInitConfig {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @PostConstruct
    public void init() throws Exception {
        Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(root)) Files.createDirectories(root);
    }
}