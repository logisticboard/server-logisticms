package com.example.logisticms.repository;

import com.example.logisticms.entity.FleetOperator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FleetOperatorRepository extends JpaRepository<FleetOperator, UUID> {
}
