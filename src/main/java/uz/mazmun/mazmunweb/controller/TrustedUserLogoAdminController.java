package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mazmun.mazmunweb.dto.TrustedUserLogoCreateDTO;
import uz.mazmun.mazmunweb.dto.TrustedUserLogoDTO;
import uz.mazmun.mazmunweb.service.TrustedUserLogoService;

@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class TrustedUserLogoAdminController {
    private final TrustedUserLogoService svc;

    @PostMapping("/trusted-user-logo")
    public ResponseEntity<TrustedUserLogoDTO> create(@RequestBody TrustedUserLogoCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(svc.create(dto));
    }

    @GetMapping("/trusted-user-logos")
    public Page<TrustedUserLogoDTO> list(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable p){
        return svc.list(p);
    }

    @GetMapping("/trusted-user-logo/{id}")
    public TrustedUserLogoDTO one(@PathVariable Integer id){
        return svc.get(id);
    }

    @PatchMapping("/trusted-user-logo/{id}")
    public TrustedUserLogoDTO patch(@PathVariable Integer id, @RequestBody TrustedUserLogoCreateDTO dto){
        return svc.patch(id, dto);
    }

    @DeleteMapping("/trusted-user-logo/{id}")
    public void delete(@PathVariable Integer id){
        svc.delete(id);
    }
}
