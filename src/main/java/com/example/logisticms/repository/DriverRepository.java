package com.example.logisticms.repository;

import com.example.logisticms.dto.DriverDto;
import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.FleetOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {
    List<Driver> findByFleetOperator(FleetOperator fleetOperator);

    @Query("SELECT d FROM Driver d WHERE d.fleetOperator.id = :fleetOperatorId AND d.truck IS NULL")
    List<Driver> findUnassignedDrivers(UUID fleetOperatorId);
}