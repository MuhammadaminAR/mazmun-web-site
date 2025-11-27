package uz.mazmun.mazmunweb.service;

import org.springframework.http.ResponseEntity;

public interface FilterUtil {
    ResponseEntity<?> getDestinationsBySearchKeyword(String keyword); // filter uchun
}
