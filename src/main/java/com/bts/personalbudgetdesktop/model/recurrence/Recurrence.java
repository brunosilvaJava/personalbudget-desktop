package com.bts.personalbudgetdesktop.model.recurrence;

public interface Recurrence<T> {
    RecurrenceType findType();
    T findRecurrence();
}
