package com.example.logisticms.service.client;

import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.ConversationResponse;
import com.example.logisticms.dto.CreateActivityConversationRequest;
import com.example.logisticms.exception.ClientException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Enumeration;
import java.util.Objects;

@Component
public class CommuncationMsClient {

    @Value("${COMMUNICATION_MS_URL}")
    private String communicationMsUrl;

    @Value("${INTERNAL_API_TOKEN}")
    private String internalApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders buildAuthHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isEmpty()) {
            headers.set("Authorization", authHeader);
        }

        // Optional: internal service token (recommended for MS-to-MS calls)
        headers.set("X-Internal-Api-Key", internalApiKey);

        return headers;
    }

    public ConversationResponse createActivityConversation(CreateActivityConversationRequest createActivityConversationRequest, HttpServletRequest httpServletRequest) {
        String url = communicationMsUrl + "/api/communications/v1/conversations";

        HttpHeaders headers = buildAuthHeaders(httpServletRequest);
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


        HttpEntity<CreateActivityConversationRequest> entity = new HttpEntity<>(createActivityConversationRequest, headers);

        try {
            ResponseEntity<ApiResponseDTO<ConversationResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            new ParameterizedTypeReference<>() {
                            }
                    );
            Objects.requireNonNull(response.getBody());
            if (response.getBody() != null && Boolean.TRUE.equals(response.getBody().isSuccess())) {
                return response.getBody().getData();
            }

            throw new ClientException(
                    ApiResponseDTO.error(
                            Objects.requireNonNull(response.getBody()).getApiPath(),
                            response.getBody().getErrorCode(),
                            response.getBody().getMessage(),
                            response.getBody().getErrorTime())
            );

        } catch (HttpClientErrorException ex) {
            throw new ClientException(ApiResponseDTO.error(
                    url,
                    HttpStatus.valueOf(ex.getStatusCode().value()),
                    "Client error when calling Logistic MS: " + ex.getResponseBodyAsString(),
                    String.valueOf(System.currentTimeMillis())
            ));
        } catch (HttpServerErrorException ex) {
            throw new ClientException(ApiResponseDTO.error(
                    url,
                    HttpStatus.valueOf(ex.getStatusCode().value()),
                    "Server error when calling Logistic MS: " + ex.getResponseBodyAsString(),
                    String.valueOf(System.currentTimeMillis())
            ));
        } catch (Exception ex) {
            throw new ClientException(ApiResponseDTO.error(
                    url,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error when calling Logistic MS: " + ex.getMessage(),
                    String.valueOf(System.currentTimeMillis())
            ));
        }
    }

}
