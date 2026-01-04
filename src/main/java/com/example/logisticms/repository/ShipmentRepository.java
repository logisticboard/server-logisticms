package com.example.logisticms.repository;


import com.example.logisticms.entity.Shipment;
import com.example.logisticms.entity.enums.ShipmentStatus;
import com.example.logisticms.entity.view.PickupLocationView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {
    List<Shipment> findAllByFleetOperator_Id(UUID fleetOperatorId);

    Page<Shipment> findAllByFleetOperator_Id(UUID fleetOperatorId, Pageable pageable);

    Page<Shipment> findAllByFleetOperator_IdAndShipmentStatus(UUID fleetOperatorId, ShipmentStatus shipmentStatus, Pageable pageable);

    @Query("SELECT s.id AS shipmentId, s.pickupLocation AS pickupLocation " +
            "FROM Shipment s WHERE s.id IN :shipmentIds")
    List<PickupLocationView> findPickupLocationsByIds(List<UUID> shipmentIds);

    @Query("SELECT s.shipmentStatus as status, COUNT(s) as count FROM Shipment s WHERE s.fleetOperator.id = :fleetOperatorId GROUP BY s.shipmentStatus")
    List<Object[]> countByStatusForFleetOperator(UUID fleetOperatorId);

    long countByFleetOperator_Id(UUID fleetOperatorId);

    @Query("SELECT FUNCTION('DATE', s.createdAt) as date, s.shipmentStatus as status, COUNT(s) as count " +
            "FROM Shipment s WHERE s.fleetOperator.id = :fleetOperatorId AND s.createdAt >= :startDate AND s.createdAt <= :endDate " +
            "GROUP BY FUNCTION('DATE', s.createdAt), s.shipmentStatus ORDER BY date ASC")
    List<Object[]> countShipmentsByDateAndStatus(UUID fleetOperatorId, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
}