package com.example.logisticms.service.impl;

import com.example.logisticms.dto.FleetOperatorRolesEnum;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.entity.FleetOperatorMember;
import com.example.logisticms.entity.FleetOperatorMemberId;
import com.example.logisticms.repository.FleetOperatorMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FleetOperatorRoleServiceImpl {
    private final FleetOperatorMemberRepository fleetOperatorMemberRepository;
    public void addMemberToFleetOperator(FleetOperator fleetOperator, FleetOperatorRolesEnum roleEnum, UUID userId) {
        FleetOperatorMemberId fleetOperatorMemberId = FleetOperatorMemberId.builder()
                .fleetOperatorId(fleetOperator.getId())
                .userId(userId)
                .build();
        fleetOperatorMemberRepository.save(FleetOperatorMember.builder()
                .role(roleEnum)
                .fleetOperator(fleetOperator)
                        .id(fleetOperatorMemberId)
                .build());
    }
}
