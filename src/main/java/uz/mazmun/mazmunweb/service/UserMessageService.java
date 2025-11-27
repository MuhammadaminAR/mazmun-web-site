package uz.mazmun.mazmunweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mazmun.mazmunweb.dto.UnreadCountDTO;
import uz.mazmun.mazmunweb.dto.UserMessageCreateDTO;
import uz.mazmun.mazmunweb.dto.UserMessageDTO;
import uz.mazmun.mazmunweb.entity.UserMessage;
import uz.mazmun.mazmunweb.repo.UserMessageRepository;

@Service
@RequiredArgsConstructor
public class UserMessageService {

    private final UserMessageRepository repo;

    @Transactional
    public UserMessageDTO create(UserMessageCreateDTO dto) {
        UserMessage entity = UserMessage.builder()
                .fullName(dto.getFullName().trim())
                .contact(dto.getContact().trim())
                .message(dto.getMessage().trim())
                .isRead(false)
                .build();
        entity = repo.save(entity);
        return toDTO(entity);
    }

    @Transactional(readOnly = true)
    public Page<UserMessageDTO> list(Integer page, Integer size, boolean onlyUnread) {
        Pageable pageable = PageRequest.of(
                page == null ? 0 : Math.max(page, 0),
                size == null ? 20 : Math.min(Math.max(size, 1), 100),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        Page<UserMessage> p = onlyUnread ? repo.findByIsReadFalse(pageable) : repo.findAll(pageable);
        return p.map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public UserMessageDTO get(Integer id) {
        UserMessage e = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Message not found"));
        return toDTO(e);
    }

    @Transactional
    public void markRead(Integer id) {
        UserMessage e = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Message not found"));
        if (!e.isRead()) {
            e.setRead(true);
            repo.save(e);
        }
    }

    @Transactional(readOnly = true)
    public UnreadCountDTO unreadCount() {
        long c = repo.countByIsReadFalse();
        return new UnreadCountDTO(c);
    }

    private UserMessageDTO toDTO(UserMessage e) {
        return UserMessageDTO.builder()
                .id(e.getId())
                .fullName(e.getFullName())
                .contact(e.getContact())
                .message(e.getMessage())
                .read(e.isRead())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
