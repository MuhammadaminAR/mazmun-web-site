package uz.mazmun.mazmunweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uz.mazmun.mazmunweb.dto.HomeKpiCreateDto;
import uz.mazmun.mazmunweb.dto.HomeKpiDto;
import uz.mazmun.mazmunweb.dto.HomeKpiUpdateDto;
import uz.mazmun.mazmunweb.entity.HomeKpi;
import uz.mazmun.mazmunweb.repo.HomeKpiRepository;
import uz.mazmun.mazmunweb.repo.ProjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeKpiService {

    private final HomeKpiRepository repo;
    private final ProjectRepository projectRepository;

    /* ========= PUBLIC API (homepage) ========= */

    public List<HomeKpiDto> listPublic() {
        return repo.findAllByEnabledTrueOrderBySortOrderAsc().stream()
                .map(this::materialize)   // computed bo‘lsa shu yerda hisoblanadi
                .map(this::toDto)
                .toList();
    }

    public HomeKpiDto getByCode(String code){
        return repo.findByCode(code)
                .map(this::materialize)
                .map(this::toDto)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "KPI code topilmadi"));
    }

    /* ========= ADMIN API ========= */

    // Jadval uchun — hammasini qaytaradi (enabled bo‘lishi shart emas)
    public List<HomeKpiDto> listAll() {
        return repo.findAllByOrderBySortOrderAsc().stream()
                .map(this::materialize)  // admin tomonda ham ko‘rsatsak bo‘ladi
                .map(this::toDto)
                .toList();
    }

    // Edit modal uchun
    public HomeKpiDto getById(Long id){
        HomeKpi e = repo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "KPI topilmadi"));
        return toDto(materialize(e));
    }

    // CREATE
    public HomeKpiDto create(HomeKpiCreateDto dto){
        // code unikal bo‘lsin
        if (repo.existsByCode(dto.getCode())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Bu code bilan KPI allaqachon mavjud: " + dto.getCode()
            );
        }

        HomeKpi e = new HomeKpi();
        e.setCode(dto.getCode());
        e.setValue(dto.getValue());
        e.setSuffix(dto.getSuffix());
        e.setLabel(dto.getLabel());
        e.setIconClass(dto.getIconClass());
        e.setSortOrder(dto.getSortOrder());
        e.setEnabled(dto.getEnabled());
        e.setComputed(dto.getComputed());

        return toDto(repo.save(e));
    }

    // UPDATE (mavjudiga qisman o‘zgartirish)
    public HomeKpiDto update(Long id, HomeKpiUpdateDto dto){
        HomeKpi e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "KPI topilmadi"));

        // code’ni odatda o‘zgartirmaymiz, shuning uchun teginmadik
        if (dto.getValue() != null)      e.setValue(dto.getValue());
        if (dto.getSuffix() != null)     e.setSuffix(dto.getSuffix());
        if (dto.getLabel() != null)      e.setLabel(dto.getLabel());
        if (dto.getIconClass() != null)  e.setIconClass(dto.getIconClass());
        if (dto.getSortOrder() != null)  e.setSortOrder(dto.getSortOrder());
        if (dto.getEnabled() != null)    e.setEnabled(dto.getEnabled());
        if (dto.getComputed() != null)   e.setComputed(dto.getComputed());

        return toDto(repo.save(e));
    }

    // DELETE
    public void delete(Long id){
        if (!repo.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "KPI topilmadi");
        }
        repo.deleteById(id);
    }

    /* ========= Ichki yordamchi metodlar ========= */

    // “computed = true” bo‘lganlar uchun qiymatni dinamik hisoblash
    private HomeKpi materialize(HomeKpi e){
        if (Boolean.TRUE.equals(e.getComputed()) && "projects".equals(e.getCode())){
            int computed = Math.toIntExact(projectRepository.count()) + 10;
            e.setValue(computed);
        }
        return e;
    }

    private HomeKpiDto toDto(HomeKpi e){
        return new HomeKpiDto(
                e.getId(),
                e.getCode(),
                e.getValue(),
                e.getSuffix(),
                e.getLabel(),
                e.getIconClass(),
                e.getSortOrder(),
                e.getEnabled(),
                e.getComputed()
        );
    }
}
