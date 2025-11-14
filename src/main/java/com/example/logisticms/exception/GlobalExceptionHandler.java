package com.example.logisticms.exception;

import com.example.logisticms.dto.ApiResponseDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleException(Exception exception, WebRequest webRequest){
        exception.printStackTrace();
        ApiResponseDTO<Void> dto = ApiResponseDTO.error(
                webRequest.getDescription(false).replace("uri=", ""),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleValidationException(MethodArgumentNotValidException exception, WebRequest webRequest){
        String errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ApiResponseDTO<Void> dto = ApiResponseDTO.error(
                webRequest.getDescription(false).replace("uri=", ""),
                HttpStatus.BAD_REQUEST,
                errorMessage,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, WebRequest webRequest) {
        String errorMessage = "Required request body is missing.";
        ApiResponseDTO<Void> dto = ApiResponseDTO.error(
                webRequest.getDescription(false).replace("uri=", ""),
                HttpStatus.BAD_REQUEST,
                errorMessage,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }



    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleRateLimitExceededException(RateLimitExceededException ex, WebRequest webRequest) {
        ApiResponseDTO<Void> dto = ApiResponseDTO.error(
                webRequest.getDescription(false).replace("uri=", ""),
                HttpStatus.TOO_MANY_REQUESTS,
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(dto);
    }



    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleUserBlockedException(NoResourceFoundException ex, WebRequest webRequest) {
        ex.printStackTrace();
        ApiResponseDTO<Void> dto = ApiResponseDTO.error(
                webRequest.getDescription(false).replace("uri=", ""),
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(dto);
    }
}
