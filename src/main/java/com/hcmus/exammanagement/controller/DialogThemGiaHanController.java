package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.PhieuGiaHanBUS;
import com.hcmus.exammanagement.dto.PhieuGiaHanDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class DialogThemGiaHanController {

    @FXML private ComboBox<String> cbMaCTPDK;
    @FXML private TextField txtLoaiGH;
    @FXML private TextField txtPhiGH;
    @FXML private TextField txtNhanVienTao;
    @FXML private CheckBox chkDaThanhToan;

    public void setDanhSachCTPDK(List<String> maCTPDKList) {
        cbMaCTPDK.setItems(FXCollections.observableArrayList(maCTPDKList));
    }

    @FXML
    private void handleLuu() {
        String maCTPDK = cbMaCTPDK.getValue();
        String loaiGH = txtLoaiGH.getText().trim();
        String nhanVienTao = txtNhanVienTao.getText().trim();
        double phiGH;

        try {
            phiGH = Double.parseDouble(txtPhiGH.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Phí gia hạn phải là số.");
            return;
        }

        if (maCTPDK == null || loaiGH.isEmpty() || nhanVienTao.isEmpty()) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        PhieuGiaHanDTO newPGH = new PhieuGiaHanDTO(
                null, loaiGH, phiGH, nhanVienTao, chkDaThanhToan.isSelected(), maCTPDK
        );

        if (PhieuGiaHanBUS.taoPhieuGiaHan(newPGH)) {
            showAlert("Thành công", "Thêm phiếu gia hạn thành công.");
            closeDialog();
        } else {
            showAlert("Lỗi", "Không thể thêm phiếu gia hạn.");
        }
    }

    @FXML
    private void handleHuy() {
        closeDialog();
    }

    private void closeDialog() {
        ((Stage) cbMaCTPDK.getScene().getWindow()).close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
