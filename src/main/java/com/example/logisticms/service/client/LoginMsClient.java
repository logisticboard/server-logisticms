package com.example.logisticms.service.client;

import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.UserDetailsDto;
import com.example.logisticms.exception.LoginMsException;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Enumeration;
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

    public List<UserDetailsDto> getUserDetailsByIds(List<UUID> userIdList, HttpServletRequest httpServletRequest) {
        String url = loginMsUrl + "/api/v1/users/batch-by-userid";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();

            if (headerName.toLowerCase().startsWith("x-")) {
                Enumeration<String> values = httpServletRequest.getHeaders(headerName);

                while (values.hasMoreElements()) {
                    headers.add(headerName, values.nextElement());
                }
            }
        }


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

    public List<UserDetailsDto> getUserDetailsByEmailIds(List<String> emailsIdList, HttpServletRequest httpServletRequest) {
        String url = loginMsUrl + "/api/v1/users/batch-by-user-email";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();

            if (headerName.toLowerCase().startsWith("x-")) {
                Enumeration<String> values = httpServletRequest.getHeaders(headerName);

                while (values.hasMoreElements()) {
                    headers.add(headerName, values.nextElement());
                }
            }
        }


        HttpEntity<List<String>> entity = new HttpEntity<>(emailsIdList, headers);

        try {
            ResponseEntity<ApiResponseDTO<List<UserDetailsDto>>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            new ParameterizedTypeReference<>() {}
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
