package com.bts.personalbudgetdesktop.client.personalbudgetapi;

import com.bts.personalbudgetdesktop.model.InstallmentBillStatus;
import com.bts.personalbudgetdesktop.model.OperationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InstallmentBillResponse(
        UUID code,
        @JsonProperty("operation_type")
        OperationType operationType,
        String description,
        BigDecimal amount,
        InstallmentBillStatus status,
        @JsonProperty("purchase_date")
        LocalDate purchaseDate,
        @JsonProperty("installment_total")
        Integer installmentCount,
        Boolean active
) {
}