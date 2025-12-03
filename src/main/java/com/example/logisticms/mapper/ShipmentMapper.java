package com.example.logisticms.mapper;

import com.example.logisticms.dto.ShipmentCreateRequest;
import com.example.logisticms.dto.ShipmentCreateResponse;
import com.example.logisticms.dto.ShipmentSummaryResponse;
import com.example.logisticms.entity.*;
import com.example.logisticms.entity.enums.ShipmentStatus;

import java.util.List;

public class ShipmentMapper {
    private ShipmentMapper() {
        // Private constructor to prevent instantiation
    }
    public static Shipment toEntity(ShipmentCreateRequest request, Truck truck, ShipmentStatus status, FleetOperator fleetOperator) {
        Location locationPickup = toEntityLocation(request.getPickupLocation());
        Location locationDelivery = toEntityLocation(request.getDeliveryLocation());
        List<ContactDetails> contactDetailsList = toEntityContactList(request.getContactDetails());
        return Shipment.builder()
                .shipmentName(request.getShipmentName())
                .pickupDate(request.getPickupDate())
                .pickupLocation(locationPickup)
                .deliveryLocation(locationDelivery)
                .shipmentWeight(request.getShipmentWeight())
                .shipmentTotalEstimatedCost(request.getShipmentTotalEstimatedCost())
                .truck(truck)
                .fleetOperator(fleetOperator)
                .shipmentStatus(status)
                .shipmentSpecialInstructions(request.getShipmentSpecialInstructions())
                .contactDetails(contactDetailsList)
                .build();
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
                .truckId(shipmentResponse.getTruck().getId().toString())
                .shipmentStatus(shipmentResponse.getShipmentStatus())
                .shipmentSpecialInstructions(shipmentResponse.getShipmentSpecialInstructions())
                .contactDetails(toDtoContactList(shipmentResponse.getContactDetails()))
                .build();
    }

    public static ShipmentSummaryResponse.Shipment toShipmentSummaryShipmentResponse(Shipment shipment) {
        return ShipmentSummaryResponse.Shipment.builder()
                .shipmentId(shipment.getId())
                .shipmentName(shipment.getShipmentName())
                .pickupLocationAddress(shipment.getPickupLocation().getAddress())
                .deliveryLocationAddress(shipment.getDeliveryLocation().getAddress())
                .shipmentPickupDate(shipment.getPickupDate())
                .shipmentStatus(shipment.getShipmentStatus())
                .shipmentWeight(shipment.getShipmentWeight())
                .shipmentCarriedBy(shipment.getTruck().getRegistrationNumber())
                .build();
    }

}
