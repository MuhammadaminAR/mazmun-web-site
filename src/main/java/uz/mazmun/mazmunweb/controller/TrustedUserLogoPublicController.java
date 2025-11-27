package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.mazmun.mazmunweb.dto.TrustedUserLogoResDTO;
import uz.mazmun.mazmunweb.service.TrustedUserLogoService;

@RestController
@RequiredArgsConstructor
public class TrustedUserLogoPublicController {

    private final TrustedUserLogoService service;

    @GetMapping("/api/trusted-user-logos")
    public Page<TrustedUserLogoResDTO> list(@PageableDefault(size = 50, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.listPublic(pageable);
    }
}

