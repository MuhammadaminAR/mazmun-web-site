package uz.mazmun.mazmunweb.service;

import org.springframework.http.ResponseEntity;
import uz.mazmun.mazmunweb.dto.LoginDTO;

import java.security.Principal;

public interface UserService {

    ResponseEntity<?> loginUser(LoginDTO loginDTO);

    ResponseEntity<?> resetPassword(String password, Principal user);

}
