package com.bts.personalbudgetdesktop.controller.fixedbill;

import com.bts.personalbudgetdesktop.model.FixedBillDTO;
import com.bts.personalbudgetdesktop.model.OperationType;
import com.bts.personalbudgetdesktop.model.recurrence.RecurrenceType;
import com.bts.personalbudgetdesktop.view.FixedBillView;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    protected TableColumn<FixedBillView, String> codeColumn;
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

    protected abstract ObservableList<FixedBillView> findFixedBillViews();

    protected abstract Set<FixedBillDTO> findAll();

    protected abstract void actionEditButton(FixedBillView fixedBillView);
    protected abstract void actionDeleteButton(UUID fixedBillCode);

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
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountProperty());
        recurrenceTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getRecurrenceTypeProperty());
        operationTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getOperationTypeProperty());
        daysColumn.setCellValueFactory(cellData -> cellData.getValue().getDaysProperty());
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().getStartDateProperty());
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().getEndDateProperty());
        activeColumn.setCellValueFactory(cellData -> cellData.getValue().getActiveProperty());

        TableColumn<FixedBillView, Void> editColumn = new TableColumn<>("Editar");

        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("✏ Editar");

            {
                editButton.setOnAction(event -> {
                    final FixedBillView selectedItem = getTableView().getItems().get(getIndex());
                    actionEditButton(selectedItem);
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

        TableColumn<FixedBillView, Void> deleteColumn = new TableColumn<>("Excluir");

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("✏ Excluir");

            {
                editButton.setOnAction(event -> {
                    final FixedBillView selectedItem = getTableView().getItems().get(getIndex());
                    actionDeleteButton(UUID.fromString(selectedItem.getCodeProperty().get()));
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
        fixedBillTable.getColumns().add(deleteColumn);

        fixedBillTable.setItems(findFixedBillViews());
    }

    protected void setFieldsValues(final FixedBillDTO fixedBillDTO) {
        creditRadio.setSelected(OperationType.CREDIT == fixedBillDTO.operationType());
        debitRadio.setSelected(OperationType.DEBIT == fixedBillDTO.operationType());
        codeField.setText(fixedBillDTO.code());
        descriptionField.setText(fixedBillDTO.description());
        amountField.setText(fixedBillDTO.amount());
        startDatePicker.setValue(fixedBillDTO.startDate());
        endDatePicker.setValue(fixedBillDTO.endDate());
        activeRadio.setSelected(fixedBillDTO.active());
        final RecurrenceType recurrenceType = fixedBillDTO.recurrenceType();
        switch (recurrenceType) {
            case WEEKLY -> {
                weeklyRadio.setSelected(true);
                fixedBillDTO.days().forEach(day -> {
                    final CheckBox checkBox = weeklyDaysPane.getChildren()
                            .stream()
                            .filter(node -> node instanceof CheckBox)
                            .map(node -> (CheckBox) node)
                            .filter(cb -> day.equals(cb.getUserData()))
                            .findFirst()
                            .orElseThrow();
                    checkBox.setSelected(true);
                });
            }
            case MONTHLY -> {
                monthlyRadio.setSelected(true);
                monthlyDayComboBox.setValue(Integer.parseInt(fixedBillDTO.days().iterator().next()));
            }
            case YEARLY -> {
                yearlyRadio.setSelected(true);
                yearlyDayField.setText(fixedBillDTO.days().iterator().next());
            }
        }
        updateVisibleRecurrenceTypePane();
    }

    protected void updateVisibleRecurrenceTypePane() {
        weeklyDaysPane.setVisible(weeklyRadio.isSelected());
        monthlyDaysPane.setVisible(monthlyRadio.isSelected());
        yearlyDaysPane.setVisible(yearlyRadio.isSelected());
        Optional<RecurrenceType> recurrenceType = Optional.empty();
        if (weeklyRadio.isSelected()) {
            recurrenceType = Optional.of(RecurrenceType.WEEKLY);
        } else if (monthlyRadio.isSelected()) {
            recurrenceType = Optional.of(RecurrenceType.MONTHLY);
        } else if (yearlyRadio.isSelected()) {
            recurrenceType = Optional.of(RecurrenceType.YEARLY);
        }
        cleanRecurrenceTypesOptions(recurrenceType.orElseThrow(), false);
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

    protected OperationType findOperationType() {
        return creditRadio.isSelected() ? OperationType.CREDIT : OperationType.DEBIT;
    }

    protected Set<String> findDaysByRecurrenceType(final RecurrenceType recurrenceType) {
        return switch (recurrenceType) {
            case WEEKLY -> findSelectedWeekDays();
            case MONTHLY -> Set.of(monthlyDayComboBox.getValue().toString());
            case YEARLY -> Set.of(yearlyDayField.getText());
        };
    }

    private Set<String> findSelectedWeekDays() {
        return weeklyDaysPane.getChildren().stream()
                .filter(node -> node instanceof CheckBox)
                .map(node -> (CheckBox) node)
                .filter(CheckBox::isSelected)
                .map(weekDayCheckbox -> (String) weekDayCheckbox.getUserData())
                .collect(Collectors.toSet());
    }

    protected void cleanForm() {
        debitRadio.setSelected(true);
        creditRadio.setSelected(false);
        codeField.setText(null);
        descriptionField.clear();
        amountField.clear();
        weeklyRadio.setSelected(true);
        weeklyDaysPane.setVisible(true);
        monthlyRadio.setSelected(false);
        monthlyDaysPane.setVisible(false);
        yearlyRadio.setSelected(false);
        yearlyDaysPane.setVisible(false);
        cleanRecurrenceTypesOptions(RecurrenceType.WEEKLY, true);
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        activeRadio.setSelected(true);
    }

    protected void cleanRecurrenceTypesOptions(final RecurrenceType recurrenceTypeSelected,
                                               final boolean cleanAll) {
        if (cleanAll || recurrenceTypeSelected != RecurrenceType.WEEKLY) {
            weeklyDaysPane.getChildren().stream()
                    .filter(node -> node instanceof CheckBox)
                    .map(node -> (CheckBox) node)
                    .forEach(weekDayCheckbox -> weekDayCheckbox.setSelected(false));
        }
        if (cleanAll || recurrenceTypeSelected != RecurrenceType.MONTHLY) {
            monthlyDayComboBox.getSelectionModel().clearSelection();
        }
        if (cleanAll || recurrenceTypeSelected != RecurrenceType.YEARLY) {
            yearlyDayField.clear();
        }
    }

    protected void loadFixedBills() {
        final List<FixedBillView> fixedBillViews = findAll()
                .stream()
                .map(FixedBillView::new)
                .toList();
        findFixedBillViews().setAll(fixedBillViews);
    }


    protected void showValidationErrors(Map<String, String> errors) {
        Alert alert = new Alert(AlertType.WARNING);

        alert.setTitle("Campos inválidos");
        alert.setHeaderText("Por favor, corrija os campos inválidos:");
        alert.setContentText(String.join("\n", errors.values()));

        if (errors.containsKey("description")) {
            descriptionField.setStyle("-fx-border-color: red;");
        }

        alert.show();
    }

}
