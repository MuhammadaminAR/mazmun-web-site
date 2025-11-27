package uz.mazmun.mazmunweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mazmun.mazmunweb.dto.HomePageVideoDTO;
import uz.mazmun.mazmunweb.service.HomePageVideoService;

@RestController
@RequestMapping("/api")
public class HomePageVideoController {
    private final HomePageVideoService service;
    public HomePageVideoController(HomePageVideoService service){ this.service = service; }

    /** Public GET: hamma foydalanuvchi uchun */
    @GetMapping("/homepage-video")
    public ResponseEntity<HomePageVideoDTO> getPublic(){
        return ResponseEntity.ok(service.get());
    }

    /** Admin GET: UIga yuklash */
    @GetMapping("/admin/homepage-video")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<HomePageVideoDTO> getAdmin(){
        return ResponseEntity.ok(service.get());
    }

    /** Admin PUT: linkni saqlash (yoki null bilan tozalash) */
    @PutMapping(value="/admin/homepage-video",
            consumes = { "text/plain", "application/json" },
            produces = "application/json")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<HomePageVideoDTO> put(@RequestBody(required=false) String url){
        if (url != null) url = url.trim();
        return ResponseEntity.ok(service.put((url==null || url.isBlank()) ? null : url));
    }




}
