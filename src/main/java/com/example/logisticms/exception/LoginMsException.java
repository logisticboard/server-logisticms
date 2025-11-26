package com.example.logisticms.exception;

import com.example.logisticms.dto.ApiResponseDTO;
import lombok.Getter;

@Getter
public class LoginMsException extends RuntimeException {
    private final ApiResponseDTO<Void> response;

    public LoginMsException(ApiResponseDTO<Void> response) {
        super(response.getMessage());
        this.response = response;
    }
}