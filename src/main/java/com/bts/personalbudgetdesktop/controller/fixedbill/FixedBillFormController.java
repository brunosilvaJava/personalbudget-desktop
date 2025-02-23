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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
            fixedBillService.validateFields(newBill);
            if (newBill.code() != null) {
                final Alert confirmSaveAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmSaveAlert.setTitle("Confirmação de Edição");
                confirmSaveAlert.setHeaderText("Confirmação de Edição");
                confirmSaveAlert.setContentText(String.format("Deseja realmente editar a conta fixa: '%s'?", newBill.description()));
                final Optional<ButtonType> result = confirmSaveAlert.showAndWait();
                if (result.isEmpty() || result.get() != ButtonType.OK) {
                    return;
                }
            }
            fixedBillService.save(newBill);
            loadFixedBills();
            updateVisibleRecurrenceTypePane();
            cleanForm(false);

        } catch (ValidationException e) {
            showValidationErrors(e.getErrors());
        }
    }

    protected FixedBillDTO buildFixedBill() {
        final RecurrenceType recurrenceType = findRecurrenceType();
        return new FixedBillDTO(
                codeField.getText(),
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
        cleanForm(true);
    }

    private void cleanForm(boolean confirmation) {
        if (confirmation) {
            final Alert confirmCleanAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmCleanAlert.setTitle("Confirmação de Limpeza");
            confirmCleanAlert.setHeaderText("Confirmação de Limpeza");
            confirmCleanAlert.setContentText("Deseja realmente limpar o formulário?");
            final Optional<ButtonType> result = confirmCleanAlert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }
        }
        cleanFormFields();
    }

    @Override
    protected ObservableList<FixedBillView> findFixedBillViews() {
        return fixedBillViewList;
    }
}
