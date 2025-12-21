package com.example.logisticms.dto;


import com.example.logisticms.entity.enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingDto {

        private String heading;
        private String description;
        private LocalDateTime timestamp;
        private ShipmentStatus shipmentStatus;
}
