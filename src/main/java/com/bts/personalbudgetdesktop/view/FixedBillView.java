package com.bts.personalbudgetdesktop.view;

import com.bts.personalbudgetdesktop.model.FixedBill;
import com.bts.personalbudgetdesktop.model.OperationType;
import com.bts.personalbudgetdesktop.model.RecurrenceType;
import com.bts.personalbudgetdesktop.util.DateUtil;
import com.bts.personalbudgetdesktop.util.NumberUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import static com.bts.personalbudgetdesktop.util.DateUtil.buildDateFormat;
import static com.bts.personalbudgetdesktop.util.StringUtils.EMPTY;
import static com.bts.personalbudgetdesktop.util.StringUtils.SEMICOLON;

public class FixedBillView {

    protected final StringProperty description;
    protected final StringProperty amount;
    protected final StringProperty recurrenceType;
    protected final StringProperty operationType;
    protected final StringProperty days;
    protected final StringProperty startDate;
    protected final StringProperty endDate;
    protected final SimpleObjectProperty<Boolean> active;

    public FixedBillView(final FixedBill fixedBill) {
        this.description = new SimpleStringProperty(fixedBill.description());
        this.amount = new SimpleStringProperty(buildAmountValue(fixedBill.amount()));
        this.recurrenceType = new SimpleStringProperty(buildRecurrenceTypeValue(fixedBill));
        this.operationType = new SimpleStringProperty(buildOperationTypeNameValue(fixedBill.operationType()));
        this.days = new SimpleStringProperty(buildDaysValue(fixedBill.days(), fixedBill.recurrenceType()));
        this.startDate = new SimpleStringProperty(buildStartDateValue(fixedBill.startDate()));
        this.endDate = new SimpleStringProperty(buildEndDateValue(fixedBill.endDate()));
        this.active = new SimpleObjectProperty<>(fixedBill.active());
    }

    protected static String buildOperationTypeNameValue(final OperationType operationType) {
        return switch (operationType) {
            case CREDIT -> "Crédito";
            case DEBIT -> "Débito";
        };
    }

    protected static String buildAmountValue(final BigDecimal amount) {
        return NumberUtil.format(amount);
    }

    protected static String buildRecurrenceTypeValue(final FixedBill fixedBill) {
        return switch (fixedBill.recurrenceType()) {
            case WEEKLY -> "Semanal";
            case MONTHLY -> "Mensal";
            case YEARLY -> "Anual";
        };
    }

    protected static String buildDaysValue(final Set<Integer> days, final RecurrenceType recurrenceType) {
        if (days == null || days.isEmpty()) {
            return EMPTY;
        }
        return switch (recurrenceType) {
            case WEEKLY -> days.stream()
                    .map(DateUtil::buildWeeklyDayName)
                    .collect(Collectors.joining(SEMICOLON));
            case MONTHLY -> days.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(SEMICOLON));
            case YEARLY -> days.stream()
                    .map(dayOfYear -> {
                        // TODO - verificar ano bissexto, ver como está sendo salvo no backend
                        final LocalDate date = LocalDate.ofYearDay(Year.now().getValue(), dayOfYear);
                        return buildDateFormat(date);
                    })
                    .collect(Collectors.joining(SEMICOLON));
        };
    }

    protected static String buildStartDateValue(final LocalDate startDate) {
        return formatDate(startDate);
    }

    protected static String buildEndDateValue(final LocalDate endDate) {
        return formatDate(endDate);
    }

    protected static String formatDate(LocalDate startDate) {
        return DateUtil.format(startDate).orElse(EMPTY);
    }


    public StringProperty getDescriptionProperty() {
        return description;
    }

    public StringProperty getAmountProperty() {
        return amount;
    }

    public StringProperty getRecurrenceTypeProperty() {
        return recurrenceType;
    }

    public StringProperty getOperationTypeProperty() {
        return operationType;
    }

    public StringProperty getDaysProperty() {
        return days;
    }

    public StringProperty getStartDateProperty() {
        return startDate;
    }

    public StringProperty getEndDateProperty() {
        return endDate;
    }

    public ObjectProperty<Boolean> getActiveProperty() {
        return active;
    }


}

