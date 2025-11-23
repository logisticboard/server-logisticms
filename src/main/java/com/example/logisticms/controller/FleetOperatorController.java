package com.example.logisticms.controller;

import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.FleetOperatorDto;
import com.example.logisticms.dto.FleetOperatorRolesEnum;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.mapper.FleetOperatorMapper;
import com.example.logisticms.service.impl.FleetOperatorRoleServiceImpl;
import com.example.logisticms.service.impl.FleetOperatorServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fleetoperators")
@RequiredArgsConstructor
@Validated
public class FleetOperatorController {
    private final FleetOperatorServiceImpl fleetOperatorService;
    private final FleetOperatorRoleServiceImpl fleetOperatorRoleService;

//    @PostMapping
//    public ApiResponseDTO<Void> createFleetOperatorRole(@RequestBody List<FleetOperatorRoleCreate> fleetOperatorRoleCreateList){
//        fleetOperatorService.createFleetOperatorRoles(fleetOperatorRoleCreateList);
//        return ApiResponseDTO.<Void>builder()
//                .message("Fleet Operator Roles created successfully")
//                .success(true)
//                .build();
//    }

    @PostMapping
    public ApiResponseDTO<FleetOperatorDto> createFleetOperator(@RequestBody @Valid FleetOperatorDto fleetOperatorDto){
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        FleetOperator fleetOperator = fleetOperatorService.createFleetOperator(fleetOperatorDto);
        fleetOperatorRoleService.addMemberToFleetOperator(fleetOperator, FleetOperatorRolesEnum.ADMIN, userId);
        return ApiResponseDTO.<FleetOperatorDto>builder()
                .message("Fleet Operator created successfully")
                .data(FleetOperatorMapper.toDto(fleetOperator))
                .success(true)
                .build();
    }

    @PutMapping("/{companyId}")
    public ApiResponseDTO<FleetOperatorDto> updateFleetOperator(@RequestBody @Valid FleetOperatorDto fleetOperatorDto,
                                                                @PathVariable @NotBlank
                                                                @Pattern(
                                                                        regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                                                                        message = "companyId must be a valid UUID"
                                                                )
                                                                String companyId){
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(fleetOperatorRoleService.isUserAdminOfFleetOperator(UUID.fromString(companyId), userId)){
            FleetOperator fleetOperator = fleetOperatorService.updateFleetOperator(fleetOperatorDto, UUID.fromString(companyId));
            fleetOperatorRoleService.addMemberToFleetOperator(fleetOperator, FleetOperatorRolesEnum.ADMIN, userId);
            return ApiResponseDTO.<FleetOperatorDto>builder()
                    .message("Company data updated successfully")
                    .data(FleetOperatorMapper.toDto(fleetOperator))
                    .success(true)
                    .build();
        }
        throw new UnsupportedOperationException("User is not authorized to update this Fleet Operator!");
    }


    @GetMapping
    public ApiResponseDTO<List<FleetOperatorDto>> getFleetOperators(){
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        return ApiResponseDTO.<List<FleetOperatorDto>>builder()
                .message("Fleet Operators retrieved successfully")
                .data(fleetOperatorRoleService.getFleetOperatorsByUserId(userId))
                .success(true)
                .build();
    }
}
