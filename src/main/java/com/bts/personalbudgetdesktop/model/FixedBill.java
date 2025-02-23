package com.bts.personalbudgetdesktop.model;

import com.bts.personalbudgetdesktop.model.recurrence.Recurrence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record FixedBill(
        UUID code,
        OperationType operationType,
        String description,
        BigDecimal amount,
        Recurrence<?> recurrence,
        LocalDate startDate,
        LocalDate endDate,
        Boolean active
) {
    public FixedBill(OperationType operationType,
                     String description,
                     BigDecimal amount,
                     Recurrence<?> recurrence) {
        this(UUID.randomUUID(), operationType, description, amount, recurrence, null, null, Boolean.TRUE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FixedBill fixedBill = (FixedBill) o;
        return Objects.equals(code, fixedBill.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
