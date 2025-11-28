package com.example.logisticms.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Truck {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(updatable = false, nullable = false)
    @GeneratedValue
    private UUID id;

    private String registrationNumber;
    private String model;
    private Double capacity; // e.g., in tons

    @Enumerated(EnumType.STRING)
    private TruckStatus status; // AVAILABLE, ON_TRIP, UNDER_MAINTENANCE

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleet_operator_id")
    @JsonIgnore
    @ToString.Exclude
    private FleetOperator fleetOperator;

    @OneToMany(mappedBy = "truck", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<Driver> assignedDriver;

    @OneToMany(mappedBy = "truck", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Shipment> shipments;
}

