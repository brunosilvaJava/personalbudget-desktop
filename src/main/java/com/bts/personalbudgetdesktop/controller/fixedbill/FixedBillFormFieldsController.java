package com.bts.personalbudgetdesktop.controller.fixedbill;

import com.bts.personalbudgetdesktop.view.FixedBillView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public abstract class FixedBillFormFieldsController {

    @FXML
    public Label fixedBillCode;

    @FXML
    protected RadioButton creditRadio;
    @FXML
    protected RadioButton debitRadio;

    @FXML
    protected TextField descriptionField;
    @FXML
    protected TextField amountField;

    @FXML
    protected HBox recurrenceTypeHBox;
    @FXML
    protected RadioButton weeklyRadio;
    @FXML
    protected RadioButton monthlyRadio;
    @FXML
    protected RadioButton yearlyRadio;

    @FXML
    protected FlowPane weeklyDaysPane;

    @FXML
    protected ComboBox<Integer> monthlyDayComboBox;
    @FXML
    protected HBox monthlyDaysPane;

    @FXML
    protected TextField yearlyDayField;
    @FXML
    protected HBox yearlyDaysPane;

    @FXML
    protected DatePicker startDatePicker;
    @FXML
    protected DatePicker endDatePicker;

    @FXML
    public RadioButton activeRadio;

    @FXML
    protected TableView<FixedBillView> fixedBillTable;
    @FXML
    protected TableColumn<FixedBillView, String> descriptionColumn;
    @FXML
    protected TableColumn<FixedBillView, String> amountColumn;
    @FXML
    protected TableColumn<FixedBillView, String> recurrenceTypeColumn;
    @FXML
    protected TableColumn<FixedBillView, String> operationTypeColumn;
    @FXML
    protected TableColumn<FixedBillView, String> daysColumn;
    @FXML
    protected TableColumn<FixedBillView, String> startDateColumn;
    @FXML
    protected TableColumn<FixedBillView, String> endDateColumn;
    @FXML
    protected TableColumn<FixedBillView, Boolean> activeColumn;

    protected void configureOperationTypeGroup() {
        final ToggleGroup operationTypeGroup = new ToggleGroup();
        creditRadio.setToggleGroup(operationTypeGroup);
        debitRadio.setToggleGroup(operationTypeGroup);
    }

    protected void configureRecurrenceTypeGroup() {
        final ToggleGroup recurrenceTypeGroup = new ToggleGroup();
        weeklyRadio.setToggleGroup(recurrenceTypeGroup);
        monthlyRadio.setToggleGroup(recurrenceTypeGroup);
        yearlyRadio.setToggleGroup(recurrenceTypeGroup);
    }

    protected void configureTable() {
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountProperty());
        recurrenceTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getRecurrenceTypeProperty());
        operationTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getOperationTypeProperty());
        daysColumn.setCellValueFactory(cellData -> cellData.getValue().getDaysProperty());
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().getStartDateProperty());
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().getEndDateProperty());
        activeColumn.setCellValueFactory(cellData -> cellData.getValue().getActiveProperty());

        // Criar a coluna do botão de editar
        TableColumn<FixedBillView, Void> editColumn = new TableColumn<>("Editar");

        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("✏ Editar");

            {
                editButton.setOnAction(event -> {
                    FixedBillView selectedItem = getTableView().getItems().get(getIndex());
                    editarRegistro(selectedItem);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

        fixedBillTable.getColumns().add(editColumn);

        fixedBillTable.setItems(findFixedBillViews());
    }


    protected void cleanFormFields() {
        descriptionField.clear();
        amountField.clear();
        weeklyRadio.setSelected(true);
        monthlyRadio.setSelected(false);
        yearlyRadio.setSelected(false);
        weeklyDaysPane.getChildren().stream()
                .filter(node -> node instanceof CheckBox)
                .map(node -> (CheckBox) node)
                .forEach(weekDayCheckbox -> weekDayCheckbox.setSelected(false));
        monthlyDayComboBox.getSelectionModel().clearSelection();
        yearlyDayField.clear();
        weeklyDaysPane.setVisible(true);
        monthlyDaysPane.setVisible(false);
        yearlyDaysPane.setVisible(false);
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        activeRadio.setSelected(true);
    }

    protected abstract void editarRegistro(FixedBillView fixedBillView);

    protected abstract ObservableList<FixedBillView> findFixedBillViews();

}
