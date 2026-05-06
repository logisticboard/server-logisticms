package com.example.logisticms.repository;

import com.example.logisticms.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    @Query("SELECT a FROM Activity a WHERE a.fleetOperator.id = :fleetOperatorId")
    List<Activity> findAllByFleetOperatorId(UUID fleetOperatorId);
}
