package com.bts.personalbudgetdesktop.controller.financialmovement;

import com.bts.personalbudgetdesktop.model.FinancialMovementDTO;
import com.bts.personalbudgetdesktop.model.FinancialMovementStatus;
import com.bts.personalbudgetdesktop.model.OperationType;
import com.bts.personalbudgetdesktop.view.FinancialMovementView;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public abstract class FinancialMovementFormFieldsController {

    @FXML
    public Label codeField;

    @FXML
    protected RadioButton creditRadio;
    @FXML
    protected RadioButton debitRadio;

    @FXML
    protected TextField descriptionField;
    @FXML
    protected TextField amountField;
    @FXML
    protected TextField amountPaidField;

    @FXML
    protected ComboBox<String> financialMovementStatusComboBox;

    @FXML
    protected DatePicker movementDatePicker;
    @FXML
    protected DatePicker dueDatePicker;
    @FXML
    protected DatePicker payDatePicker;

    @FXML
    public RadioButton activeRadio;

    @FXML
    protected TableView<FinancialMovementView> financialMovementTable;
    @FXML
    protected TableColumn<FinancialMovementView, String> descriptionColumn;
    @FXML
    protected TableColumn<FinancialMovementView, String> codeColumn;
    @FXML
    protected TableColumn<FinancialMovementView, String> amountColumn;
    @FXML
    protected TableColumn<FinancialMovementView, String> amountPaidColumn;
    @FXML
    protected TableColumn<FinancialMovementView, String> statusColumn;
    @FXML
    protected TableColumn<FinancialMovementView, String> operationTypeColumn;
    @FXML
    protected TableColumn<FinancialMovementView, String> movementDateColumn;
    @FXML
    protected TableColumn<FinancialMovementView, String> dueDateColumn;
    @FXML
    protected TableColumn<FinancialMovementView, String> payDateColumn;
    @FXML
    protected TableColumn<FinancialMovementView, String> activeColumn;

    protected abstract ObservableList<FinancialMovementView> findFinancialMovementViews();

    protected abstract List<FinancialMovementDTO> findAll();

    protected abstract void actionEditButton(FinancialMovementView financialMovementView);

    protected abstract void actionDeleteButton(UUID financialMovementCode);

    protected void configureOperationTypeGroup() {
        final ToggleGroup operationTypeGroup = new ToggleGroup();
        creditRadio.setToggleGroup(operationTypeGroup);
        debitRadio.setToggleGroup(operationTypeGroup);
    }

    protected OperationType findOperationType() {
        return creditRadio.isSelected() ? OperationType.CREDIT : OperationType.DEBIT;
    }

    protected void configureTable() {
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountProperty());
        amountPaidColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountPaidProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
        operationTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getOperationTypeProperty());
        movementDateColumn.setCellValueFactory(cellData -> cellData.getValue().getMovementDateProperty());
        dueDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDueDateProperty());
        payDateColumn.setCellValueFactory(cellData -> cellData.getValue().getPayDateProperty());
        activeColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getActiveProperty().getValue() == null) {
                return new SimpleStringProperty("Ativo");
            } else {
                if (cellData.getValue().getActiveProperty().getValue()) {
                    return new SimpleStringProperty("Ativo");
                } else {
                    return new SimpleStringProperty("Inativo");
                }
            }

        });

        TableColumn<FinancialMovementView, Void> editColumn = buildEditButtonTableColumn();
        TableColumn<FinancialMovementView, Void> deleteColumn = buildDeleteButtonTableColumn();

        financialMovementTable.getColumns().addLast(editColumn);
        financialMovementTable.getColumns().addLast(deleteColumn);

        financialMovementTable.setItems(findFinancialMovementViews());
    }

    private TableColumn<FinancialMovementView, Void> buildEditButtonTableColumn() {
        TableColumn<FinancialMovementView, Void> editColumn = new TableColumn<>("Editar");
        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("✏ Editar");

            {
                editButton.setOnAction(event -> {
                    final FinancialMovementView selectedItem = getTableView().getItems().get(getIndex());
                    actionEditButton(selectedItem);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editButton);
            }
        });
        return editColumn;
    }

    private TableColumn<FinancialMovementView, Void> buildDeleteButtonTableColumn() {
        TableColumn<FinancialMovementView, Void> deleteColumn = new TableColumn<>("Excluir");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("🗑 Excluir");

            {
                deleteButton.setOnAction(event -> {
                    final FinancialMovementView selectedItem = getTableView().getItems().get(getIndex());
                    final Alert confirmDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir?", ButtonType.YES, ButtonType.NO);
                    confirmDeleteAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            actionDeleteButton(UUID.fromString(selectedItem.getCodeProperty().get()));
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
        return deleteColumn;
    }

    protected void setFieldsValues(final FinancialMovementDTO financialMovementDTO) {
        creditRadio.setSelected(OperationType.CREDIT == financialMovementDTO.operationType());
        debitRadio.setSelected(OperationType.DEBIT == financialMovementDTO.operationType());
        codeField.setText(financialMovementDTO.code().toString());
        descriptionField.setText(financialMovementDTO.description());
        amountField.setText(financialMovementDTO.amount().toString());
        amountPaidField.setText(financialMovementDTO.amountPaid().toString());

        FinancialMovementStatus status = financialMovementDTO.status();
        if (status == FinancialMovementStatus.PENDING) {
            financialMovementStatusComboBox.setValue("Pendente");
        } else if (status == FinancialMovementStatus.PAID_OUT) {
            financialMovementStatusComboBox.setValue("Pago");
        } else {
            financialMovementStatusComboBox.setValue("Atrasado");
        }

        movementDatePicker.setValue(financialMovementDTO.movementDate());
        dueDatePicker.setValue(financialMovementDTO.dueDate());
        payDatePicker.setValue(financialMovementDTO.payDate() != null ? financialMovementDTO.payDate() : null);
        activeRadio.setSelected(financialMovementDTO.flagActive());
    }

    protected void cleanFormFields() {
        debitRadio.setSelected(true);
        creditRadio.setSelected(false);
        codeField.setText(null);
        descriptionField.clear();
        amountField.clear();
        amountPaidField.clear();
        financialMovementStatusComboBox.setValue("Pendente");
        movementDatePicker.setValue(null);
        dueDatePicker.setValue(null);
        payDatePicker.setValue(null);
        activeRadio.setSelected(true);
    }

    protected void loadFinancialMovements() {
        final List<FinancialMovementView> financialMovementViews = findAll()
                .stream()
                .map(dto -> new FinancialMovementView(
                        dto.code(),
                        dto.operationType(),
                        dto.description(),
                        dto.amount(),
                        dto.amountPaid(),
                        dto.status(),
                        dto.movementDate(),
                        dto.dueDate(),
                        dto.payDate(),
                        dto.flagActive()
                ))
                .toList();
        findFinancialMovementViews().setAll(financialMovementViews);
    }

    protected void showValidationErrors(Map<String, String> errors) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Campos inválidos");
        alert.setHeaderText("Por favor, corrija os campos inválidos:");
        alert.setContentText(String.join("\n", errors.values()));

        if (errors.containsKey("description")) {
            descriptionField.setStyle("-fx-border-color: red;");
        }
        alert.show();
    }
}