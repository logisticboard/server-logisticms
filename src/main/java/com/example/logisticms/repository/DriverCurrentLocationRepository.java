package com.example.logisticms.repository;

import com.example.logisticms.entity.DriverCurrentLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DriverCurrentLocationRepository extends JpaRepository<DriverCurrentLocation, Long> {
    List<DriverCurrentLocation> findByShipment_IdIn(List<UUID> shipmentIds);
}
