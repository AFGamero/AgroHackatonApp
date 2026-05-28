package com.agrotrace.agrotrace.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UUID userId, String email, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(UUID userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getRefreshExpiration());

        return Jwts.builder()
                .subject(userId.toString())
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID getUserIdFromToken(String token) {
        return UUID.fromString(validateToken(token).getSubject());
    }

    public boolean isTokenExpired(String token) {
        try {
            validateToken(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
