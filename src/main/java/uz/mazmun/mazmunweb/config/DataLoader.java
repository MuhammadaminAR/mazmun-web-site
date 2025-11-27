package uz.mazmun.mazmunweb.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.mazmun.mazmunweb.entity.Role;
import uz.mazmun.mazmunweb.entity.User;
import uz.mazmun.mazmunweb.enums.RoleName;
import uz.mazmun.mazmunweb.repo.RoleRepository;
import uz.mazmun.mazmunweb.repo.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.findAll().isEmpty()) {
            Role role = new Role();
            role.setRoleName(RoleName.ROLE_ADMIN);
            roleRepository.save(role);
            Role role2 = new Role();
            role2.setRoleName(RoleName.ROLE_SUPER_ADMIN);
            roleRepository.save(role2);
        }
        if (userRepository.findAll().isEmpty()) {
            User user = new User();
            user.setUsername("MazmunSuperAdmin");
            user.setRoles(new ArrayList<>(List.of(roleRepository.findByRoleName(RoleName.ROLE_SUPER_ADMIN))));
            user.setPassword(passwordEncoder.encode("mazmunedu#2025"));
            userRepository.save(user);
        }
    }
}
