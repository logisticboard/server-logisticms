package com.example.logisticms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FleetOperatorMemberProfileResponse {
    private String name;
    private String email;
    private String phone;
    private List<FleetOperatorData> fleetOperatorData;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FleetOperatorData{
        private String fleetOperatorName;
        private UUID fleetOperatorUid;
        private FleetOperatorRolesEnum role;
        private String companyName;
        private String imageUrl;
        private String email;
        private String phoneNumber;
        private String address;
        private String gstNumber;
        private String description;
    }
}
