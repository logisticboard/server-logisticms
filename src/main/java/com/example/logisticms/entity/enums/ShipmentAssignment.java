package com.example.logisticms.entity.enums;


import com.example.logisticms.entity.BaseEntity;
import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.Shipment;
import com.example.logisticms.entity.Truck;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "shipment_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentAssignment extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false)
    private Truck truck;

    // Optional but VERY useful later
    private LocalDateTime completedAt;
}
