package com.example.logisticms.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDataUpdateRequest {
    @Email
    private String email;
    private FleetOperatorRolesEnum role;
}
