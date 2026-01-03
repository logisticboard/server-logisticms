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
}