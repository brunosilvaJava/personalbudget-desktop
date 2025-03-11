package com.bts.personalbudgetdesktop.controller.installmentbill;

import com.bts.personalbudgetdesktop.model.InstallmentBillDTO;
import com.bts.personalbudgetdesktop.model.InstallmentBillStatus;
import com.bts.personalbudgetdesktop.model.OperationType;
import com.bts.personalbudgetdesktop.view.InstallmentBillView;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

public abstract class InstallmentBillFormFieldsController {

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
    protected ComboBox<String> installmentBillStatusComboBox;

    @FXML
    protected TextField installmentCount;

    @FXML
    protected DatePicker purchaseDatePicker;

    @FXML
    public RadioButton activeRadio;

    @FXML
    protected TableView<InstallmentBillView> installmentBillTable;
    @FXML
    protected TableColumn<InstallmentBillView, String> descriptionColumn;
    @FXML
    protected TableColumn<InstallmentBillView, String> codeColumn;
    @FXML
    protected TableColumn<InstallmentBillView, String> amountColumn;
    @FXML
    protected TableColumn<InstallmentBillView, String> statusColumn;
    @FXML
    protected TableColumn<InstallmentBillView, String> operationTypeColumn;
    @FXML
    protected TableColumn<InstallmentBillView, String> installmentCountColumn;
    @FXML
    protected TableColumn<InstallmentBillView, String> purchaseDateColumn;
    @FXML
    protected TableColumn<InstallmentBillView, String> activeColumn;

    protected abstract ObservableList<InstallmentBillView> findInstallmentBillViews();

    protected abstract Set<InstallmentBillDTO> findAll();

    protected abstract void actionEditButton(InstallmentBillView installmentBillView);

    protected abstract void actionDeleteButton(UUID installmentBillCode);

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
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
        operationTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getOperationTypeProperty());
        installmentCountColumn.setCellValueFactory(cellData -> cellData.getValue().getInstallmentCountProperty());
        purchaseDateColumn.setCellValueFactory(cellData -> cellData.getValue().getPurchaseDateProperty());
        activeColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getActiveProperty().getValue()) {
                return new SimpleStringProperty("Ativo");
            } else {
                return new SimpleStringProperty("Inativo");
            }
        });

        TableColumn<InstallmentBillView, Void> editColumn = buildEditButtonTableColumn();
        TableColumn<InstallmentBillView, Void> deleteColumn = buildDeleteButtonTableColumn();

        installmentBillTable.getColumns().addLast(editColumn);
        installmentBillTable.getColumns().addLast(deleteColumn);

        installmentBillTable.setItems(findInstallmentBillViews());
    }

    private TableColumn<InstallmentBillView, Void> buildEditButtonTableColumn() {
        TableColumn<InstallmentBillView, Void> editColumn = new TableColumn<>("Editar");
        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("✏ Editar");

            {
                editButton.setOnAction(event -> {
                    final InstallmentBillView selectedItem = getTableView().getItems().get(getIndex());
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

    private TableColumn<InstallmentBillView, Void> buildDeleteButtonTableColumn() {
        TableColumn<InstallmentBillView, Void> deleteColumn = new TableColumn<>("Excluir");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("🗑 Excluir");

            {
                deleteButton.setOnAction(event -> {
                    final InstallmentBillView selectedItem = getTableView().getItems().get(getIndex());
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

    protected void setFieldsValues(final InstallmentBillDTO installmentBillDTO) {
        creditRadio.setSelected(OperationType.CREDIT == installmentBillDTO.operationType());
        debitRadio.setSelected(OperationType.DEBIT == installmentBillDTO.operationType());
        codeField.setText(installmentBillDTO.code().toString());
        descriptionField.setText(installmentBillDTO.description());
        amountField.setText(installmentBillDTO.amount().toString());
        InstallmentBillStatus status = installmentBillDTO.status();
        if (status == InstallmentBillStatus.PENDING) {
            installmentBillStatusComboBox.setValue("Pendente");
        } else {
            installmentBillStatusComboBox.setValue("Pago");
        }
        installmentCount.setText(installmentBillDTO.installmentCount().toString());
        purchaseDatePicker.setValue(installmentBillDTO.purchaseDate());
        activeRadio.setSelected(installmentBillDTO.active());
    }

    protected void cleanFormFields() {
        debitRadio.setSelected(true);
        creditRadio.setSelected(false);
        codeField.setText(null);
        descriptionField.clear();
        amountField.clear();
        installmentCount.clear();
        purchaseDatePicker.setValue(null);
        activeRadio.setSelected(true);
    }

    protected void loadInstallmentBills() {
        final List<InstallmentBillView> installmentBillViews = findAll()
                .stream()
                .map(dto -> new InstallmentBillView(
                        dto.code(),
                        dto.operationType(),
                        dto.description(),
                        dto.amount(),
                        dto.status(),
                        dto.purchaseDate(),
                        dto.installmentCount(),
                        dto.active()
                ))
                .toList();
        findInstallmentBillViews().setAll(installmentBillViews);
    }

    protected void showValidationErrors(Map<String, String> errors) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Campos inválidos");
        alert.setHeaderText("Por favor, corrija os campos inválidos:");
        alert.setContentText(String.join("\n", errors.values()));
        alert.show();
    }
}
