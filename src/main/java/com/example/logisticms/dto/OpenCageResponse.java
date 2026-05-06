package com.example.logisticms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenCageResponse {
    private List<Result> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private Geometry geometry;
        private String formatted; // ✅ FIX

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Geometry {
            private double lat;
            private double lng;
        }
    }
}