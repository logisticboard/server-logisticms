package com.example.logisticms.repository;


import com.example.logisticms.dto.ShipmentSummaryResponse;
import com.example.logisticms.entity.Shipment;
import com.example.logisticms.entity.Truck;
import com.example.logisticms.entity.enums.ShipmentStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {
    List<Shipment> findAllByFleetOperator_Id(UUID fleetOperatorId);

    List<Shipment> findByTruck(Truck truck);
    @Modifying
    @Transactional
    @Query("""
    UPDATE Shipment s
    SET s.shipmentStatus = :status
    WHERE s.id = :shipmentId
""")
    int updateShipmentStatusById(UUID shipmentId, ShipmentStatus status);

    @Query("""
        SELECT s FROM Shipment s
        WHERE s.truck.id = :truckId
        AND (:status IS NULL OR s.shipmentStatus = :status)
    """)
    Page<Shipment> findByTruckAndStatus(
            @Param("truckId") UUID truckId,
            @Param("status") ShipmentStatus status,
            Pageable pageable
    );

}