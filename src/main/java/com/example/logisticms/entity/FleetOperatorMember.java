package com.example.logisticms.entity;


import com.example.logisticms.dto.FleetOperatorRolesEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "fleet_operator_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FleetOperatorMember {
//    TODO: Admin can add or remove users and assign roles.
    //TODO: MANAGER can only view and manage trucks and drivers.
    @EmbeddedId
    private FleetOperatorMemberId id;

    @ManyToOne
    @MapsId("fleetOperatorId")  // maps fleetOperatorId from composite key
    @JoinColumn(name = "fleet_operator_id")
    private FleetOperator fleetOperator;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FleetOperatorRolesEnum role;


}
