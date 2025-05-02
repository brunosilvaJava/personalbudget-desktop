package com.bts.personalbudgetdesktop.view;

import com.bts.personalbudgetdesktop.model.FinancialMovementStatus;
import com.bts.personalbudgetdesktop.model.OperationType;
import com.bts.personalbudgetdesktop.util.DateUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FinancialMovementView {

    private final StringProperty code;
    private final StringProperty description;
    private final StringProperty amount;
    private final StringProperty amountPaid;
    private final StringProperty operationType;
    private final StringProperty status;
    private final StringProperty movementDate;
    private final StringProperty dueDate;
    private final StringProperty payDate;
    private final SimpleObjectProperty<Boolean> active;

    private final BooleanProperty buttonsDisabled;

    public FinancialMovementView(
            final UUID code,
            final OperationType operationType,
            final String description,
            final BigDecimal amount,
            final BigDecimal amountPaid,
            final FinancialMovementStatus status,
            final LocalDate movementDate,
            final LocalDate dueDate,
            final LocalDate payDate,
            final Boolean active
    ) {
        this.code = new SimpleStringProperty(code.toString());
        this.operationType = new SimpleStringProperty(buildOperationTypeNameValue(operationType));
        this.description = new SimpleStringProperty(description);
        this.amount = new SimpleStringProperty(amount.toString());
        this.amountPaid = new SimpleStringProperty(amountPaid.toString());
        this.status = new SimpleStringProperty(buildStatusValue(status));
        this.movementDate = new SimpleStringProperty(formatDate(movementDate));
        this.dueDate = new SimpleStringProperty(formatDate(dueDate));
        this.payDate = new SimpleStringProperty(formatDate(payDate));
        this.active = new SimpleObjectProperty<>(active);
        buttonsDisabled = new SimpleBooleanProperty(false);
    }

    public BooleanProperty buttonsDisabledProperty() {
        return buttonsDisabled;
    }

    public void setButtonsDisabled(boolean disabled) {
        this.buttonsDisabled.set(disabled);
    }

    public static String buildOperationTypeNameValue(final OperationType operationType) {
        return switch (operationType) {
            case CREDIT -> "Crédito";
            case DEBIT -> "Débito";
        };
    }

    public static String buildStatusValue(final FinancialMovementStatus status) {
        return switch (status) {
            case PENDING -> "Pendente";
            case PAID_OUT -> "Pago";
            case LATE -> "Atrasado";
        };
    }

    public static String formatDate(LocalDate date) {
        return DateUtil.format(date).orElse("");
    }

    public StringProperty getCodeProperty() {
        return code;
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public StringProperty getAmountProperty() {
        return amount;
    }

    public StringProperty getAmountPaidProperty() {
        return amountPaid;
    }

    public StringProperty getOperationTypeProperty() {
        return operationType;
    }

    public StringProperty getStatusProperty() {
        return status;
    }

    public StringProperty getMovementDateProperty() {
        return movementDate;
    }

    public StringProperty getDueDateProperty() {
        return dueDate;
    }

    public StringProperty getPayDateProperty() {
        return payDate;
    }

    public ObjectProperty<Boolean> getActiveProperty() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialMovementView that = (FinancialMovementView) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
