package com.example.logisticms.dto;


import lombok.Data;
import java.util.List;

@Data
public class AutocompleteRequest {
    private String input;
    private String languageCode = "en";
    private String regionCode = "IN";
}

