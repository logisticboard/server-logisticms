package com.example.logisticms.service.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.util.UUID;

public class UpstreamHeaderUtil {

    public static String getAccessToken(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION).substring(7).trim();
    }

    public static String getUserEmail(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("X-User-Email").substring(7).trim();
    }

    public static UUID getUserId(HttpServletRequest httpServletRequest) {
        return UUID.fromString(httpServletRequest.getHeader("X-User-Id"));
    }
}
