package com.bts.personalbudgetdesktop.controller.fixedbill;

import java.util.UUID;
import javafx.fxml.FXML;

public interface FixedBillFormActionsController {
    @FXML void initialize();
    @FXML void updateVisibleRecurrenceTypePane();
    @FXML void cleanForm();
    @FXML void actionSaveButton();
    @FXML void actionDeleteButton(UUID fixedBillCode);
}
