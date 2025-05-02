package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.AutoTaskScheduler;
import com.hcmus.exammanagement.bus.NhanVienBUS;
import com.hcmus.exammanagement.dto.Database;
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
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            // Thử khởi tạo kết nối — nếu sai tài khoản sẽ ném lỗi
            Database.initialize(username, password);

            // Lấy loại nhân viên từ DB
            String loaiNV = NhanVienBUS.layLoaiNV(username);
            if (loaiNV == null) {
                loginMessage.setText("Không tìm thấy loại nhân viên.");
                loginMessage.setStyle("-fx-text-fill: red;");
                return;
            }

            loginMessage.setText("Đăng nhập thành công!");
            loadDashboard(loaiNV); // truyền loai_nv như "Tiep nhan", "Ke toan" v.v.

            // Khởi động AutoTaskScheduler nếu cần
            AutoTaskScheduler autoTaskScheduler = new AutoTaskScheduler();
            autoTaskScheduler.start();

        } catch (Exception e) {
            loginMessage.setText("Tên đăng nhập hoặc mật khẩu sai.");
            loginMessage.setStyle("-fx-text-fill: red;");
        }
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