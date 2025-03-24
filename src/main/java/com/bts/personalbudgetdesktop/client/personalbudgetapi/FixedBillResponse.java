package com.bts.personalbudgetdesktop.client.personalbudgetapi;

import com.bts.personalbudgetdesktop.model.OperationType;
import com.bts.personalbudgetdesktop.model.recurrence.RecurrenceType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record FixedBillResponse(
        UUID code,
        @JsonProperty("operation_type")
        OperationType operationType,
        String description,
        BigDecimal amount,
        @JsonProperty("recurrence_type")
        RecurrenceType recurrenceType,
        Set<Integer> days,
        @JsonProperty("flg_leap_year")
        Boolean flgLeapYear,
        String status,
        @JsonProperty("start_date")
        LocalDate startDate,
        @JsonProperty("end_date")
        LocalDate endDate
){
}
