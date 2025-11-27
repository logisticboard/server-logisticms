package com.example.logisticms.repository;


import com.example.logisticms.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TruckRepository extends JpaRepository<Truck, UUID> {}