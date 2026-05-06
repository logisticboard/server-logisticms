package com.example.logisticms.service.impl;

import com.example.logisticms.dto.*;
import com.example.logisticms.entity.Activity;
import com.example.logisticms.exception.NoResourceFoundException;
import com.example.logisticms.repository.ActivityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogisticsAiQueryService {
    private final ShipmentServiceImpl shipmentService;
    private final TrackingServiceImpl trackingService;
    private final FleetOperatorServiceImpl fleetOperatorService;
    private final ActivityRepository activityRepository;

    public String fetchContext(LogisticsActivityAiContextRequest request) {
        if (request.getIntentList() == null || request.getIntentList().isEmpty()) {
            throw new NoResourceFoundException("Intent is required to fetch AI context");
        }
        Activity activity = activityRepository
                 .findByConversationId(request.getConversationId())
                 .orElseThrow(() -> new NoResourceFoundException("No Activity Data present")
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<Object> resolveIntent = request.getIntentList().stream()
                .map(intent -> this.handleIntent(intent, activity.getShipment().getId(), activity.getFleetOperator().getId()))
                .toList();
        try {
            return mapper.writeValueAsString(resolveIntent);
        } catch (JsonProcessingException e) {
            return null;
        }

    }


    private Object handleIntent(String intent, UUID shipmentId, UUID fleetOperatorId){
        return switch (intent) {
            case "GET_SHIPMENT_DETAILS" -> shipmentDetailsContext(shipmentId);
            case "GET_SHIPMENT_TRACKING" -> shipmentTrackingContext(shipmentId);
            case "GET_TRUCK_LIST" -> truckListContext(fleetOperatorId);
            case "GET_DRIVER_LIST" -> driverListContext(fleetOperatorId);
            default -> null;
        };
    }

    private ShipmentSummaryResponse shipmentListContext(UUID fleetOperatorId) {
        return shipmentService.getAllShipmentsSummary(fleetOperatorId, null, 0, 100);

    }

    private ShipmentDetailsResponse shipmentDetailsContext(UUID shipmentId) {
        return shipmentService.getShipmentDetails(shipmentId);
    }

    private List<TrackingDto> shipmentTrackingContext(UUID shipmentId) {
        return trackingService.getShipmentTracking(shipmentId);
    }

    private ShipmentOverviewResponse shipmentOverviewContext(UUID fleetOperatorId) {
        return shipmentService.getShipmentOverview(fleetOperatorId);
    }

    private List<TruckDto> truckListContext(UUID fleetOperatorId) {
        return fleetOperatorService.getTrucksByFleetOperator(fleetOperatorId);
    }

    private List<DriverDto> driverListContext(UUID fleetOperatorId) {
        return fleetOperatorService.getDriversByFleetOperator(fleetOperatorId);
    }
}
