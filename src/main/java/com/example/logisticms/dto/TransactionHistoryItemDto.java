package com.example.logisticms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryItemDto {
    private UUID shipmentId;
    private String shipmentName;
    private String shipmentFormalName;
    private LocalDateTime date;
    private BigDecimal totalEstimatedCost;
}

