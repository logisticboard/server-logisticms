package com.example.logisticms.service.impl;


import com.example.logisticms.exception.InvalidJwtTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public String validateTokenAndGetUUID(String token){
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e){
            throw new InvalidJwtTokenException("Token has expired.", e);
        }
        catch (JwtException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw new InvalidJwtTokenException("Invalid or malformed JWT token.", e);
        }
    }
}

