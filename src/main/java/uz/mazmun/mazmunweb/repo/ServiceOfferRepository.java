package uz.mazmun.mazmunweb.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.mazmun.mazmunweb.entity.ServiceOffer;
import uz.mazmun.mazmunweb.enums.Status;

import java.util.Optional;

public interface ServiceOfferRepository extends JpaRepository<ServiceOffer, Integer> {
  Optional<ServiceOffer> findBySlug(String slug);
  Page<ServiceOffer> findAllByStatusOrderByPublishedAtDesc(Status status, Pageable pageable);
  Page<ServiceOffer> findAllByStatus(Status status, Pageable pageable);
}