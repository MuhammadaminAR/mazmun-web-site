package uz.mazmun.mazmunweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.mazmun.mazmunweb.dto.TrustedUserLogoCreateDTO;
import uz.mazmun.mazmunweb.dto.TrustedUserLogoDTO;
import uz.mazmun.mazmunweb.dto.TrustedUserLogoResDTO;
import uz.mazmun.mazmunweb.entity.Media;
import uz.mazmun.mazmunweb.entity.TrustedUserLogo;
import uz.mazmun.mazmunweb.enums.Status;
import uz.mazmun.mazmunweb.repo.MediaRepository;
import uz.mazmun.mazmunweb.repo.TrustedUserLogoRepository;

@Service
@RequiredArgsConstructor
public class TrustedUserLogoService {
    private final MediaRepository mediaRepo;
    private final TrustedUserLogoRepository repo;

    public TrustedUserLogoDTO create(TrustedUserLogoCreateDTO dto) {
        TrustedUserLogo e = new TrustedUserLogo();
        e.setUsername(dto.getUsername());
        // status string safe parse
        Status st = "PUBLISHED".equalsIgnoreCase(dto.getStatus()) ? Status.PUBLISHED : Status.DRAFT;
        e.setStatus(st);

        if (dto.getCoverMediaId() != null) {
            Media m = mediaRepo.findById(dto.getCoverMediaId().longValue())
                    .orElseThrow(() -> new IllegalArgumentException("Media not found"));
            e.setLogo(m);
        }
        e = repo.save(e);
        return toDto(e);
    }

    public Page<TrustedUserLogoDTO> list(Pageable pageable){
        return repo.findAllByDeletedFalse(pageable).map(this::toDto);
    }

    public TrustedUserLogoDTO get(Integer id){
        return toDto(repo.findByIdAndDeletedFalse(id).orElseThrow());
    }

    public TrustedUserLogoDTO patch(Integer id, TrustedUserLogoCreateDTO dto){
        TrustedUserLogo e = repo.findByIdAndDeletedFalse(id).orElseThrow();
        if (dto.getUsername()!=null) e.setUsername(dto.getUsername());
        if (dto.getStatus()!=null){
            e.setStatus("PUBLISHED".equalsIgnoreCase(dto.getStatus()) ? Status.PUBLISHED : Status.DRAFT);
        }
        if (dto.getCoverMediaId()!=null){
            Media m = mediaRepo.findById(dto.getCoverMediaId().longValue()).orElseThrow();
            e.setLogo(m);
        }
        e = repo.save(e);
        return toDto(e);
    }

    public void delete(Integer id){
        TrustedUserLogo e = repo.findByIdAndDeletedFalse(id).orElseThrow();
        e.setDeleted(true);
        repo.save(e);
    }

    private TrustedUserLogoDTO toDto(TrustedUserLogo e){
        TrustedUserLogoDTO d = new TrustedUserLogoDTO();
        d.setId(e.getId());
        d.setUsername(e.getUsername());
        d.setStatus(e.getStatus().name());
        if (e.getLogo()!=null){
            d.setLogoId(e.getLogo().getId().intValue());
            d.setLogoUrl("/api/media/" + e.getLogo().getId() + "/view");
        }
        return d;
    }
    public Page<TrustedUserLogoResDTO> listPublic(Pageable pageable) {
        Page<TrustedUserLogo> page = repo.findAllByDeletedFalseAndStatus(Status.PUBLISHED, pageable);
        return page.map(t -> new TrustedUserLogoResDTO(
                t.getId(),
                t.getUsername(),
                // media URL – sizning tizimingizdagi odatiy ko‘rinish:
                (t.getLogo()!=null && t.getLogo().getId()!=null)
                        ? "/api/media/" + t.getLogo().getId() + "/view"
                        : null
        ));
    }
}
