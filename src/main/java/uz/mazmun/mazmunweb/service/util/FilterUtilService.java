package uz.mazmun.mazmunweb.service.util;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mazmun.mazmunweb.service.FilterUtil;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilterUtilService implements FilterUtil {




    @Override
    public ResponseEntity<?> getDestinationsBySearchKeyword(String keyword) {
        if (keyword.isEmpty()) {
            return new ResponseEntity<>("Filter is empty", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Such a country does not exist", HttpStatus.BAD_REQUEST);
    }
}
