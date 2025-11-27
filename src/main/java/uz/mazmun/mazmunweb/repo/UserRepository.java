package uz.mazmun.mazmunweb.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import uz.mazmun.mazmunweb.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
//    boolean existsByUsername(String username);
    boolean existsByUsername(String username);

    @Query("""
      select u from User u
      where (lower(u.username) like concat('%', :q, '%')
         or lower(u.fullName) like concat('%', :q, '%')
         or lower(u.email)    like concat('%', :q, '%'))
    """)
    Page<User> search(String q, Pageable pageable);

    @Query("select count(u) from User u")
    long countAllUsers();

    @Query("""
      select count(distinct u) from User u
      join u.roles r
      where r.roleName = uz.mazmun.mazmunweb.enums.RoleName.ROLE_ADMIN
    """)
    long countAdmins();
}