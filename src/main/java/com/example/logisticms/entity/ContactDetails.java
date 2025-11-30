package com.example.logisticms.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDetails {

    private String name;

    private String role;

    private String email;

    private String phoneNumber;
}
