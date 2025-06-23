package com.bts.personalbudgetdesktop.controller.installmentbill;

import com.bts.personalbudgetdesktop.exception.ValidationException;
import com.bts.personalbudgetdesktop.model.InstallmentBillDTO;
import com.bts.personalbudgetdesktop.model.InstallmentBillStatus;
import com.bts.personalbudgetdesktop.service.InstallmentBillService;
import com.bts.personalbudgetdesktop.view.InstallmentBillView;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.mapstruct.ap.internal.util.Strings;

public class InstallmentBillFormController
        extends InstallmentBillFormFieldsController
        implements InstallmentBillFormActionsController {

    private final InstallmentBillService installmentBillService;
    private final ObservableList<InstallmentBillView> installmentBillViewList;

    public InstallmentBillFormController() {
        installmentBillService = new InstallmentBillService();
        installmentBillViewList = FXCollections.observableArrayList();
    }


    @Override
    public void initialize() {
        configureInstallmentBillStatusComboBox();
        configureOperationTypeGroup();
        configureTable();
        loadInstallmentBills();
    }

    private void configureInstallmentBillStatusComboBox() {
        InstallmentBillStatus[] statusValues = InstallmentBillStatus.values();
        for (InstallmentBillStatus status : statusValues) {
            String statusValue = switch (status) {
                case PENDING -> "Pendente";
                case DONE -> "Pago";
            };
            installmentBillStatusComboBox.getItems().add(statusValue);
        }
        installmentBillStatusComboBox.setValue("Pendente");
    }

    @Override
    public List<InstallmentBillDTO> findAll() {
        return installmentBillService.findAll();
    }

    @Override
    public void actionEditButton(final InstallmentBillView installmentBillView) {
        final UUID code = UUID.fromString(installmentBillView.getCodeProperty().getValue());
        final InstallmentBillDTO installmentBillDTO = installmentBillService.findByCode(code);
        setFieldsValues(installmentBillDTO);
    }

    @Override
    public void actionDeleteButton(final UUID installmentBillCode) {
        installmentBillService.delete(installmentBillCode);
        loadInstallmentBills();
    }

    @Override
    public void actionSaveButton() {
        try {
            final InstallmentBillDTO installmentBillDTO = buildInstallmentBill();
            installmentBillService.validateFields(installmentBillDTO);
            if (installmentBillDTO.code() != null) {
                final Alert confirmSaveAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmSaveAlert.setTitle("Confirmação de Edição");
                confirmSaveAlert.setHeaderText("Confirmação de Edição");
                confirmSaveAlert.setContentText(String.format("Deseja realmente editar a conta parcelada: '%s'?", installmentBillDTO.description()));
                final Optional<ButtonType> result = confirmSaveAlert.showAndWait();
                if (result.isEmpty() || result.get() != ButtonType.OK) {
                    return;
                }
            }
            installmentBillService.save(installmentBillDTO);
            loadInstallmentBills();
            cleanForm(false);

        } catch (ValidationException e) {
            showValidationErrors(e.getErrors());
        }
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateForm() {
        String installmentText = installmentCount.getText().trim();

        if (installmentText.isEmpty()) {
            showErrorMessage("O total de parcelas não pode estar vazio!");
            return false;
        }

        try {
            int installmentCount = Integer.parseInt(installmentText);
            if (installmentCount <= 0) {
                showErrorMessage("O total de parcelas deve ser maior que zero!");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("O total de parcelas deve ser um número válido!");
            return false;
        }

        return true; // Se passar todas as validações, retorna verdadeiro
    }

    protected InstallmentBillDTO buildInstallmentBill() {
        return new InstallmentBillDTO(
                findCode(),
                findOperationType(),
                descriptionField.getText(),
                Strings.isNotEmpty(amountField.getText()) ? new BigDecimal(amountField.getText()) : null,
                findStatus(),
                purchaseDatePicker.getValue(),
                Strings.isNotEmpty(installmentCount.getText()) ? Integer.parseInt(installmentCount.getText()) : null
        );
    }

    private InstallmentBillStatus findStatus() {
        String statusSelected = installmentBillStatusComboBox.getSelectionModel().getSelectedItem();
        if (statusSelected.equals("Pendente")) {
            return InstallmentBillStatus.PENDING;
        } else {
            return InstallmentBillStatus.DONE;
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
    protected ObservableList<InstallmentBillView> findInstallmentBillViews() {
        return installmentBillViewList;
    }
}

