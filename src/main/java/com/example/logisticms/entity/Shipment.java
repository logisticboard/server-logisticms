package com.example.logisticms.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pickupLocation;
    private String dropLocation;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status; // CREATED, IN_TRANSIT, DELIVERED, CANCELLED

    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime actualDeliveryDate;

    @ManyToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    private String customerName;
    private String customerPhone;
}

