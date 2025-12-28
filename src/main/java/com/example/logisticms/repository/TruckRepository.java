package com.example.logisticms.repository;


import com.example.logisticms.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TruckRepository extends JpaRepository<Truck, UUID> {
    @Query("SELECT t FROM Truck t WHERE t.fleetOperator.id = :fleetOperatorId")
    List<Truck> findUnassignedTrucks(UUID fleetOperatorId);

    Optional<Truck> findByIdAndFleetOperatorId(UUID truckId, UUID fleetOperatorId);
}