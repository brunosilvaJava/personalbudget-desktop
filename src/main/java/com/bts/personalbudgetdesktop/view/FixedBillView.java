package com.bts.personalbudgetdesktop.view;

import com.bts.personalbudgetdesktop.model.FixedBillDTO;
import com.bts.personalbudgetdesktop.model.OperationType;
import com.bts.personalbudgetdesktop.model.recurrence.RecurrenceType;
import com.bts.personalbudgetdesktop.util.DateUtil;
import com.bts.personalbudgetdesktop.util.NumberUtil;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import static com.bts.personalbudgetdesktop.util.StringUtils.EMPTY;
import static com.bts.personalbudgetdesktop.util.StringUtils.IN_LOCALE;
import static com.bts.personalbudgetdesktop.util.StringUtils.SEMICOLON;

public class FixedBillView {

    protected final StringProperty code;
    protected final StringProperty description;
    protected final StringProperty amount;
    protected final StringProperty recurrenceType;
    protected final StringProperty operationType;
    protected final StringProperty days;
    protected final StringProperty startDate;
    protected final StringProperty endDate;
    protected final SimpleObjectProperty<Boolean> active;

    public FixedBillView(final FixedBillDTO fixedBillDTO) {
        this.code = new SimpleStringProperty(fixedBillDTO.code().toString());
        this.description = new SimpleStringProperty(fixedBillDTO.description());
        this.amount = new SimpleStringProperty(fixedBillDTO.amount());
        this.recurrenceType = new SimpleStringProperty(buildRecurrenceTypeValue(fixedBillDTO));
        this.operationType = new SimpleStringProperty(buildOperationTypeNameValue(fixedBillDTO.operationType()));
        this.days = new SimpleStringProperty(buildDaysValue(fixedBillDTO.days(), fixedBillDTO.recurrenceType()));
        this.startDate = new SimpleStringProperty(buildStartDateValue(fixedBillDTO.startDate()));
        this.endDate = new SimpleStringProperty(buildEndDateValue(fixedBillDTO.endDate()));
        this.active = new SimpleObjectProperty<>(fixedBillDTO.active());
    }

    protected static String buildOperationTypeNameValue(final OperationType operationType) {
        return switch (operationType) {
            case CREDIT -> "Crédito";
            case DEBIT -> "Débito";
        };
    }

    protected static String buildRecurrenceTypeValue(final FixedBillDTO fixedBillDTO) {
        return switch (fixedBillDTO.recurrenceType()) {
            case WEEKLY -> "Semanal";
            case MONTHLY -> "Mensal";
            case YEARLY -> "Anual";
        };
    }

    protected static String buildDaysValue(final Set<String> days, final RecurrenceType recurrenceType) {
        if (days == null || days.isEmpty()) {
            return EMPTY;
        }
        return switch (recurrenceType) {
            case WEEKLY -> days.stream()
                    .map(day -> DayOfWeek.valueOf(day).getDisplayName(TextStyle.SHORT_STANDALONE, IN_LOCALE))
                    .collect(Collectors.joining(SEMICOLON));
            case MONTHLY -> days.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(SEMICOLON));
            case YEARLY -> String.join(SEMICOLON, days);
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

    public StringProperty getCodeProperty() {
        return code;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FixedBillView that = (FixedBillView) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}

