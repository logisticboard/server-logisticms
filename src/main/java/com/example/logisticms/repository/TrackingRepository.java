package com.example.logisticms.repository;

import com.example.logisticms.dto.TrackingDto;
import com.example.logisticms.entity.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;

public interface TrackingRepository  extends JpaRepository<Tracking, UUID> {
    List<Tracking> findByShipment_IdOrderByTimestampAsc(UUID shipmentId);
}
