package uz.mazmun.mazmunweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.mazmun.mazmunweb.dto.SocialDTO;
import uz.mazmun.mazmunweb.dto.TeamMemberCreateDTO;
import uz.mazmun.mazmunweb.dto.TeamMemberDTO;
import uz.mazmun.mazmunweb.dto.TeamMemberPublicDTO;
import uz.mazmun.mazmunweb.entity.Media;
import uz.mazmun.mazmunweb.entity.TeamMember;
import uz.mazmun.mazmunweb.entity.TeamSocialLink;
import uz.mazmun.mazmunweb.enums.Status;
import uz.mazmun.mazmunweb.repo.MediaRepository;
import uz.mazmun.mazmunweb.repo.TeamMemberRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamMemberService {
    private final TeamMemberRepository repo;
    private final MediaRepository mediaRepo;

    public TeamMemberDTO create(TeamMemberCreateDTO d){
        TeamMember e = new TeamMember();
        apply(e, d, true);
        e = repo.save(e);
        return toDto(e);
    }

    public Page<TeamMemberDTO> list(Pageable p){
        return repo.findAllByDeletedFalse(p).map(this::toDto);
    }

    public TeamMemberDTO one(Long id){
        return toDto(repo.findByIdAndDeletedFalse(id).orElseThrow());
    }

    public TeamMemberDTO patch(Long id, TeamMemberCreateDTO d){
        TeamMember e = repo.findByIdAndDeletedFalse(id).orElseThrow();
        apply(e, d, false);
        e = repo.save(e);
        return toDto(e);
    }

    public void delete(Long id){
        TeamMember e = repo.findByIdAndDeletedFalse(id).orElseThrow();
        e.setDeleted(true);
        repo.save(e);
    }

    public List<TeamMemberPublicDTO> listPublic(){
        return repo.findAllByDeletedFalseAndStatusOrderByOrderIndexAsc(Status.PUBLISHED)
                .stream().map(this::toPublicDto).toList();
    }

    /* ===== helpers ===== */
    private void apply(TeamMember e, TeamMemberCreateDTO d, boolean creating){
        if(d.getFullName()!=null) e.setFullName(d.getFullName());
        if(d.getRoleTitle()!=null) e.setRoleTitle(d.getRoleTitle());
        if(d.getOrderIndex()!=null) e.setOrderIndex(d.getOrderIndex());
        if(d.getStatus()!=null) e.setStatus("PUBLISHED".equalsIgnoreCase(d.getStatus()) ? Status.PUBLISHED : Status.DRAFT);

        if(d.getPhotoMediaId()!=null){
            Media m = mediaRepo.findById(d.getPhotoMediaId().longValue())
                    .orElseThrow(() -> new IllegalArgumentException("Media not found"));
            e.setPhoto(m);
        } else if (creating && e.getPhoto()==null){
            throw new IllegalArgumentException("photoMediaId required");
        }

        if(d.getSocials()!=null){
            List<TeamSocialLink> items = d.getSocials().stream().limit(4).map(s -> {
                TeamSocialLink l = new TeamSocialLink();
                l.setNetwork(s.getNetwork());
                l.setIconClass( (s.getIconClass()==null || s.getIconClass().isBlank())
                        ? defaultIcon(s.getNetwork()) : s.getIconClass());
                l.setUrl(s.getUrl());
                return l;
            }).toList();
            e.setSocials(items); // full replace
        }
    }

    private String defaultIcon(String network){
        String key = Objects.toString(network,"").toLowerCase();
        return switch (key){
            case "telegram" -> "bi-telegram";
            case "linkedin" -> "bi-linkedin";
            case "x","twitter","twitter-x" -> "bi-twitter-x";
            case "facebook" -> "bi-facebook";
            case "instagram" -> "bi-instagram";
            default -> "bi-link-45deg";
        };
    }

    private TeamMemberDTO toDto(TeamMember e){
        TeamMemberDTO d = new TeamMemberDTO();
        d.setId(e.getId());
        d.setFullName(e.getFullName());
        d.setRoleTitle(e.getRoleTitle());
        d.setStatus(e.getStatus().name());
        d.setOrderIndex(e.getOrderIndex());
        if(e.getPhoto()!=null && e.getPhoto().getId()!=null){
            d.setPhotoId(e.getPhoto().getId().intValue());
            d.setPhotoUrl("/api/media/" + e.getPhoto().getId() + "/view");
        }
        d.setSocials(e.getSocials().stream().map(s -> {
            SocialDTO sDto = new SocialDTO();
            sDto.setNetwork(s.getNetwork());
            sDto.setIconClass(s.getIconClass());
            sDto.setUrl(s.getUrl());
            return sDto;
        }).toList());
        return d;
    }

    private TeamMemberPublicDTO toPublicDto(TeamMember e){
        String url = (e.getPhoto()!=null && e.getPhoto().getId()!=null)
                ? "/api/media/" + e.getPhoto().getId() + "/view" : null;
        List<SocialDTO> socials = e.getSocials().stream().map(s -> {
            SocialDTO sDto = new SocialDTO();
            sDto.setNetwork(s.getNetwork());
            sDto.setIconClass(s.getIconClass());
            sDto.setUrl(s.getUrl());
            return sDto;
        }).toList();
        return new TeamMemberPublicDTO(e.getId(), e.getFullName(), e.getRoleTitle(), url, socials,e.getOrderIndex());
    }
}
