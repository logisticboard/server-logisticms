package com.example.logisticms.repository;

import com.example.logisticms.entity.Shipment;
import com.example.logisticms.entity.enums.ShipmentAssignment;
import com.example.logisticms.entity.enums.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ShipmentAssignmentRepository extends JpaRepository<ShipmentAssignment, UUID> {

    @Query("""
        SELECT sa
        FROM ShipmentAssignment sa
        JOIN sa.driver d
        JOIN sa.shipment s
        WHERE d.phoneNumber = :phoneNumber
    """)
    Page<ShipmentAssignment> findShipmentsByDriverPhone(@Param("phoneNumber") String phoneNumber, Pageable pageable);

    @Query("""
        SELECT sa
        FROM ShipmentAssignment sa
        JOIN sa.driver d
        JOIN sa.shipment s
        WHERE d.phoneNumber = :phoneNumber AND s.shipmentStatus = :shipmentStatus
    """)
    Page<ShipmentAssignment> findByDriverPhoneAndShipmentStatus(@Param("phoneNumber") String phoneNumber, @Param("shipmentStatus") ShipmentStatus shipmentStatus, Pageable pageable);
}
