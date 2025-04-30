package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.PhieuDangKyBUS;
import com.hcmus.exammanagement.dto.HoaDonDTO;
import com.hcmus.exammanagement.bus.ThanhToanBUS;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class DuyetThanhToanController {

    @FXML
    private Label labelNgayLap;

    @FXML
    private TextField tongTien;

    @FXML
    private TextField maThanhToan;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnThanhToan;

    private HoaDonDTO hoaDon;
    private ThanhToanBUS thanhToanBUS;
    private PhieuDangKyBUS phieuDangKyBUS;

    @FXML
    public void initialize() {
        btnThanhToan.setOnAction(e -> handleThanhToan());
        btnHuy.setOnAction(e -> handleHuy());
    }

    public DuyetThanhToanController() {
        thanhToanBUS = new ThanhToanBUS();
        phieuDangKyBUS = new PhieuDangKyBUS();
    }

    public void setHoaDon(HoaDonDTO hoaDon) {
        this.hoaDon = hoaDon;
        if (hoaDon != null) {
            labelNgayLap.setText(hoaDon.getNgayLap().toString());
            tongTien.setText(String.valueOf(hoaDon.getTongTien()));
        }
    }

    @FXML
    private void handleThanhToan() {
        if (hoaDon == null) {
            showAlert(AlertType.ERROR, "Lỗi", "Không có thông tin hóa đơn để xét duyệt.");
            return;
        }

        String maThanhToanValue = maThanhToan.getText();
        if (maThanhToanValue == null || maThanhToanValue.isBlank()) {
            showAlert(AlertType.ERROR, "Lỗi", "Mã thanh toán không hợp lệ.");
            return;
        }

        boolean success = thanhToanBUS.duyetThanhToan(hoaDon.getMaHd(), maThanhToanValue);
        if (success) {
            if (hoaDon.getPhieuDangKy() != null) {
                phieuDangKyBUS.capNhatTrangThai(
                        hoaDon.getPhieuDangKy().getMaPhieuDangKy(),
                        "Đã xác nhận"
                );
            }

            showAlert(AlertType.INFORMATION, "Thành công", "Thanh toán đã được xác nhận.");
            closePopup();
        } else {
            showAlert(AlertType.ERROR, "Lỗi", "Xảy ra lỗi trong quá trình thanh toán.");
        }
    }

    @FXML
    private void handleHuy() {
        closePopup();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closePopup() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }
}
