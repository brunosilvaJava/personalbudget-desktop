package com.bts.personalbudgetdesktop.model.recurrence;

public abstract class AbstractRecurrence<T> implements Recurrence<T>{
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "type=" + findType() +
                ", recurrence=" + findRecurrence() +
                '}';
    }
}
