package com.example.logisticms.config;

import com.example.logisticms.dto.ApiResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {



        ApiResponseDTO<Void> dto = ApiResponseDTO.<Void>builder()
                .apiPath(request.getRequestURI())
                .errorCode(HttpStatus.UNAUTHORIZED)
                .errorTime( LocalDateTime.now().toString())
                .message("Unauthorized: Invalid or expired token")
                .build();


        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        response.getWriter()
                .write(objectMapper.writeValueAsString(dto));
    }
}

