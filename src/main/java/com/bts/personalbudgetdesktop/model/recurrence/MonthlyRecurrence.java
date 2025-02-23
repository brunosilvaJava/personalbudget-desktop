package com.bts.personalbudgetdesktop.model.recurrence;

public class MonthlyRecurrence extends AbstractRecurrence<Integer> {
    private final Integer dayOfMonth;

    public MonthlyRecurrence(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    @Override
    public RecurrenceType findType() {
        return RecurrenceType.MONTHLY;
    }

    @Override
    public Integer findRecurrence() {
        return dayOfMonth;
    }
}
