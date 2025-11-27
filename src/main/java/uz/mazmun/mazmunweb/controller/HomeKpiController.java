package uz.mazmun.mazmunweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mazmun.mazmunweb.dto.HomeKpiCreateDto;
import uz.mazmun.mazmunweb.dto.HomeKpiDto;
import uz.mazmun.mazmunweb.dto.HomeKpiUpdateDto;
import uz.mazmun.mazmunweb.service.HomeKpiService;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class HomeKpiController {

    private final HomeKpiService svc;


    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PutMapping("/admin/home/kpis/{id}")
    public HomeKpiDto update(@PathVariable Long id, @RequestBody HomeKpiUpdateDto dto){
        return svc.update(id, dto);
    }
    @GetMapping("/admin/home/kpis")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public List<HomeKpiDto> listAll() {
        return svc.listAll();
    }

    @PostMapping("/admin/home/kpis")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public HomeKpiDto create(@RequestBody HomeKpiCreateDto dto) {
        return svc.create(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/admin/home/kpis/{id}")
    public HomeKpiDto getOne(@PathVariable Long id) {
        return svc.getById(id);
    }

    @DeleteMapping("/admin/home/kpis/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/home/kpis")
    public List<HomeKpiDto> list() { return svc.listPublic(); }

    @GetMapping("/home/kpis/{code}")
    public HomeKpiDto byCode(@PathVariable String code){ return svc.getByCode(code); }
}
