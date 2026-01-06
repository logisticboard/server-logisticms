package com.example.logisticms.service.impl;

import com.example.logisticms.dto.*;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.entity.FleetOperatorMember;
import com.example.logisticms.entity.FleetOperatorMemberId;
import com.example.logisticms.exception.NoResourceFoundException;
import com.example.logisticms.mapper.FleetOperatorMapper;
import com.example.logisticms.repository.FleetOperatorMemberRepository;
import com.example.logisticms.service.client.LoginMsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FleetOperatorRoleServiceImpl {
    private final FleetOperatorMemberRepository fleetOperatorMemberRepository;
    private final FleetOperatorServiceImpl fleetOperatorService;
    private final LoginMsClient loginMsClient;
    public void addMemberToFleetOperator(FleetOperator fleetOperator, FleetOperatorRolesEnum roleEnum, UUID userId) {
        FleetOperatorMemberId fleetOperatorMemberId = FleetOperatorMemberId.builder()
                .fleetOperatorId(fleetOperator.getId())
                .userId(userId)
                .build();
        fleetOperatorMemberRepository.save(FleetOperatorMember.builder()
                .role(roleEnum)
                .fleetOperator(fleetOperator)
                        .id(fleetOperatorMemberId)
                .build());
    }

    public List<FleetOperatorDto> getFleetOperatorsByUserId(UUID userId) {
        return fleetOperatorMemberRepository.findByIdUserId(userId)
                .stream()
                .map(member -> FleetOperatorMapper.toDto(member.getFleetOperator()))
                .toList();
    }

    public boolean isUserAdminOfFleetOperator(UUID companyId, UUID userId) {
        return fleetOperatorMemberRepository.existsById_FleetOperatorIdAndId_UserIdAndRole(
                companyId,
                userId,
                FleetOperatorRolesEnum.ADMIN
        );
    }


    public List<FleetOperatorMembersResponse> getFleetOperatorsMembers(UUID userId, UUID fleetOperatorId) {
        List<FleetOperatorMember> fleetOperatorMemberList =
                fleetOperatorMemberRepository.findByIdFleetOperatorId(fleetOperatorId);
        List<UserDetailsDto> userDetails = loginMsClient.getUserDetailsByIds(
                fleetOperatorMemberList.stream()
                        .map(member -> member.getId().getUserId())
                        .toList()
        );
        Map<UUID, FleetOperatorRolesEnum> userIdToRoleMap = fleetOperatorMemberList.stream()
                .collect(Collectors.toMap(
                        member -> member.getId().getUserId(),
                        FleetOperatorMember::getRole
                ));
        return userDetails
                .stream().map(member ->
                    FleetOperatorMembersResponse.builder()
                            .role(userIdToRoleMap.get(member.getId()))
                            .memberEmail(member.getEmail())
                            .memberName(member.getName())
                            .build()


                ).toList();
    }

    public void upsertFleetOperatorMemberData(UUID fleetOperatorId, MemberDataUpdateRequest data) {
        List<UserDetailsDto> userDetailsDtos = loginMsClient.getUserDetailsByEmailIds(
                List.of(data.getEmail())
        );
        FleetOperatorMember fleetOperatorMemberData = FleetOperatorMember.builder()
                .id(FleetOperatorMemberId.builder()
                        .fleetOperatorId(fleetOperatorId)
                        .userId(userDetailsDtos.getFirst().getId())
                        .build())
                .role(data.getRole())
                .fleetOperator(fleetOperatorService.getFleetOperatorById(fleetOperatorId))
                .build();
        fleetOperatorMemberRepository.save(fleetOperatorMemberData);
    }

    public boolean isUserMemberOfFleetOperator(UUID fleetOperatorId, UUID userId) {
        return fleetOperatorMemberRepository.existsById_FleetOperatorIdAndId_UserId(
                fleetOperatorId,
                userId
        );
    }

    public FleetOperatorMemberProfileResponse getFleetOperatorMemberProfileById(UUID memberId) {
        UserDetailsDto userDetails = loginMsClient.getUserDetailsByIds(
                List.of(memberId)
        ).getFirst();
        List<FleetOperatorMember> fleetOperatorMember = fleetOperatorMemberRepository.findByIdUserId(memberId);
        return FleetOperatorMemberProfileResponse.builder()
                .name(userDetails.getName())
                .email(userDetails.getEmail())
                .phone(userDetails.getPhone())
                .fleetOperatorData(
                        fleetOperatorMember
                                .stream()
                                .map((fleetOperatorDto)-> FleetOperatorMemberProfileResponse.FleetOperatorData.builder()
                                        .fleetOperatorName(fleetOperatorDto.getFleetOperator().getName())
                                        .fleetOperatorUid(fleetOperatorDto.getFleetOperator().getId())
                                        .role(fleetOperatorDto.getRole())
                                        .build()
                                )
                                .toList()
                )
                .build();
    }
}
