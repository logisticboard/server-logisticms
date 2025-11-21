package com.example.logisticms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FleetOperatorRoleCreate(
        @Email String email,
        @NotNull FleetOperatorRolesEnum role
) {}