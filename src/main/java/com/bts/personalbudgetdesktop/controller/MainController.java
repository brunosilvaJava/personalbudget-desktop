package com.bts.personalbudgetdesktop.controller;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class MainController {

    @FXML
    protected BorderPane mainPane;


    @FXML
    public void initialize() {
    }

    @FXML
    public void handleExit() {
        System.exit(0);
    }

    @FXML
    public void showDashboard() {
        loadView("/views/dashboard.fxml");
    }

    @FXML
    public void showFixedBill() {
        loadView("/views/fixed_bill_form.fxml");
    }

    public void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent view = loader.load();
            // Adiciona efeito de transição
            FadeTransition ft = new FadeTransition(Duration.millis(500), view);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}