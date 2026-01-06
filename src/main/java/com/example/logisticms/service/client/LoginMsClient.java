package com.example.logisticms.service.client;

import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.UserDetailsDto;
import com.example.logisticms.exception.LoginMsException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class LoginMsClient {

    @Value("${LOGIN_MS_URL}")
    private String loginMsUrl;

    @Value("${INTERNAL_MS_TOKEN}")
    private String internalApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<UserDetailsDto> getUserDetailsByIds(List<UUID> userIdList) {
        String url = loginMsUrl + "/api/users/batch-by-userid";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Internal-Token", internalApiKey);

        HttpEntity<List<UUID>> entity = new HttpEntity<>(userIdList, headers);

        try {
            ResponseEntity<ApiResponseDTO<List<UserDetailsDto>>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            new ParameterizedTypeReference<ApiResponseDTO<List<UserDetailsDto>>>() {}
                    );
            Objects.requireNonNull(response.getBody());
            if(response.getBody() != null && Boolean.TRUE.equals(response.getBody().isSuccess())){
                return response.getBody().getData();
            }

            throw new LoginMsException(
                    ApiResponseDTO.error(
                            Objects.requireNonNull(response.getBody()).getApiPath(),
                            response.getBody().getErrorCode(),
                            response.getBody().getMessage(),
                            response.getBody().getErrorTime())
            );

        }
        catch (HttpClientErrorException ex) {
            throw new LoginMsException(ApiResponseDTO.error(
                    url,
                    HttpStatus.valueOf(ex.getStatusCode().value()),
                    "Client error when calling Login MS: " + ex.getResponseBodyAsString(),
                    String.valueOf(System.currentTimeMillis())
            ));
        }
        catch (HttpServerErrorException ex) {
            throw new LoginMsException(ApiResponseDTO.error(
                    url,
                    HttpStatus.valueOf(ex.getStatusCode().value()),
                    "Server error when calling Login MS: " + ex.getResponseBodyAsString(),
                    String.valueOf(System.currentTimeMillis())
            ));
        }
        catch (Exception ex) {
            throw new LoginMsException(ApiResponseDTO.error(
                    url,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error when calling Login MS: " + ex.getMessage(),
                    String.valueOf(System.currentTimeMillis())
            ));
        }
    }

    public List<UserDetailsDto> getUserDetailsByEmailIds(List<String> emailsIdList) {
        String url = loginMsUrl + "/api/users/batch-by-user-email";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Internal-Token", internalApiKey);

        HttpEntity<List<String>> entity = new HttpEntity<>(emailsIdList, headers);

        try {
            ResponseEntity<ApiResponseDTO<List<UserDetailsDto>>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            new ParameterizedTypeReference<ApiResponseDTO<List<UserDetailsDto>>>() {}
                    );
            Objects.requireNonNull(response.getBody());
            if(response.getBody() != null && Boolean.TRUE.equals(response.getBody().isSuccess())){
                return response.getBody().getData();
            }

            throw new LoginMsException(
                    ApiResponseDTO.error(
                            Objects.requireNonNull(response.getBody()).getApiPath(),
                            response.getBody().getErrorCode(),
                            response.getBody().getMessage(),
                            response.getBody().getErrorTime())
            );

        }
        catch (HttpClientErrorException ex) {
            throw new LoginMsException(ApiResponseDTO.error(
                    url,
                    HttpStatus.valueOf(ex.getStatusCode().value()),
                    "Client error when calling Login MS: " + ex.getResponseBodyAsString(),
                    String.valueOf(System.currentTimeMillis())
            ));
        }
        catch (HttpServerErrorException ex) {
            ex.printStackTrace();
            throw new LoginMsException(ApiResponseDTO.error(
                    url,
                    HttpStatus.valueOf(ex.getStatusCode().value()),
                    "Server error when calling Login MS: " + ex.getResponseBodyAsString(),
                    String.valueOf(System.currentTimeMillis())
            ));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new LoginMsException(ApiResponseDTO.error(
                    url,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error when calling Login MS: " + ex.getMessage(),
                    String.valueOf(System.currentTimeMillis())
            ));
        }
    }
}
