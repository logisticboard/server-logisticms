package com.example.logisticms.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Truck {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String registrationNumber;
    private String model;
    private Double capacity; // e.g., in tons

    @Enumerated(EnumType.STRING)
    private TruckStatus status; // AVAILABLE, ON_TRIP, UNDER_MAINTENANCE

    @OneToMany
    @JoinColumn(name = "driver_id")
    private List<Driver> assignedDriver;

    @OneToMany(mappedBy = "truck", cascade = CascadeType.ALL)
    private List<Shipment> shipments;
}

