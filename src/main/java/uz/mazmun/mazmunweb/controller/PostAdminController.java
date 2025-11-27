package uz.mazmun.mazmunweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.mazmun.mazmunweb.dto.PostCreateDTO;
import uz.mazmun.mazmunweb.dto.PostResDTO;
import uz.mazmun.mazmunweb.dto.PostUpdateDTO;
import uz.mazmun.mazmunweb.entity.Media;
import uz.mazmun.mazmunweb.entity.Post;
import uz.mazmun.mazmunweb.mapper.PostMapper;
import uz.mazmun.mazmunweb.service.MediaService;
import uz.mazmun.mazmunweb.service.PostService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/posts")
public class PostAdminController {

    private final PostService postService;
    private final MediaService mediaService;
    private final ObjectMapper om;     // <<-- bean sifatida injekt qildik
    private final PostMapper postMapper;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<PostResDTO> list(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size) {
        var pr = PageRequest.of(page, size, org.springframework.data.domain.Sort.by("createdAt").descending());
        return postService.listAll(pr).map(postMapper::toDto);
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResDTO> getOne(@PathVariable Integer id){
        return ResponseEntity.ok(postMapper.toDto(postService.get(id)));
    }

    @PatchMapping(value="/{id}/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResDTO> setStatus(@PathVariable Integer id,
                                                @RequestBody Map<String,String> body){
        var st = uz.mazmun.mazmunweb.enums.Status.valueOf(body.get("status"));
        return ResponseEntity.ok(postMapper.toDto(postService.setStatus(id, st)));
    }



    // JSON only
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResDTO> create(@RequestBody PostCreateDTO dto, Authentication auth){
        Integer authorId = null;
        Post saved = postService.create(dto, authorId);
        return ResponseEntity.ok(postMapper.toDto(saved));
    }

    // JSON + FILE (multipart)
    @PostMapping(value="/with-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResDTO> createWithFile(
            @RequestPart("data") String dataJson,
            @RequestPart(value="file", required=false) MultipartFile file,
            Authentication auth
    ) throws Exception {
        PostCreateDTO dto = om.readValue(dataJson, PostCreateDTO.class);
        if (file!=null && !file.isEmpty()){
            // sizning MediaService.save(file) — R2 ga yuklaydi va Media qaytaradi
            Media m = mediaService.save(file);
            dto.setCoverMediaId(m.getId().intValue());
        }
        Integer authorId = null;
        Post saved = postService.create(dto, authorId);
        return ResponseEntity.ok(postMapper.toDto(saved));
    }

    // JSON update
    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResDTO> update(@PathVariable Integer id, @RequestBody PostUpdateDTO dto){
        Post saved = postService.update(id, dto);
        return ResponseEntity.ok(postMapper.toDto(saved));
    }

    // JSON + FILE update (rasmni ham yangilash)
    @PutMapping(value="/{id}/with-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResDTO> updateWithFile(
            @PathVariable Integer id,
            @RequestPart("data") String dataJson,
            @RequestPart(value="file", required=false) MultipartFile file
    ) throws Exception {
        PostUpdateDTO dto = om.readValue(dataJson, PostUpdateDTO.class);
        if (file!=null && !file.isEmpty()){
            Media m = mediaService.save(file);
            dto.setCoverMediaId(m.getId().intValue());
        }
        Post saved = postService.update(id, dto);
        return ResponseEntity.ok(postMapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        postService.softDelete(id); // yoki eski hard delete ni qoldiring
        return ResponseEntity.noContent().build();
    }
}
