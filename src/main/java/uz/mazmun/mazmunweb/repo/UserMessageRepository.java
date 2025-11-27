package uz.mazmun.mazmunweb.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.mazmun.mazmunweb.entity.UserMessage;

public interface UserMessageRepository extends JpaRepository<UserMessage, Integer> {
    long countByIsReadFalse();

    Page<UserMessage> findByIsReadFalse(Pageable pageable);
}