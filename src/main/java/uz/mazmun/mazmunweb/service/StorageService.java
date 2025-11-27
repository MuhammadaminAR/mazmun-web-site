package uz.mazmun.mazmunweb.service;

import java.net.URL;
import java.time.Duration;

public interface StorageService {
    String generateKey(String originalName);
    URL presignPut(String key, String contentType, Duration ttl, long contentLength);
    URL presignGet(String key, Duration ttl);
    void delete(String key);
}

