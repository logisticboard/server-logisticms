package com.example.logisticms.repository;

import com.example.logisticms.entity.FleetOperatorMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FleetOperatorMemberRepository extends JpaRepository<FleetOperatorMember, UUID> {
}
