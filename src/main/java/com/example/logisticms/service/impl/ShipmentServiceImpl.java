package com.example.logisticms.service.impl;


import com.example.logisticms.dto.*;
import com.example.logisticms.entity.*;
import com.example.logisticms.entity.enums.ShipmentAssignment;
import com.example.logisticms.entity.enums.ShipmentStatus;
import com.example.logisticms.exception.NoResourceFoundException;
import com.example.logisticms.mapper.ShipmentMapper;
import com.example.logisticms.repository.ActivityRepository;
import com.example.logisticms.repository.DriverCurrentLocationRepository;
import com.example.logisticms.repository.ShipmentRepository;
import com.example.logisticms.repository.TrackingRepository;
import com.example.logisticms.service.client.CommuncationMsClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl {

    private final ShipmentRepository shipmentRepository;
    private final TrackingRepository trackingRepository;
    private final DriverCurrentLocationRepository driverCurrentLocationRepository;
    private final GeocodingService geocodingService;
    private final CommuncationMsClient communcationMsClient;
    private final ActivityRepository activityRepository;


    public Shipment createShipment(ShipmentCreateRequest shipment, FleetOperator fleetOperator, HttpServletRequest httpServletRequest) {
        GeoResult deliveryLocationGeoResult = geocodingService.geocode(shipment.getDeliveryLocation().getAddress());
        GeoResult pickupLocationGeoResult = geocodingService.geocode(shipment.getPickupLocation().getAddress());
        shipment.getDeliveryLocation().setLatitude(deliveryLocationGeoResult.getLat());
        shipment.getDeliveryLocation().setLongitude(deliveryLocationGeoResult.getLng());
        shipment.getPickupLocation().setLongitude(pickupLocationGeoResult.getLng());
        shipment.getPickupLocation().setLatitude(pickupLocationGeoResult.getLat());

        ShipmentStatus shipmentStatus = ShipmentStatus.CREATED;
        Shipment toSave = ShipmentMapper.toEntity(shipment, shipmentStatus, fleetOperator);
        List<ShipmentAssignment> shipmentAssignments = new ArrayList<>();
        if (shipment.getShippers() != null && !shipment.getShippers().isEmpty()) {
            boolean isDriverAssignedToAllShippers = true;
            for (ShipmentCreateRequest.ShipperDataDto shipper : shipment.getShippers()) {
                if (shipper.getDriverUids().isEmpty()) {
                    isDriverAssignedToAllShippers = false;
                    shipmentAssignments.add(ShipmentAssignment.builder()
                            .truck(Truck.builder().id(shipper.getTruckUid()).build())
                            .shipment(toSave)
                            .build());
                }
                for (UUID driverUid : shipper.getDriverUids()) {
                    shipmentAssignments.add(ShipmentAssignment.builder()
                            .truck(Truck.builder().id(shipper.getTruckUid()).build())
                            .driver(Driver.builder().id(driverUid).build())
                            .shipment(toSave)
                            .build());
                }
            }
            toSave.setAssignments(shipmentAssignments);
            shipmentStatus = isDriverAssignedToAllShippers ? ShipmentStatus.READY_FOR_DISPATCH : ShipmentStatus.TRUCK_ASSIGNED;
        }
        toSave.setShipmentStatus(shipmentStatus);
        Shipment shipmentSavedEntity = shipmentRepository.save(toSave);
        trackingRepository.save(Tracking.builder()
                .shipment(shipmentSavedEntity)
                .heading("Shipment Created")
                .description("Shipment has been created.")
                .timestamp(shipmentSavedEntity.getCreatedAt())
                .shipmentStatus(shipmentStatus)
                .build());

        List<CreateActivityConversationRequest.ParticipantInfo> participantInfos =
                fleetOperator.getMembers().stream().map(
                        fleetOperatorMember -> CreateActivityConversationRequest.ParticipantInfo.builder()
                                .userId(fleetOperatorMember.getId().getUserId())
                                .role(FleetOperatorRolesEnum.ADMIN.equals(fleetOperatorMember.getRole()) ? ParticipantRole.ADMIN : ParticipantRole.MEMBER)
                                .build()
                ).toList();
        CreateActivityConversationRequest createActivityConversationRequest = CreateActivityConversationRequest.builder()
                .conversationType(ConversationType.ACTIVITY)
                .participantIds(participantInfos)
                .name("Activity Chat")
                .build();
        ConversationResponse conversationResponse = communcationMsClient.createActivityConversation(
                createActivityConversationRequest,
                httpServletRequest
        );
        Activity activity = Activity.builder()
                .conversationId(conversationResponse.getId())
                .shipment(shipmentSavedEntity)
                .fleetOperator(fleetOperator)
                .build();
        activityRepository.save(activity);

        return shipmentSavedEntity;
    }

    public ShipmentSummaryResponse getAllShipmentsSummary(UUID fleetOperatorId) {
        List<ShipmentSummaryResponse.Shipment> shipments = shipmentRepository.findAllByFleetOperator_Id(fleetOperatorId).stream()
                .map(ShipmentMapper::toShipmentSummaryShipmentResponse)
                .toList();
        return ShipmentSummaryResponse.builder()
                .shipments(shipments)
                .totalShipments(shipments.size())
                .inTransit((int) shipments.stream().filter(s -> s.getShipmentStatus() == ShipmentStatus.IN_TRANSIT).count())
                .delivered((int) shipments.stream().filter(s -> s.getShipmentStatus() == ShipmentStatus.DELIVERED).count())
                .pending((int) shipments.stream().filter(s -> s.getShipmentStatus() == ShipmentStatus.CREATED || s.getShipmentStatus() == ShipmentStatus.TRUCK_ASSIGNED || s.getShipmentStatus() == ShipmentStatus.READY_FOR_DISPATCH).count())
                .build();
    }

    public ShipmentSummaryResponse getAllShipmentsSummary(UUID fleetOperatorId, ShipmentStatus shipmentStatus, int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Shipment> shipmentPage;
        if (shipmentStatus != null) {
            shipmentPage = shipmentRepository.findAllByFleetOperator_IdAndShipmentStatus(fleetOperatorId, shipmentStatus, pageable);
        } else {
            shipmentPage = shipmentRepository.findAllByFleetOperator_Id(fleetOperatorId, pageable);
        }
        List<ShipmentSummaryResponse.Shipment> shipments = shipmentPage.getContent().stream()
                .map(ShipmentMapper::toShipmentSummaryShipmentResponse)
                .toList();
        return ShipmentSummaryResponse.builder()
                .shipments(shipments)
                .totalShipments((int) shipmentPage.getTotalElements())
                .inTransit((int) shipmentPage.getContent().stream().filter(s -> s.getShipmentStatus() == ShipmentStatus.IN_TRANSIT).count())
                .delivered((int) shipmentPage.getContent().stream().filter(s -> s.getShipmentStatus() == ShipmentStatus.DELIVERED).count())
                .pending((int) shipmentPage.getContent().stream().filter(s -> s.getShipmentStatus() == ShipmentStatus.CREATED || s.getShipmentStatus() == ShipmentStatus.TRUCK_ASSIGNED || s.getShipmentStatus() == ShipmentStatus.READY_FOR_DISPATCH).count())
                .build();
    }

    public void updateShipmentStatus(ShipmentStatus shipmentStatus, UUID shipmentId) {
        Shipment shipmentSavedEntity = shipmentRepository.findById(shipmentId).orElseThrow(() -> new NoResourceFoundException("Shipment not found with ID: " + shipmentId));
        shipmentSavedEntity.setShipmentStatus(shipmentStatus);
        shipmentRepository.save(shipmentSavedEntity);
        trackingRepository.save(Tracking.builder()
                .shipment(shipmentSavedEntity)
                .heading(ShipmentStatusUtil.getHeading(shipmentStatus))
                .description(ShipmentStatusUtil.getDescription(shipmentStatus))
                .timestamp(shipmentSavedEntity.getCreatedAt())
                .shipmentStatus(shipmentStatus)
                .build());
    }

    public ShipmentDetailsResponse getShipmentDetails(UUID shipmentId) {
        return shipmentRepository.findById(shipmentId)
                .map(ShipmentMapper::toShipmentDetailsResponse)
                .orElseThrow(() -> new NoResourceFoundException("Shipment not found with ID: " + shipmentId));
    }


    public void updateShipment(ShipmentUpdateRequest request, UUID shipmentId) {
        Shipment savedEntity = shipmentRepository.findById(shipmentId).orElseThrow(() -> new NoResourceFoundException("Shipment not found with ID: " + shipmentId));
        savedEntity.getAssignments().clear();
        for (ShipmentCreateRequest.ShipperDataDto shipper : request.getShippers()) {
            if (shipper.getDriverUids().isEmpty())
                savedEntity.getAssignments().add(ShipmentAssignment.builder()
                        .truck(Truck.builder().id(shipper.getTruckUid()).build())
                        .shipment(savedEntity)
                        .build());
            ;
            for (UUID driverUid : shipper.getDriverUids()) {
                savedEntity.getAssignments().add(ShipmentAssignment.builder()
                        .truck(Truck.builder().id(shipper.getTruckUid()).build())
                        .driver(Driver.builder().id(driverUid).build())
                        .shipment(savedEntity)
                        .build());
            }
        }
        savedEntity.setContactDetails(request.getContactDetails().stream()
                .map(contact -> ContactDetails.builder()
                        .name(contact.getName())
                        .email(contact.getEmail())
                        .phoneNumber(contact.getPhoneNumber())
                        .role(contact.getRole())
                        .build())
                .collect(Collectors.toList()));
        shipmentRepository.save(savedEntity);
    }

    public Map<UUID, CoordinateDTO> getDriverCurrentLocationsForShipments(List<UUID> shipmentIds) {

        Map<UUID, CoordinateDTO> coordinateDTOMap = driverCurrentLocationRepository.findByShipment_IdIn(shipmentIds).stream()
                .collect(Collectors.toMap(
                        loc -> loc.getShipment().getId(),
                        loc -> CoordinateDTO.builder()
                                .latitude(loc.getLatitude())
                                .longitude(loc.getLongitude())
                                .build()
                ));

        List<UUID> shipmentWithNoDriverLocation = shipmentIds.stream()
                .filter(id -> !coordinateDTOMap.containsKey(id))
                .toList();
        shipmentRepository.findPickupLocationsByIds(shipmentWithNoDriverLocation).forEach(pickupLocationView -> {
            Location pickupLocation = pickupLocationView.getPickupLocation();
            coordinateDTOMap.put(pickupLocationView.getShipmentId(), CoordinateDTO.builder()
                    .latitude(pickupLocation.getLatitude())
                    .longitude(pickupLocation.getLongitude())
                    .build());
        });
        return coordinateDTOMap;
    }

    public ShipmentOverviewResponse getShipmentOverview(UUID fleetOperatorId) {
        List<Object[]> statusCounts = shipmentRepository.countByStatusForFleetOperator(fleetOperatorId);
        Map<String, Long> overview = statusCounts.stream()
                .collect(Collectors.toMap(
                        arr -> arr[0].toString(),
                        arr -> (Long) arr[1]
                ));
        long total = statusCounts.stream()
                .mapToLong(arr -> (Long) arr[1])
                .sum();
        return ShipmentOverviewResponse.builder()
                .shipmentOverview(overview)
                .totalShipments(total)
                .build();
    }

    public org.springframework.data.domain.Page<TransactionHistoryItemDto> getTransactionHistory(UUID fleetOperatorId, int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by("createdAt").descending());
        org.springframework.data.domain.Page<Shipment> shipmentPage = shipmentRepository.findAllByFleetOperator_Id(fleetOperatorId, pageable);
        return shipmentPage.map(s -> TransactionHistoryItemDto.builder()
                .shipmentId(s.getId())
                .shipmentName(s.getShipmentName())
                .shipmentFormalName(s.getShipmentFormalName())
                .date(s.getCreatedAt())
                .totalEstimatedCost(s.getShipmentTotalEstimatedCost() == null ? null : java.math.BigDecimal.valueOf(s.getShipmentTotalEstimatedCost()))
                .build());
    }
}
