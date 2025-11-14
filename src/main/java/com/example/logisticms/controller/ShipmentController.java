package com.example.logisticms.controller;



import com.example.logisticms.entity.Shipment;
import com.example.logisticms.service.impl.ShipmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentServiceImpl shipmentService;

    @PostMapping
    public Shipment createShipment(@RequestBody Shipment shipment) {
        return shipmentService.createShipment(shipment);
    }

    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentService.getAllShipments();
    }

    @GetMapping("/{id}")
    public Shipment getShipmentById(@PathVariable Long id) {
        return shipmentService.getShipmentById(id);
    }

    @PutMapping("/{id}/status")
    public Shipment updateShipmentStatus(@PathVariable Long id, @RequestParam String status) {
        return shipmentService.updateStatus(id, status);
    }

    @PutMapping("/{id}/assign")
    public Shipment assignTruckAndDriver(@PathVariable Long id,
                                         @RequestParam Long truckId,
                                         @RequestParam Long driverId) {
        return shipmentService.assignTruckAndDriver(id, truckId, driverId);
    }

    @DeleteMapping("/{id}")
    public void deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
    }
}

