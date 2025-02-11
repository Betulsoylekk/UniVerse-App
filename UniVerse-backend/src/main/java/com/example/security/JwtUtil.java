package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Component
    public class JwtUtil {
        @Value("${jwt.secret}")
        private String secretKey;

        private Key key;
        private final long EXPIRATION_TIME = 900_000;


        @PostConstruct
        public void init() {
            this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        }

        public String generateToken(String username) {
            return Jwts.builder()
                    .setSubject(username)
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(key)
                    .compact();
        }

        public void validateToken(String token) {
            try {
                extractAllClaims(token);
            } catch (JwtException | IllegalArgumentException e) {
                throw e;
            }
        }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw e; // Re-throw for specific handling in the filter
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Invalid token");
        }
    }
    }



