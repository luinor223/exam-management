package com.hcmus.exammanagement.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashboardController {
    @FXML
    private VBox sidebarContainer;

    @FXML
    private StackPane contentArea;

    private final List<JFXButton> sidebarButtons = new ArrayList<>();

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
                sidebarButtons.add(createButton("Đăng ký", "#TNDangKy"));
                break;
            case "Ke toan":
                sidebarButtons.add(createButton("Lập phiếu gia hạn", "#LapPhieuGiaHan"));
                sidebarButtons.add(createButton("Thanh toán", "#ThanhToan"));
                break;
            case "Nhap lieu":
                sidebarButtons.add(createButton("Nhập kết quả", "#NhapKetQua"));
                break;
            case "Khao thi":
                sidebarButtons.add(createButton("Xếp lịch cho đơn vị", "Xeplichdonvi"));
                sidebarButtons.add(createButton("Quản lý lịch thi", "#QLLichThi"));
                sidebarButtons.add(createButton("Phát hành phiếu dự thi", "#PhatHanhPhieu"));
                break;
            case "Quan tri":
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
        button.setOnAction(event -> {
            try {
                handleButtonAction(action, button);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        button.setFont(new Font("System Bold", 12.0));
        return button;
    }

    private void handleButtonAction(String action, JFXButton button) throws IOException {
        resetButtonStyles();
        button.setStyle("-fx-background-color: #77dbb8; -fx-background-radius: 10px; -fx-text-fill: WHITE");
        switch (action) {
            case "#TNDangKy":
                TNDangKy(new ActionEvent());
                break;
            case "#ThanhToan":
                ThanhToan(new ActionEvent());
                break;
            case "#NhapKetQua":
                NhapKetQua(new ActionEvent());
                break;
            case "#LapPhieuGiaHan":
                LapPhieuGiaHan(new ActionEvent());
                break;
            case "#QLLichThi":
                QLLichThi(new ActionEvent());
                break;
            case "Xeplichdonvi":
                XepLichDonVi(new ActionEvent());
            case "#PhatHanhPhieu":
                PhatHanhPhieu(new ActionEvent());
                break;
        }
    }

    private void resetButtonStyles() {
        for (JFXButton button : sidebarButtons) {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: WHITE");
        }
    }

    @FXML
    void TNDangKy(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hcmus/exammanagement/DangKy/dangky.fxml")));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);
    }

    @FXML
    void ThanhToan(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hcmus/exammanagement/ThanhToan/thanhtoan.fxml")));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);
    }

    @FXML
    void LapPhieuGiaHan(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hcmus/exammanagement/LapPhieuGiaHan/lap-phieu-gia-han.fxml")));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);
    }

    @FXML
    void NhapKetQua(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hcmus/exammanagement/CapChungChi/nhap-ket-qua.fxml")));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);
    }

    @FXML
    void QLLichThi(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hcmus/exammanagement/DangKy/ql-lich-thi.fxml")));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);
    }

    @FXML
    void XepLichDonVi(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hcmus/exammanagement/DangKy/xep-lich-don-vi.fxml")));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);
    }

    @FXML
    void PhatHanhPhieu(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hcmus/exammanagement/PhatHanhPhieu/phat-hanh-phieu.fxml")));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);
    }
}