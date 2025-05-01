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

import java.text.NumberFormat;
import java.util.Locale;

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
    private Button btnXacNhanMaTT;

    private HoaDonDTO hoaDon;
    private ThanhToanBUS thanhToanBUS;
    private PhieuDangKyBUS phieuDangKyBUS;

    @FXML
    public void initialize() {
        btnXacNhanMaTT.setOnAction(e -> btnXacNhanMaTT());
        btnHuy.setOnAction(e -> btnHuy());
    }

    public DuyetThanhToanController() {
        thanhToanBUS = new ThanhToanBUS();
        phieuDangKyBUS = new PhieuDangKyBUS();
    }

    public void setHoaDon(HoaDonDTO hoaDon) {
        this.hoaDon = hoaDon;
        if (hoaDon != null) {
            labelNgayLap.setText(hoaDon.getNgayLap().toString());
//            tongTien.setText(String.valueOf(hoaDon.getTongTien()));

            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            currencyFormat.setMaximumFractionDigits(0);

            tongTien.setText(currencyFormat.format(hoaDon.getTongTien()) + "đ");
        }
    }

    @FXML
    private void btnXacNhanMaTT() {
        if (hoaDon == null) {
            hienThongBao(AlertType.ERROR, "Lỗi", "Không có thông tin hóa đơn để xét duyệt.");
            return;
        }

        String maThanhToanValue = maThanhToan.getText();
        if (maThanhToanValue == null || maThanhToanValue.isBlank()) {
            hienThongBao(AlertType.ERROR, "Lỗi", "Mã thanh toán không hợp lệ.");
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

            hienThongBao(AlertType.INFORMATION, "Thành công", "Thanh toán đã được xác nhận.");
            closePopup();
        } else {
            hienThongBao(AlertType.ERROR, "Lỗi", "Xảy ra lỗi trong quá trình thanh toán.");
        }
    }

    @FXML
    private void btnHuy() {
        closePopup();
    }

    private void hienThongBao(AlertType type, String title, String message) {
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
