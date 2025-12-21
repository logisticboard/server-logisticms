package com.example.logisticms.entity;

import com.example.logisticms.entity.enums.ShipmentStatus;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shipments")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Shipment name is required")
    @Column(nullable = false)
    private String shipmentName;

    @NotBlank(message = "Shipment formal name is required")
    @Column(nullable = false)
    private String shipmentFormalName;

    @NotNull(message = "Pickup date is required")
    private LocalDateTime pickupDate;

    @NotNull(message = "Pickup location is required")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "pickup_latitude", nullable = false)),
            @AttributeOverride(name = "longitude", column = @Column(name = "pickup_longitude", nullable = false)),
            @AttributeOverride(name = "address", column = @Column(name = "pickup_address", nullable = false))
    })
    private Location pickupLocation;

    @NotNull(message = "Delivery location is required")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "delivery_latitude", nullable = false)),
            @AttributeOverride(name = "longitude", column = @Column(name = "delivery_longitude", nullable = false)),
            @AttributeOverride(name = "address", column = @Column(name = "delivery_address", nullable = false))
    })
    private Location deliveryLocation;

    @NotNull(message = "Shipment weight is required")
    @Positive(message = "Shipment weight must be positive")
    @Column(nullable = false)
    private Double shipmentWeight;

    @NotNull(message = "Estimated cost is required")
    @PositiveOrZero(message = "Estimated cost cannot be negative")
    @Column(nullable = false)
    private Double shipmentTotalEstimatedCost;

    @Size(max = 500, message = "Special instructions cannot exceed 500 characters")
    @Column(length = 500)
    private String shipmentSpecialInstructions;

    @NotNull(message = "Shipment status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus shipmentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id")
    private Truck truck;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleet_operator_id", nullable = false)
    private FleetOperator fleetOperator;

    @NotEmpty(message = "At least one contact detail is required")
    @ElementCollection
    @CollectionTable(
            name = "shipment_contacts",
            joinColumns = @JoinColumn(name = "shipment_id")
    )
    private List<@Valid ContactDetails> contactDetails;
}
