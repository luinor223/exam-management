package com.hcmus.exammanagement;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    @FXML
    private VBox sidebarContainer;

    @FXML
    private StackPane contentArea;

    private List<JFXButton> sidebarButtons = new ArrayList<>();

    @FXML
    void initialize() {
        // Initialization code if needed
    }

    public void initializeRole(String role) {
        addSidebarButtons(role);
    }

    private void addSidebarButtons(String employeeType) {
        sidebarContainer.getChildren().clear();
        sidebarButtons.clear();
        switch (employeeType) {
            case "Tiep nhan":
                sidebarButtons.add(createButton("Today", "#Today"));
                sidebarButtons.add(createButton("Important", "#Important"));
                break;
            case "Ke toan":
                sidebarButtons.add(createButton("Inbox", "#Inbox"));
                sidebarButtons.add(createButton("Someday", "#Someday"));
                break;
            case "Nhap lieu":
                sidebarButtons.add(createButton("Inbox", "#Inbox"));
                sidebarButtons.add(createButton("Someday", "#Someday"));
                break;
            case "Khao thi":
                sidebarButtons.add(createButton("Inbox", "#Inbox"));
                sidebarButtons.add(createButton("Someday", "#Someday"));
                break;
            case "Quan tri":
                sidebarButtons.add(createButton("Inbox", "#Inbox"));
                sidebarButtons.add(createButton("Someday", "#Someday"));
                break;
        }
        sidebarContainer.getChildren().addAll(sidebarButtons);
    }

    private JFXButton createButton(String text, String action) {
        JFXButton button = new JFXButton(text);
        button.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        button.setPrefHeight(32.0);
        button.setPrefWidth(174.0);
        button.getStyleClass().add("invisible-button");
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: WHITE");
        button.setOnAction(event -> handleButtonAction(action, button));
        button.setFont(new Font("System Bold", 12.0));
        return button;
    }

    private void handleButtonAction(String action, JFXButton button) {
        resetButtonStyles();
        button.setStyle("-fx-background-color: #77dbb8; -fx-background-radius: 10px; -fx-text-fill: WHITE");
        switch (action) {
            case "#Today":
                Today(new ActionEvent());
                break;
            case "#Important":
                Important(new ActionEvent());
                break;
            case "#Inbox":
                Inbox(new ActionEvent());
                break;
            case "#Someday":
                Someday(new ActionEvent());
                break;
            // Add more cases for other actions
        }
    }

    private void resetButtonStyles() {
        for (JFXButton button : sidebarButtons) {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: WHITE");
        }
    }

    @FXML
    void Important(ActionEvent event) {
        // Implementation
    }

    @FXML
    void Inbox(ActionEvent event) {
        // Implementation
    }

    @FXML
    void Someday(ActionEvent event) {
        // Implementation
    }

    @FXML
    void Today(ActionEvent event) {
        // Implementation
    }
}