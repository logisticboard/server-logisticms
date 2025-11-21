package com.example.logisticms.dto;

// AutocompleteResponse.java

import lombok.Data;
import java.util.List;

@Data
public class AutocompleteResponse {
    private List<Candidate> suggestions;

    @Data
    public static class Candidate {
        private String placeId;
        private String text;
    }
}

