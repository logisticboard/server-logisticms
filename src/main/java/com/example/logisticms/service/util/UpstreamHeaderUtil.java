package com.example.logisticms.service.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public class UpstreamHeaderUtil {

    public static String getAccessToken(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthenticationCredentialsNotFoundException("Missing bearer token");
        }
        return authorization.substring(7).trim();
    }

    public static String getUserEmail(HttpServletRequest httpServletRequest) {
        return getRequiredClaim("email");
    }

    public static UUID getUserId(HttpServletRequest httpServletRequest) {
        String userId = getRequiredClaim("username");
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException ex) {
            throw new AuthenticationCredentialsNotFoundException("Invalid custom:userId claim", ex);
        }
    }

    private static String getRequiredClaim(String claimName) {
        Jwt jwt = getAuthenticatedJwt();
        String value = jwt.getClaimAsString(claimName);
        if (value == null || value.isBlank()) {
            throw new AuthenticationCredentialsNotFoundException("Missing " + claimName + " claim");
        }
        return value;
    }

    private static Jwt getAuthenticatedJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new AuthenticationCredentialsNotFoundException("Authenticated JWT is missing");
        }
        return jwt;
    }
}
