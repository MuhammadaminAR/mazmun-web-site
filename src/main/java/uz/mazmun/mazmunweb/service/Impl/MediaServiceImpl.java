package uz.mazmun.mazmunweb.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.mazmun.mazmunweb.entity.Media;
import uz.mazmun.mazmunweb.repo.MediaRepository;
import uz.mazmun.mazmunweb.service.MediaService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final LocalStorageService localStorageService;

    @Override
    public String presignGetUrl(Long mediaId) {
        Media m = mediaRepository.findById(mediaId).orElseThrow();
        return "/uploads/" + m.getKey();
    }

    @Override
    public String presignGetUrlByKey(String key) {
        return "/uploads/" + key;
    }

    @Override
    public Media save(MultipartFile file) { return save(file, "common"); }

    @Override
    public Media save(MultipartFile file, String folder) {
        try {
            String path = localStorageService.save(file, folder);

            String originalName = Optional.ofNullable(file.getOriginalFilename()).orElse("file");
            // Windows yo'llarini tozalash
            originalName = originalName.replace("\\", "/");
            int slash = originalName.lastIndexOf('/');
            if (slash >= 0) originalName = originalName.substring(slash + 1);
            if (originalName.length() > 255) originalName = originalName.substring(0, 255);

            String ct = Optional.ofNullable(file.getContentType()).orElse("application/octet-stream");
            if (ct.length() > 100) ct = ct.substring(0, 100);

            Media m = Media.builder()
                    .key(path)                 // nisbiy yo‘l (512 gacha)
                    .filename(originalName)    // 255 gacha
                    .contentType(ct)           // 100 gacha
                    .size(file.getSize())
                    .build();

            return mediaRepository.save(m);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }


    @Override
    public void delete(Long id) {
        mediaRepository.findById(id).ifPresent(m -> {
            try { localStorageService.delete(m.getKey()); } catch (IOException ignored) {}
            mediaRepository.deleteById(id);
        });
    }
}
