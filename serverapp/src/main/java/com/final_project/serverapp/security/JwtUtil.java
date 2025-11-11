package com.final_project.serverapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${security.jwt.access-expire-ms}")
    private long ACCESS_EXPIRE;

    @Value("${security.jwt.refresh-expire-ms}")
    private long REFRESH_EXPIRE;

    @Value("${security.jwt.secret-key}")
    private String SECRET;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
    }
    
    public Long getRefreshExpire() {
        return this.REFRESH_EXPIRE;
    }

    public Long getRefreshExpireSecond() {
        return this.getRefreshExpire() / 1000;
    }
    
    public Long getAccessExpire() {
        return this.ACCESS_EXPIRE;
    }

    public Long getAccessExpireSecond() {
        return this.getAccessExpire() / 1000;
    }

    public String generateAccessToken(String email) {
        return buildToken(email, ACCESS_EXPIRE);
    }

    public String generateRefreshToken(String email) {
        return buildToken(email, REFRESH_EXPIRE);
    }

    private String buildToken(String email, long expiration) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
