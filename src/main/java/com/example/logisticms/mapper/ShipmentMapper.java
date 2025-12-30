package com.example.logisticms.mapper;

import com.example.logisticms.dto.*;
import com.example.logisticms.entity.*;
import com.example.logisticms.entity.enums.ShipmentStatus;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ShipmentMapper {
    private ShipmentMapper() {
        // Private constructor to prevent instantiation
    }
    public static Shipment toEntity(ShipmentCreateRequest request, ShipmentStatus status, FleetOperator fleetOperator) {
        Location locationPickup = toEntityLocation(request.getPickupLocation());
        Location locationDelivery = toEntityLocation(request.getDeliveryLocation());
        List<ContactDetails> contactDetailsList = toEntityContactList(request.getContactDetails());
        return Shipment.builder()
                .shipmentName(request.getShipmentName())
                .shipmentFormalName(generateShipmentCreateFormalNameCreate(fleetOperator.getName()))
                .pickupDate(request.getPickupDate())
                .pickupLocation(locationPickup)
                .deliveryLocation(locationDelivery)
                .shipmentWeight(request.getShipmentWeight())
                .shipmentTotalEstimatedCost(request.getShipmentTotalEstimatedCost())
                .fleetOperator(fleetOperator)
                .shipmentStatus(status)
                .shipmentSpecialInstructions(request.getShipmentSpecialInstructions())
                .contactDetails(contactDetailsList)
                .build();
    }

    public static String generateShipmentCreateFormalNameCreate(String fleetOperatorName) {
        String code;
        if (fleetOperatorName == null || fleetOperatorName.trim().isEmpty()) {
            code = "AAAA";
        } else {
            String trimmed = fleetOperatorName.trim().replaceAll("\\s+", "");
            code = trimmed.length() >= 4
                    ? trimmed.substring(0, 4).toUpperCase()
                    : String.format("%-4s", trimmed).replace(' ', 'A').toUpperCase();
        }

        long millis = System.currentTimeMillis();

        return "#" + code + "-" + millis;
    }


    private static List<ContactDetails> toEntityContactList(List<ShipmentCreateRequest.ContactDetailsRequest> request) {
        return request.stream()
                .map(cd -> ContactDetails.builder()
                        .name(cd.getName())
                        .role(cd.getRole())
                        .email(cd.getEmail())
                        .phoneNumber(cd.getPhoneNumber())
                        .build())
                .toList();
    }

    public static List<ShipmentCreateRequest.ContactDetailsRequest> toDtoContactList(List<ContactDetails> contactDetails) {
        return contactDetails.stream()
                .map(cd -> ShipmentCreateRequest.ContactDetailsRequest.builder()
                        .name(cd.getName())
                        .role(cd.getRole())
                        .email(cd.getEmail())
                        .phoneNumber(cd.getPhoneNumber())
                        .build())
                .toList();
    }

    private static Location toEntityLocation(ShipmentCreateRequest.Location request) {
        return Location.builder()
                .address(request.getAddress())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .build();
    }

    public static ShipmentCreateResponse toShipmentCreateResponse(Shipment shipmentResponse) {
        return ShipmentCreateResponse.builder()
                .shipmentId(shipmentResponse.getId())
                .shipmentName(shipmentResponse.getShipmentName())
                .pickupDate(shipmentResponse.getPickupDate())
                .pickupLocation(shipmentResponse.getPickupLocation().getAddress())
                .deliveryLocation(shipmentResponse.getDeliveryLocation().getAddress())
                .shipmentWeight(shipmentResponse.getShipmentWeight())
                .shipmentTotalEstimatedCost(shipmentResponse.getShipmentTotalEstimatedCost())
                .shipmentStatus(shipmentResponse.getShipmentStatus())
                .shipmentSpecialInstructions(shipmentResponse.getShipmentSpecialInstructions())
                .contactDetails(toDtoContactList(shipmentResponse.getContactDetails()))
                .build();
    }

    public static ShipmentSummaryResponse.Shipment toShipmentSummaryShipmentResponse(Shipment shipment) {
        return ShipmentSummaryResponse.Shipment.builder()
                .shipmentId(shipment.getId())
                .shipmentName(shipment.getShipmentName())
                .shipmentFormalName(shipment.getShipmentFormalName())
                .pickupLocationAddress(shipment.getPickupLocation().getAddress())
                .deliveryLocationAddress(shipment.getDeliveryLocation().getAddress())
                .shipmentPickupDate(shipment.getPickupDate())
                .shipmentStatus(shipment.getShipmentStatus())
                .shipmentWeight(shipment.getShipmentWeight())
                .build();
    }

    public static DriverShipment toDriverShipmentDto(Shipment shipment) {
        return DriverShipment.builder()
                .shipmentUid(shipment.getId())
                .shipmentId(shipment.getShipmentFormalName())
                .shipmentName(shipment.getShipmentName())
                .pickupLocationAddress(shipment.getPickupLocation().getAddress())
                .deliveryLocationAddress(shipment.getDeliveryLocation().getAddress())
                .shipmentPickupDate(shipment.getPickupDate())
                .shipmentStatus(shipment.getShipmentStatus())
                .shipmentWeight(shipment.getShipmentWeight())
                .shipmentSpecialInstructions(shipment.getShipmentSpecialInstructions())
                .contactDetails(toDtoContactList(shipment.getContactDetails()))
                .fleetOperatorName(shipment.getFleetOperator().getName())
                .build();
    }

    public static ShipmentDetailsResponse toShipmentDetailsResponse(Shipment shipment) {
        Map<Truck, List<Driver>> truckListMap= new HashMap<>();
        shipment.getAssignments().forEach(assignment -> {
            truckListMap.computeIfAbsent(assignment.getTruck(), k -> new java.util.ArrayList<>()).add(assignment.getDriver());
        });
        ShipmentDetailsResponse shipmentDetailsResponse = ShipmentDetailsResponse.builder()
                .id(shipment.getId())
                .shipmentName(shipment.getShipmentName())
                .shipmentFormalName(shipment.getShipmentFormalName())
                .pickupDate(shipment.getPickupDate())
                .pickupLocation(shipment.getPickupLocation().getAddress())
                .deliveryLocation(shipment.getDeliveryLocation().getAddress())
                .shipmentWeight(shipment.getShipmentWeight())
                .shipmentTotalEstimatedCost(shipment.getShipmentTotalEstimatedCost())
                .shipmentSpecialInstructions(shipment.getShipmentSpecialInstructions())
                .shipmentStatus(shipment.getShipmentStatus())
                .fleetOperatorName(shipment.getFleetOperator().getName())
                .assignments(truckListMap.entrySet().stream()
                        .map(entry -> ShipmentDetailsResponse.ShipmentAssignmentData.builder()
                                .truckUid(entry.getKey().getId().toString())
                                .truckRegistrationNumber(entry.getKey().getRegistrationNumber())
                                .truckModel(entry.getKey().getModel())
                                .truckCapacity(entry.getKey().getCapacity())
                                .drivers(entry.getValue().stream()
                                        .filter(Objects::nonNull)
                                        .map(driver -> ShipmentDetailsResponse.DriverData.builder()
                                                .driverUid(driver.getId().toString())
                                                .driverFullName(driver.getName())
                                                .driverLicenseNumber(driver.getLicenseNumber())
                                                .driverContactNumber(driver.getPhoneNumber())
                                                .build())
                                        .toList())
                                .build())
                        .toList())
                .contactDetails(shipment.getContactDetails().stream()
                        .map(cd -> ShipmentDetailsResponse.ContactDetailsData.builder()
                                .contactName(cd.getName())
                                .contactEmail(cd.getEmail())
                                .contactPhone(cd.getPhoneNumber())
                                .contactRole(cd.getRole())
                                .build())
                        .toList())
                .build();
        return shipmentDetailsResponse;
    }
}
