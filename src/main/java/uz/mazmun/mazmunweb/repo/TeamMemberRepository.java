package uz.mazmun.mazmunweb.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.mazmun.mazmunweb.entity.TeamMember;
import uz.mazmun.mazmunweb.enums.Status;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    @EntityGraph(attributePaths = "socials")
    Page<TeamMember> findAllByDeletedFalse(Pageable pageable);

    @EntityGraph(attributePaths = "socials")
    Optional<TeamMember> findByIdAndDeletedFalse(Long id);

    @EntityGraph(attributePaths = "socials")
    List<TeamMember> findAllByDeletedFalseAndStatusOrderByOrderIndexAsc(Status status);
}