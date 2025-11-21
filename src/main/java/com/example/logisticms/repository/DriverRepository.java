package com.example.logisticms.repository;

import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.FleetOperator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {
    List<Driver> findByFleetOperator(FleetOperator fleetOperator);
}