package uz.mazmun.mazmunweb.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private Path root() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    private static String sanitizeFolder(String folder) {
        // faqat harf-raqam, _, -, / ruxsat
        return StringUtils.hasText(folder)
                ? folder.replaceAll("[^a-zA-Z0-9_\\-/]", "_")
                : "common";
    }

    /** Path traversaldan himoya */
    private Path safeResolve(String relativeKey) {
        Path target = root().resolve(relativeKey).normalize();
        if (!target.startsWith(root())) {
            throw new SecurityException("Invalid path");
        }
        return target;
    }

    public String save(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Empty file");

        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.')); // .jpg
        }
        String safeFolder = sanitizeFolder(folder);

        LocalDate today = LocalDate.now();
        String key = safeFolder + "/"
                + today.getYear() + "/"
                + String.format("%02d", today.getMonthValue()) + "/"
                + UUID.randomUUID() + ext;

        Path out = safeResolve(key);
        Files.createDirectories(out.getParent());
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, out);
        }
        // Windowsdan kelgan backslashlarni normalizatsiya qilamiz:
        return key.replace("\\", "/");
    }

    public boolean delete(String key) throws IOException {
        Path p = safeResolve(key);
        return Files.deleteIfExists(p);
    }

    public Path resolve(String key) { return safeResolve(key); }
    public boolean exists(String key) { return Files.exists(safeResolve(key)); }
}