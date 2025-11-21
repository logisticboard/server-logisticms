package com.example.logisticms.mapper;

import com.example.logisticms.dto.FleetOperatorRolesEnum;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.entity.FleetOperatorMember;
import com.example.logisticms.entity.FleetOperatorMemberId;

import java.util.UUID;

public class FleetOperatorRoleMapper {
    public static FleetOperatorMember toEntity(UUID userId, FleetOperatorRolesEnum role, FleetOperator fleetOperator) {
        FleetOperatorMemberId fleetOperatorMemberId = FleetOperatorMemberId.builder()
                .userId(userId)
                .fleetOperatorId(fleetOperator.getId())
                .build();
        FleetOperatorMember fleetOperatorMember = new FleetOperatorMember();
        fleetOperatorMember.setId(fleetOperatorMemberId);
        fleetOperatorMember.setRole(role);
        return fleetOperatorMember;
    }
}
