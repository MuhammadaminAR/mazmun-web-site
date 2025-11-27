package uz.mazmun.mazmunweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import uz.mazmun.mazmunweb.dto.UnreadCountDTO;
import uz.mazmun.mazmunweb.dto.UserMessageCreateDTO;
import uz.mazmun.mazmunweb.dto.UserMessageDTO;
import uz.mazmun.mazmunweb.service.UserMessageService;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
@EnableMethodSecurity
public class UserMessageController {

    private final UserMessageService service;

    // --- Public: Create ---
    @PostMapping("/public/messages")
    public UserMessageDTO create(@RequestBody UserMessageCreateDTO dto){
        return service.create(dto);
    }

    // --- Admin: List with pagination ---
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/admin/messages")
    public Page<UserMessageDTO> list(@RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size,
                                     @RequestParam(defaultValue = "false") boolean onlyUnread) {
        return service.list(page, size, onlyUnread);
    }

    // --- Admin: Get one ---
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/admin/messages/{id}")
    public UserMessageDTO get(@PathVariable Integer id){
        return service.get(id);
    }

    // --- Admin: Mark read ---
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PatchMapping("/admin/messages/{id}/read")
    public void markRead(@PathVariable Integer id){
        service.markRead(id);
    }

    // --- Admin: Unread count (badge uchun) ---
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/admin/messages-unread-count")
    public UnreadCountDTO unreadCount(){
        return service.unreadCount();
    }
}
