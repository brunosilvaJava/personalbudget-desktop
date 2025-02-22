package com.bts.personalbudgetdesktop.controller.fixedbill;

import javafx.fxml.FXML;

public interface FixedBillFormActionsController {
    @FXML void initialize();
    @FXML void updateVisibleRecurrenceTypePane();
    @FXML void cleanForm();
    @FXML void handleSubmit();
}
