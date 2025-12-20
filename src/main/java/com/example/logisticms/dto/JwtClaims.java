package com.example.logisticms.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtClaims {
    private String jwtSubject;
    private String role;
}
