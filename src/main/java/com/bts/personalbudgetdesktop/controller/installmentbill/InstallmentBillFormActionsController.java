package com.bts.personalbudgetdesktop.controller.installmentbill;

import java.util.UUID;
import javafx.fxml.FXML;

public interface InstallmentBillFormActionsController {
    @FXML void initialize();
    @FXML void cleanForm();
    @FXML void actionSaveButton();
    void actionDeleteButton(UUID installmentBillCode);
}
