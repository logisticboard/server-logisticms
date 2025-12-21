package com.example.logisticms.entity;

import com.example.logisticms.entity.enums.ShipmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tracking {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(updatable = false, nullable = false)
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;


    @NotBlank
    @Size(max = 100)
    private String heading;

    @NotBlank
    @Size(max = 500)
    private String description;

    @NotNull
    private LocalDateTime timestamp;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;
}