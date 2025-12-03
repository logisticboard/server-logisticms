package com.example.logisticms.repository;


import com.example.logisticms.dto.ShipmentSummaryResponse;
import com.example.logisticms.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {
    List<Shipment> findAllByFleetOperator_Id(UUID fleetOperatorId);
}