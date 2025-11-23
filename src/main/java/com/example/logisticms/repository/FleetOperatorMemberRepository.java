package com.example.logisticms.repository;

import com.example.logisticms.dto.FleetOperatorRolesEnum;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.entity.FleetOperatorMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FleetOperatorMemberRepository extends JpaRepository<FleetOperatorMember, UUID> {
    List<FleetOperatorMember> findByIdUserId(UUID userId);

    boolean existsById_FleetOperatorIdAndId_UserIdAndRole(UUID companyId, UUID userId, FleetOperatorRolesEnum fleetOperatorRolesEnum);
}
