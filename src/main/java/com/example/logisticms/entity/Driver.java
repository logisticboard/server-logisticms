package com.example.logisticms.entity;



import jakarta.persistence.*;
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
public class Driver extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
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
}

