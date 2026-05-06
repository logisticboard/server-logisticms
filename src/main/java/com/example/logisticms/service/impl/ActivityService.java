package com.example.logisticms.service.impl;

import com.example.logisticms.dto.ActivityListResponse;
import com.example.logisticms.entity.Activity;
import com.example.logisticms.exception.UnauthorizedOperationException;
import com.example.logisticms.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final FleetOperatorRoleServiceImpl fleetOperatorRoleService;

    public List<ActivityListResponse> getActivityList(UUID userId, UUID fleetOperatorId) {
        if (!fleetOperatorRoleService.isUserMemberOfFleetOperator(fleetOperatorId, userId)) {
            throw new UnauthorizedOperationException("User is not authorized to activity view this Fleet Operator!");
        }
        return activityRepository.findAllByFleetOperatorId(fleetOperatorId).stream().map(
                        activity -> ActivityListResponse.builder()
                                .shipmentFormalName(activity.getShipment().getShipmentFormalName())
                                .shipmentName(activity.getShipment().getShipmentName())
                                .shipmentId(activity.getShipment().getId())
                                .dropLocation(activity.getShipment().getDeliveryLocation().getAddress())
                                .pickupLocation(activity.getShipment().getPickupLocation().getAddress())
                                .conversationId(activity.getConversationId())
                                .lastActivityAt(activity.getUpdatedAt())
                                .build())
                .toList();

    }

}
