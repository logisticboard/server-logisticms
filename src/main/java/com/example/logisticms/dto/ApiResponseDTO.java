package com.example.logisticms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApiResponseDTO<T> {
    private String message;
    private boolean success;
    private T data;

    private String apiPath;
    private HttpStatus errorCode;
    private String errorTime;

    public ApiResponseDTO(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ApiResponseDTO(String message, boolean success, T data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public static <T> ApiResponseDTO<T> error(String apiPath, HttpStatus errorCode, String errorMessage, String errorTime){
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.setSuccess(false);
        response.setApiPath(apiPath);
        response.setErrorCode(errorCode);
        response.setMessage(errorMessage);
        response.setErrorTime(errorTime);
        return response;
    }

}
