package com.hcmus.exammanagement.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label loginMessage;

    @FXML
    private void initialize() {

    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        if (isValidCredentials(username, password)) {
//            loginMessage.setText("Login successful!");
//            loadDashboard("Tiep nhan");
//        } else {
//            loginMessage.setText("Invalid username or password.");
//        }

        loadDashboard("Tiep nhan");
    }

    private boolean isValidCredentials(String username, String password) {
        // Replace with actual authentication logic
        return ("admin".equals(username) && "password".equals(password)) || ("1".equals(username) && "1".equals(password));
    }

    private void loadDashboard(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController dashboardController = loader.getController();
            dashboardController.initializeRole(role);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/hcmus/exammanagement/style.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}