package com.example.logisticms.repository;

import com.example.logisticms.dto.FleetOperatorRolesEnum;
import com.example.logisticms.entity.FleetOperatorMember;
import com.example.logisticms.entity.FleetOperatorMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FleetOperatorMemberRepository extends JpaRepository<FleetOperatorMember, FleetOperatorMemberId> {
    List<FleetOperatorMember> findByIdUserId(UUID userId);

    boolean existsById_FleetOperatorIdAndId_UserIdAndRole(UUID companyId, UUID userId, FleetOperatorRolesEnum fleetOperatorRolesEnum);

    List<FleetOperatorMember> findByIdFleetOperatorId(UUID fleetOperatorId);

    boolean existsById_FleetOperatorIdAndId_UserId(UUID fleetOperatorId, UUID userId);
}
