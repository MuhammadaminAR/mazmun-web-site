package uz.mazmun.mazmunweb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mazmun.mazmunweb.entity.Role;
import uz.mazmun.mazmunweb.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {
  Role findByRoleName(RoleName roleName);

}