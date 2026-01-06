package com.example.logisticms.service.impl;


import com.example.logisticms.dto.JwtClaims;
import com.example.logisticms.exception.InvalidJwtTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public JwtClaims validateTokenAndGetUUID(String token){
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return JwtClaims.builder()
                    .jwtSubject(claims.getSubject())
                    .role(claims.get("role", String.class))
                    .build();
        } catch (ExpiredJwtException e){
            throw new InvalidJwtTokenException("Token has expired.", e);
        }
        catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtTokenException("Invalid or malformed JWT token.", e);
        }
    }
}

