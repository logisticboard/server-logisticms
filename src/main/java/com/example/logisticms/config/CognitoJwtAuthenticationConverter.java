package com.example.logisticms.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CognitoJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String USER_ID_CLAIM = "custom:userId";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        return new JwtAuthenticationToken(jwt, extractAuthorities(jwt), principalName(jwt));
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Object roleClaim = firstPresent(jwt.getClaim("custom:role"), jwt.getClaim("role"));

        if (roleClaim instanceof String role && !role.isBlank()) {
            authorities.add(new SimpleGrantedAuthority(normalizeRole(role)));
        }

        Object groupsClaim = jwt.getClaim("cognito:groups");
        if (groupsClaim instanceof Collection<?> groups) {
            groups.stream()
                    .map(Object::toString)
                    .filter(role -> !role.isBlank())
                    .map(this::normalizeRole)
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authorities::add);
        }

        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authorities;
    }

    private String principalName(Jwt jwt) {
        String userId = jwt.getClaimAsString(USER_ID_CLAIM);
        if (userId != null && !userId.isBlank()) {
            return userId;
        }
        return jwt.getSubject();
    }

    private String normalizeRole(String role) {
        String normalized = role.trim().toUpperCase();
        return normalized.startsWith("ROLE_") ? normalized : "ROLE_" + normalized;
    }

    private Object firstPresent(Object first, Object second) {
        return first != null ? first : second;
    }
}
