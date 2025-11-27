package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import uz.mazmun.mazmunweb.entity.Media;
import uz.mazmun.mazmunweb.repo.MediaRepository;
import uz.mazmun.mazmunweb.service.MediaService;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaRepository mediaRepository;
    private final MediaService mediaService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Media> upload(@RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "folder", required = false) String folder) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }
        Media m = mediaService.save(file, (folder == null ? "common" : folder));
        return ResponseEntity.status(HttpStatus.CREATED).body(m);
    }

    @GetMapping("/{id}/view")
    public ResponseEntity<Void> view(@PathVariable Long id) {
        if (!mediaRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        String url = mediaService.presignGetUrl(id);
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", url).build();
    }

    @GetMapping("/url")
    public ResponseEntity<Void> urlByKey(@RequestParam String key) {
        String url = mediaService.presignGetUrlByKey(key);
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", url).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mediaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
