package com.example.logisticms.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsDto {
    private String email;
    private UUID id;
    private String name;
    private String phone;
    private String city;
}
