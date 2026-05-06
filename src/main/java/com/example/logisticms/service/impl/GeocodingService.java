package com.example.logisticms.service.impl;

import com.example.logisticms.dto.GeoResult;
import com.example.logisticms.dto.OpenCageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class GeocodingService {

    @Value("${opencage.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public GeoResult geocode(String address) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.opencagedata.com/geocode/v1/json")
                .queryParam("q", address)
                .queryParam("key", apiKey)
                .toUriString();

        System.out.println(restTemplate.getForObject(url, Map.class));
        OpenCageResponse response = restTemplate.getForObject(url, OpenCageResponse.class);

        if (response == null || response.getResults().isEmpty()) {
            throw new RuntimeException("No results found");
        }


        OpenCageResponse.Result result = response.getResults().get(0);

        return new GeoResult(
                result.getGeometry().getLat(),
                result.getGeometry().getLng(),
                result.getFormatted()
        );
    }

    public GeoResult reverseGeocode(double lat, double lng) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.opencagedata.com/geocode/v1/json")
                .queryParam("q", lat + "," + lng)
                .queryParam("key", apiKey)
                .toUriString();

        OpenCageResponse response = restTemplate.getForObject(url, OpenCageResponse.class);

        if (response == null || response.getResults().isEmpty()) {
            throw new RuntimeException("No results found");
        }

        OpenCageResponse.Result result = response.getResults().get(0);

        return new GeoResult(
                result.getGeometry().getLat(),
                result.getGeometry().getLng(),
                result.getFormatted()
        );
    }
}
