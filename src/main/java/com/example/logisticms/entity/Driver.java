package com.example.logisticms.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Driver extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(updatable = false, nullable = false)
    @GeneratedValue
    private UUID id;

    private String name;

    @NotNull(message = "Phone number is required")
    @Column(nullable = false)
    private String phoneNumber;

    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    private DriverStatus status; // AVAILABLE, ON_TRIP, OFF_DUTY

    @Column(precision = 9, scale = 6)
    private BigDecimal currentLat;

    @Column(precision = 9, scale = 6)
    private BigDecimal currentLon;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private FleetOperator fleetOperator;
}

