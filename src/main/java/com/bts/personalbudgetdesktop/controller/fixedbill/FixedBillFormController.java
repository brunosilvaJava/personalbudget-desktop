package com.bts.personalbudgetdesktop.controller.fixedbill;

import com.bts.personalbudgetdesktop.model.FixedBill;
import com.bts.personalbudgetdesktop.model.OperationType;
import com.bts.personalbudgetdesktop.model.RecurrenceType;
import com.bts.personalbudgetdesktop.service.FixedBillService;
import com.bts.personalbudgetdesktop.view.FixedBillView;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;

public class FixedBillFormController
        extends FixedBillFormFieldsController
        implements FixedBillFormActionsController {

    protected final FixedBillService fixedBillService;
    protected final ObservableList<FixedBillView> fixedBillViewList;

    public FixedBillFormController() {
        fixedBillService = new FixedBillService();
        fixedBillViewList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize() {
        monthlyDayComboBox.getItems().addAll(IntStream.rangeClosed(1, 31).boxed().toList());
        configureRecurrenceTypeGroup();
        configureOperationTypeGroup();
        configureTable();
        loadFixedBills();
    }

    protected void loadFixedBills() {
        final List<FixedBillView> fixedBillViews = fixedBillService.findAllFixedBills()
                .stream()
                .map(FixedBillView::new)
                .toList();
        fixedBillViewList.setAll(fixedBillViews);
    }

    @Override
    protected void editarRegistro(FixedBillView fixedBillView) {
        System.out.println("Editando: " + fixedBillView.getDescriptionProperty().get());
    }

    @Override
    public void updateVisibleRecurrenceTypePane() {
        weeklyDaysPane.setVisible(weeklyRadio.isSelected());
        monthlyDaysPane.setVisible(monthlyRadio.isSelected());
        yearlyDaysPane.setVisible(yearlyRadio.isSelected());
    }

    @Override
    public void cleanForm() {
        cleanFormFields();
    }

    @Override
    public void handleSubmit() {
        final FixedBill newBill = buildFixedBill();
        fixedBillService.save(newBill);
        loadFixedBills();
        updateVisibleRecurrenceTypePane();
        cleanForm();
    }

    protected FixedBill buildFixedBill() {
        final RecurrenceType recurrenceType = findRecurrenceType();
        return new FixedBill(
                findOperationType(),
                descriptionField.getText(),
                new BigDecimal(amountField.getText()),
                recurrenceType,
                findDaysByRecurrenceType(recurrenceType),
                startDatePicker.getValue(),
                endDatePicker.getValue(),
                activeRadio.isSelected()
        );
    }

    protected OperationType findOperationType() {
        return creditRadio.isSelected() ? OperationType.CREDIT : OperationType.DEBIT;
    }

    protected RecurrenceType findRecurrenceType() {
        final String recurrenceTypeValue = (String) recurrenceTypeHBox.getChildren()
                .stream()
                .filter(node -> node instanceof RadioButton)
                .map(node -> (RadioButton) node)
                .filter(RadioButton::isSelected)
                .findFirst()
                .get()
                .getUserData();
        return RecurrenceType.valueOf(recurrenceTypeValue);
    }

    protected Set<Integer> findDaysByRecurrenceType(RecurrenceType recurrenceType) {
        return switch (recurrenceType) {
            case WEEKLY -> weeklyDaysPane.getChildren().stream()
                    .filter(node -> node instanceof CheckBox)
                    .map(node -> (CheckBox) node)
                    .filter(CheckBox::isSelected)
                    .map(weekDayCheckbox -> Integer.valueOf((String) weekDayCheckbox.getUserData()))
                    .collect(Collectors.toSet());
            case MONTHLY -> Set.of(monthlyDayComboBox.getValue());
            case YEARLY -> Set.of(convertDateToDayOfYear(yearlyDayField.getText()));
        };
    }

    protected int convertDateToDayOfYear(String date) {
        String[] parts = date.split("/");
        if (parts.length != 2) throw new IllegalArgumentException("Formato inválido (DD/MM esperado)");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        LocalDate localDate = LocalDate.of(Year.now().getValue(), month, day);
        return localDate.getDayOfYear();
    }

    @Override
    protected ObservableList<FixedBillView> findFixedBillViews() {
        return fixedBillViewList;
    }
}
