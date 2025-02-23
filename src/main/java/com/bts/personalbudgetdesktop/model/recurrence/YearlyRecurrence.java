package com.bts.personalbudgetdesktop.model.recurrence;

import java.time.MonthDay;

public class YearlyRecurrence extends AbstractRecurrence<MonthDay> {
    private final MonthDay monthDay;

    public YearlyRecurrence(MonthDay monthDay) {
        this.monthDay = monthDay;
    }

    @Override
    public RecurrenceType findType() {
        return RecurrenceType.YEARLY;
    }

    @Override
    public MonthDay findRecurrence() {
        return monthDay;
    }
}
