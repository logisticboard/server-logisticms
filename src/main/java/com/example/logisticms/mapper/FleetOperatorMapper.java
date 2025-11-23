package com.example.logisticms.mapper;

import com.example.logisticms.dto.FleetOperatorDto;
import com.example.logisticms.entity.FleetOperator;

public class FleetOperatorMapper {
    public static FleetOperator toEntity(com.example.logisticms.dto.FleetOperatorDto dto) {
        FleetOperator fleetOperator = new FleetOperator();
        fleetOperator.setName(dto.getName());
        fleetOperator.setGstNumber(dto.getGstNumber());
        fleetOperator.setContactPhone(dto.getContactPhone());
        fleetOperator.setContactEmail(dto.getContactEmail());
        fleetOperator.setAddress(dto.getAddress());
        fleetOperator.setImageUrl(dto.getImageUrl());
        fleetOperator.setDescription(dto.getDescription());
        return fleetOperator;
    }

    public static FleetOperatorDto toDto(FleetOperator save) {
        FleetOperatorDto fleetOperatorDto =  new FleetOperatorDto();
        fleetOperatorDto.setFleetOperatorId(save.getId());
        fleetOperatorDto.setName(save.getName());
        fleetOperatorDto.setGstNumber(save.getGstNumber());
        fleetOperatorDto.setContactPhone(save.getContactPhone());
        fleetOperatorDto.setContactEmail(save.getContactEmail());
        fleetOperatorDto.setAddress(save.getAddress());
        fleetOperatorDto.setImageUrl(save.getImageUrl());
        fleetOperatorDto.setDescription(save.getDescription());
        return fleetOperatorDto;
    }
}
