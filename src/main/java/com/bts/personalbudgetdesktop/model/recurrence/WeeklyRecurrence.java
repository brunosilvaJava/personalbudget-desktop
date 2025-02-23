package com.bts.personalbudgetdesktop.model.recurrence;

import java.time.DayOfWeek;
import java.util.List;

public class WeeklyRecurrence extends AbstractRecurrence<List<DayOfWeek>> {
    private final List<DayOfWeek> dayOfWeekList;

    public WeeklyRecurrence(List<DayOfWeek> dayOfWeekList) {
        this.dayOfWeekList = dayOfWeekList;
    }

    @Override
    public RecurrenceType findType() {
        return RecurrenceType.WEEKLY;
    }

    @Override
    public List<DayOfWeek> findRecurrence() {
        return dayOfWeekList;
    }
}
