package com.example.logisticms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FleetOperatorMembersResponse {
    private String memberName;
    private String memberEmail;
    private FleetOperatorRolesEnum role;
}
