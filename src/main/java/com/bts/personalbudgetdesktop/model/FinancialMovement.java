package com.bts.personalbudgetdesktop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record FinancialMovement(
        UUID code,
        OperationType operationType,
        String description,
        BigDecimal amount,
        BigDecimal amountPaid,
        LocalDateTime movementDate,
        LocalDateTime dueDate,
        LocalDateTime payDate,
        FinancialMovementStatus status,
        Boolean flagActive) {

    public FinancialMovement(OperationType operationType,
                             String description,
                             BigDecimal amount,
                             LocalDateTime movementDate,
                             LocalDateTime dueDate,
                             FinancialMovementStatus status

    ) {
        this(UUID.randomUUID(), operationType, description, amount, null, movementDate, dueDate, null,
                status, Boolean.TRUE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialMovement financialMovement = (FinancialMovement) o;
        return Objects.equals(code, financialMovement.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

}