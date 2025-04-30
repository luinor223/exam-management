package com.hcmus.exammanagement.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TNDangKyController {

    @FXML
    private AnchorPane formContainer;

    @FXML
    private JFXButton caNhanButton;

    @FXML
    private JFXButton donViButton;

    @FXML
    private void initialize() {
        // Ensure buttons are not null before setting styles
        if (caNhanButton != null && donViButton != null) {
            caNhanButton.setStyle("-fx-background-color: #71D9B6; -fx-background-radius: 10px; -fx-text-fill: WHITE");
            donViButton.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 10px; -fx-text-fill: BLACK");
        }
        // Load the default form
        loadForm("/com/hcmus/exammanagement/DangKy/dangky-canhan.fxml");
    }

    @FXML
    private void handleCaNhanButtonAction() {
        caNhanButton.setStyle("-fx-background-color: #71D9B6; -fx-background-radius: 10px; -fx-text-fill: WHITE");
        donViButton.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 10px; -fx-text-fill: BLACK");
        loadForm("/com/hcmus/exammanagement/DangKy/dangky-canhan.fxml");
    }

    @FXML
    private void handleDonViButtonAction() {
        donViButton.setStyle("-fx-background-color: #71D9B6; -fx-background-radius: 10px; -fx-text-fill: WHITE");
        caNhanButton.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 10px; -fx-text-fill: BLACK");
        loadForm("/com/hcmus/exammanagement/DangKy/dangky-donvi.fxml");
    }

    private void loadForm(String fxmlPath) {
        try {
            Parent form = FXMLLoader.load(getClass().getResource(fxmlPath));
            formContainer.getChildren().clear();
            formContainer.getChildren().add(form);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}