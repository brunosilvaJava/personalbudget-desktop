package com.bts.personalbudgetdesktop.controller.financialmovement;

import java.util.UUID;
import javafx.fxml.FXML;

public interface FinancialMovementFormActionsController {

    @FXML void initialize();
    @FXML void cleanForm();
    @FXML void actionSaveButton();
    void actionDeleteButton(UUID financialMovementCode);
}
