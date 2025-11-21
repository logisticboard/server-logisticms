package com.example.logisticms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import lombok.*;

import java.util.UUID;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FleetOperatorMemberId {
    private UUID userId;
    private UUID fleetOperatorId;
}
