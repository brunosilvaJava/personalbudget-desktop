package com.bts.personalbudgetdesktop.view;

import com.bts.personalbudgetdesktop.model.InstallmentBillDTO;
import com.bts.personalbudgetdesktop.model.InstallmentBillStatus;
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

public class InstallmentBillView {

    private final StringProperty code;
    private final StringProperty description;
    private final StringProperty amount;
    private final StringProperty operationType;
    private final StringProperty status;
    private final StringProperty purchaseDate;
    private final StringProperty installmentCount;
    private final SimpleObjectProperty<Boolean> active;

    private final BooleanProperty buttonsDisabled;

    public InstallmentBillView(
            final UUID code,
            final OperationType operationType,
            final String description,
            final BigDecimal amount,
            final InstallmentBillStatus status,
            final LocalDate purchaseDate,
            final Integer installmentCount,
            final Boolean active
    ) {
        this.code = new SimpleStringProperty(code.toString());
        this.description = new SimpleStringProperty(description);
        this.amount = new SimpleStringProperty(amount.toString());
        this.operationType = new SimpleStringProperty(buildOperationTypeNameValue(operationType));
        this.status = new SimpleStringProperty(buildStatusValue(status));
        this.purchaseDate = new SimpleStringProperty(formatDate(purchaseDate));
        this.installmentCount = new SimpleStringProperty(installmentCount.toString());
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

    public static String buildStatusValue(final InstallmentBillStatus status) {
        return switch (status) {
            case PENDING -> "Pendente";
            case DONE -> "Pago";
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

    public StringProperty getOperationTypeProperty() {
        return operationType;
    }

    public StringProperty getStatusProperty() {
        return status;
    }

    public StringProperty getPurchaseDateProperty() {
        return purchaseDate;
    }

    public StringProperty getInstallmentCountProperty() {
        return installmentCount;
    }

    public ObjectProperty<Boolean> getActiveProperty() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstallmentBillView that = (InstallmentBillView) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}

