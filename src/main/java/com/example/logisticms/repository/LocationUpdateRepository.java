package com.example.logisticms.repository;

import com.example.logisticms.entity.LocationUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationUpdateRepository extends JpaRepository<LocationUpdate, Long> {
    List<LocationUpdate> findByShipmentIdOrderByTimestampDesc(Long shipmentId);
}