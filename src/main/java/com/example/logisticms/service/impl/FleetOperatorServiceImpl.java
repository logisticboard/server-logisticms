package com.example.logisticms.service.impl;

import com.example.logisticms.dto.FleetOperatorDto;
import com.example.logisticms.dto.FleetOperatorRoleCreate;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.exception.NoResourceFoundException;
import com.example.logisticms.mapper.FleetOperatorMapper;
import com.example.logisticms.repository.FleetOperatorRepository;
import com.example.logisticms.repository.FleetOperatorMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FleetOperatorServiceImpl {
    private final FleetOperatorMemberRepository fleetOperatorMemberRepository;
    private final FleetOperatorRepository fleetOperatorRepository;
    public FleetOperator getFleetOperatorById(UUID userId) {
        return fleetOperatorMemberRepository.findById(userId)
                .orElseThrow(()-> new NoResourceFoundException("Fleet Operator not found for given user"))
                .getFleetOperator();
    }

    public void createFleetOperatorRoles(List<FleetOperatorRoleCreate> fleetOperatorRoleCreateList) {
//        TODO: call loginms for user id corresponding to email
//        TODO: send email notification to user asking to confirm for assigned with new roles and ask in frontend side to confirm
//        fleetOperatorRoleRepository.saveAll(
//                fleetOperatorRoleCreateList.stream()
//                        .map(roleCreate-> FleetOperatorRoleMapper.toEntity(roleCreate.userId(), roleCreate.role()))
//                        .toList()
//        );
    }

    public FleetOperator createFleetOperator(FleetOperatorDto fleetOperatorDto) {
        return fleetOperatorRepository.save(
                FleetOperatorMapper.toEntity(
                        fleetOperatorDto
                )
        );
    }

    public FleetOperator updateFleetOperator(FleetOperatorDto fleetOperatorDto, UUID companyId) {
        FleetOperator fleetOperator = FleetOperatorMapper.toEntity(
                fleetOperatorDto
        );
        fleetOperator.setId(companyId);
        return fleetOperatorRepository.save(fleetOperator);

    }
}
