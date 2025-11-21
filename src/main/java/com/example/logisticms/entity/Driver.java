package com.example.logisticms.entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString
public class Driver extends BaseEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String name;

    private String phoneNumber;

    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    private DriverStatus status; // AVAILABLE, ON_TRIP, OFF_DUTY

    private Double currentLat;
    private Double currentLon;

    @ManyToOne
    private Truck truck;

    @ManyToOne
    private FleetOperator fleetOperator;
}

