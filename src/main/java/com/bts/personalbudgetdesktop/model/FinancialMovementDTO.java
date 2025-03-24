package com.bts.personalbudgetdesktop.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

    public record FinancialMovementDTO(
        UUID code,
        OperationType operationType,
        String description,
        BigDecimal amount,
        BigDecimal amountPaid,
        LocalDate movementDate,
        LocalDate dueDate,
        LocalDate payDate,
        FinancialMovementStatus status,
        Boolean flagActive
    ) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FinancialMovementDTO financialMovementDTO = (FinancialMovementDTO) o;
        return Objects.equals(code, financialMovementDTO.code());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
    }
