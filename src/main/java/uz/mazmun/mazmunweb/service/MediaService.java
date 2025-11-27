package uz.mazmun.mazmunweb.service;

import org.springframework.web.multipart.MultipartFile;
import uz.mazmun.mazmunweb.entity.Media;

public interface MediaService {
    String presignGetUrl(Long mediaId);
    String presignGetUrlByKey(String key);
    Media save(MultipartFile file);
    Media save(MultipartFile file, String folder);
    void delete(Long id); // ixtiyoriy

}
