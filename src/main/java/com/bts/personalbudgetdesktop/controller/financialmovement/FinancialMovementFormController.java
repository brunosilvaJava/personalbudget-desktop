package com.bts.personalbudgetdesktop.controller.financialmovement;

import com.bts.personalbudgetdesktop.exception.ValidationException;
import com.bts.personalbudgetdesktop.model.FinancialMovementDTO;
import com.bts.personalbudgetdesktop.model.FinancialMovementStatus;
import com.bts.personalbudgetdesktop.service.FinancialMovementService;
import com.bts.personalbudgetdesktop.view.FinancialMovementView;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.mapstruct.ap.internal.util.Strings;

public class FinancialMovementFormController
        extends FinancialMovementFormFieldsController
        implements FinancialMovementFormActionsController {

    private final FinancialMovementService financialMovementService;
    private final ObservableList<FinancialMovementView> financialMovementViewList;

    public FinancialMovementFormController() {
        financialMovementService = new FinancialMovementService();
        financialMovementViewList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize() {
        configureFinancialMovementStatusComboBox();
        configureOperationTypeGroup();
        configureTable();
        loadFinancialMovements();
    }

    private void configureFinancialMovementStatusComboBox() {
        FinancialMovementStatus[] statusValues = FinancialMovementStatus.values();
        for (FinancialMovementStatus status : statusValues) {
            String statusValue = switch (status) {
                case PENDING -> "Pendente";
                case PAID_OUT -> "Pago";
                case LATE -> "Atrasado";
            };
            financialMovementStatusComboBox.getItems().add(statusValue);
        }
        financialMovementStatusComboBox.setValue("Pendente");
    }

    @Override
    public void actionEditButton(final FinancialMovementView financialMovementView) {
        final UUID code = UUID.fromString(financialMovementView.getCodeProperty().getValue());
        final FinancialMovementDTO financialMovementDTO = financialMovementService.findByCode(code).orElseThrow();
        setFieldsValues(financialMovementDTO);
    }

    @Override
    public void actionDeleteButton(final UUID financialMovementCode) {
        financialMovementService.delete(financialMovementCode);
        loadFinancialMovements();
    }

    @Override
    public void actionSaveButton() {
        try {
            final FinancialMovementDTO financialMovementDTO = buildFinancialMovement();
            financialMovementService.validateFields(financialMovementDTO);
            if (financialMovementDTO.code() != null) {
                final Alert confirmSaveAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmSaveAlert.setTitle("Confirmação de Edição");
                confirmSaveAlert.setHeaderText("Confirmação de Edição");
                confirmSaveAlert.setContentText(String.format("Deseja realmente editar o movimento financeiro: '%s'?", financialMovementDTO.description()));
                final Optional<ButtonType> result = confirmSaveAlert.showAndWait();
                if (result.isEmpty() || result.get() != ButtonType.OK) {
                    return;
                }
            }
            financialMovementService.save(financialMovementDTO);
            loadFinancialMovements();
            cleanForm(false);

        } catch (ValidationException e) {
            showValidationErrors(e.getErrors());
        }
    }

    protected FinancialMovementDTO buildFinancialMovement() {
        return new FinancialMovementDTO(
                findCode(),
                findOperationType(),
                descriptionField.getText(),
                Strings.isNotEmpty(amountField.getText()) ? new BigDecimal(amountField.getText()) : null,
                Strings.isNotEmpty(amountPaidField.getText()) ? new BigDecimal(amountPaidField.getText()) : null,
                movementDatePicker.getValue(),
                dueDatePicker.getValue(),
                payDatePicker.getValue() != null ? payDatePicker.getValue() : null,
                findStatus(),
                activeRadio.isSelected()
        );
    }

    private FinancialMovementStatus findStatus() {
        String statusSelected = financialMovementStatusComboBox.getSelectionModel().getSelectedItem();
        if (statusSelected.equals("Pendente")) {
            return FinancialMovementStatus.PENDING;
        } else if (statusSelected.equals("Pago")) {
            return FinancialMovementStatus.PAID_OUT;
        } else {
            return FinancialMovementStatus.LATE;
        }
    }

    private UUID findCode() {
        if (codeField.getText() == null || codeField.getText().isEmpty()) {
            return UUID.randomUUID();
        } else {
            return UUID.fromString(codeField.getText());
        }
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
    protected ObservableList<FinancialMovementView> findFinancialMovementViews() {
        return financialMovementViewList;
    }

    @Override
    protected Set<FinancialMovementDTO> findAll() {
        return financialMovementService.findAll();
    }

    private void showValidationErrors(Set<String> errors) {
        StringBuilder errorMessage = new StringBuilder("Erros de validação:\n");
        for (String error : errors) {
            errorMessage.append(error).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erros de Validação");
        alert.setHeaderText("Existem erros no formulário");
        alert.setContentText(errorMessage.toString());
        alert.showAndWait();
    }
}
