package com.bts.personalbudgetdesktop.model;

import com.bts.personalbudgetdesktop.model.recurrence.RecurrenceType;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public record FixedBillDTO(
        String code,
        OperationType operationType,
        String description,
        String amount,
        RecurrenceType recurrenceType,
        Set<String> days,
        LocalDate startDate,
        LocalDate endDate,
        Boolean active
) {

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FixedBillDTO that = (FixedBillDTO) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
