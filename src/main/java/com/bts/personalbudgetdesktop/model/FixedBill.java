package com.bts.personalbudgetdesktop.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record FixedBill(
        OperationType operationType,
        String description,
        BigDecimal amount,
        RecurrenceType recurrenceType,
        Set<Integer> days,
        LocalDate startDate,
        LocalDate endDate,
        Boolean active
) {
    public FixedBill(OperationType operationType,
                     String description,
                     BigDecimal amount,
                     RecurrenceType recurrenceType,
                     Set<Integer> days) {
        this(operationType, description, amount, recurrenceType, days, null, null, Boolean.TRUE);
    }

}
