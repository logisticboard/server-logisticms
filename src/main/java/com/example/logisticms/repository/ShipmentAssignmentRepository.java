package com.example.logisticms.repository;

import com.example.logisticms.entity.Shipment;
import com.example.logisticms.entity.enums.ShipmentAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ShipmentAssignmentRepository extends JpaRepository<ShipmentAssignment, UUID> {

    @Query("""
        SELECT sa.shipment
        FROM ShipmentAssignment sa
        JOIN sa.driver d
        WHERE d.phoneNumber = :phoneNumber
    """)
    List<ShipmentAssignment> findShipmentsByDriverPhone(@Param("phoneNumber") String phoneNumber);
}
