package uz.mazmun.mazmunweb.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.mazmun.mazmunweb.entity.TrustedUserLogo;
import uz.mazmun.mazmunweb.enums.Status;

import java.util.Optional;

public interface TrustedUserLogoRepository extends JpaRepository<TrustedUserLogo, Integer> {
    Page<TrustedUserLogo> findAllByDeletedFalse(Pageable pageable);
    Optional<TrustedUserLogo> findByIdAndDeletedFalse(Integer id);
    Page<TrustedUserLogo> findAllByDeletedFalseAndStatus(Status status, Pageable pageable);
}