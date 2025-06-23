package com.bts.personalbudgetdesktop.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record InstallmentBill(
        UUID code,
        OperationType operationType,
        String description,
        BigDecimal amount,
        InstallmentBillStatus status,
        LocalDate purchaseDate,
        Integer installmentCount
) {
    public InstallmentBill(OperationType operationType,
                           String description,
                           BigDecimal amount,
                           InstallmentBillStatus status,
                           LocalDate purchaseDate,
                           Integer installmentCount
    ) {
        this(UUID.randomUUID(), operationType, description, amount, status, purchaseDate, installmentCount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstallmentBill installmentBill = (InstallmentBill) o;
        return Objects.equals(code, installmentBill.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

}