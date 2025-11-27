package uz.mazmun.mazmunweb.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.mazmun.mazmunweb.entity.Role;
import uz.mazmun.mazmunweb.entity.User;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class JwtService {


    public String  generateToken(User user) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        StringJoiner sj = new StringJoiner(",");
        for (Role role : user.getRoles()) {
            sj.add(role.getRoleName().toString());
        }
        claims.put("roles", sj.toString());
        return  Jwts.builder()
                .subject(user.getUsername())
                .signWith(getSecretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24*10))
                .claims(claims)
                .compact();
    }


    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(30, ChronoUnit.DAYS)))
                .claim("type", "refresh")
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor("1234567891011121314151617181920212223242526272829303132".getBytes());
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        Claims cl = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return cl.getSubject();
    }
}
