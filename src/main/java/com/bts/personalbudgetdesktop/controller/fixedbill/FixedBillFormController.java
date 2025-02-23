package com.bts.personalbudgetdesktop.controller.fixedbill;

import com.bts.personalbudgetdesktop.exception.ValidationException;
import com.bts.personalbudgetdesktop.model.FixedBillDTO;
import com.bts.personalbudgetdesktop.model.recurrence.RecurrenceType;
import com.bts.personalbudgetdesktop.service.FixedBillService;
import com.bts.personalbudgetdesktop.view.FixedBillView;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FixedBillFormController
        extends FixedBillFormFieldsController
        implements FixedBillFormActionsController {

    private final FixedBillService fixedBillService;
    private final ObservableList<FixedBillView> fixedBillViewList;

    private static final int FIRST_DAY_OF_MONTH = 1;
    private static final int LAST_DAY_OF_MONTH = 31;

    public FixedBillFormController() {
        fixedBillService = new FixedBillService();
        fixedBillViewList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize() {
        monthlyDayComboBox.getItems()
                .addAll(IntStream.rangeClosed(FIRST_DAY_OF_MONTH, LAST_DAY_OF_MONTH).boxed().toList());
        configureRecurrenceTypeGroup();
        configureOperationTypeGroup();
        configureTable();
        loadFixedBills();
    }

    @Override
    public Set<FixedBillDTO> findAll() {
        return fixedBillService.findAll();
    }

    @Override
    public void actionEditButton(final FixedBillView fixedBillView) {
        final UUID code = UUID.fromString(fixedBillView.getCodeProperty().getValue());
        final FixedBillDTO fixedBillDTO = fixedBillService.findByCode(code).orElseThrow();
        setFieldsValues(fixedBillDTO);
    }

    @Override
    public void actionDeleteButton(final UUID fixedBillCode) {
        fixedBillService.delete(fixedBillCode);
        loadFixedBills();
    }

    @Override
    public void actionSaveButton() {
        try {
            final FixedBillDTO newBill = buildFixedBill();
            fixedBillService.save(newBill);
            loadFixedBills();
            updateVisibleRecurrenceTypePane();
            cleanForm();
        } catch (ValidationException e) {
            showValidationErrors(e.getErrors());
        }
    }

    protected FixedBillDTO buildFixedBill() {
        final RecurrenceType recurrenceType = findRecurrenceType();
        return new FixedBillDTO(
                Optional.ofNullable(codeField.getText()).orElse(null),
                findOperationType(),
                descriptionField.getText(),
                amountField.getText(),
                recurrenceType,
                findDaysByRecurrenceType(recurrenceType),
                startDatePicker.getValue(),
                endDatePicker.getValue(),
                activeRadio.isSelected()
        );
    }

    @Override
    public void updateVisibleRecurrenceTypePane() {
        super.updateVisibleRecurrenceTypePane();
    }

    @Override
    public void cleanForm() {
        super.cleanForm();
    }

    @Override
    protected ObservableList<FixedBillView> findFixedBillViews() {
        return fixedBillViewList;
    }
}
