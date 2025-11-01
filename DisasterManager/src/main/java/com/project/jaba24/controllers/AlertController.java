package com.project.jaba24.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.stage.Stage;

public class AlertController {
    public Button acknowledgeButton;

    @FXML
    private void handleAcknowledge(ActionEvent event) {
        Stage stage = (Stage) acknowledgeButton.getScene().getWindow();
        stage.close();
    }
}
