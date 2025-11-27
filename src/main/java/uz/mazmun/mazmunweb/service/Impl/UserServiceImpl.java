package uz.mazmun.mazmunweb.service.Impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.mazmun.mazmunweb.dto.LoginDTO;
import uz.mazmun.mazmunweb.entity.User;
import uz.mazmun.mazmunweb.repo.RoleRepository;
import uz.mazmun.mazmunweb.repo.UserRepository;
import uz.mazmun.mazmunweb.service.JwtService;
import uz.mazmun.mazmunweb.service.UserService;

import java.security.Principal;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @Override
    public ResponseEntity<?> loginUser(LoginDTO loginDTO) {
            var authentication = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authentication);
            User user = (User) authenticate.getPrincipal();
            String token = jwtService.generateToken(user);
//        System.out.println(token);
        return ResponseEntity.ok().body(token);
    }

    @Override
    public ResponseEntity<?> resetPassword(String password, Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(password));
                    userRepository.save(user);
                    return ResponseEntity.ok(user); // User qaytadi
                })
                .orElseThrow();
    }
}
