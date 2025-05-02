package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.KetQuaBUS;
import com.hcmus.exammanagement.dto.KetQuaDTO;
import com.hcmus.exammanagement.dto.PhieuDuThiDTO;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller for form to input exam results
 */
@Slf4j
public class NhapKetQuaFormController implements Initializable {
    @FXML private TextField tfMaLT;
    @FXML private TextField tfSBD;
    @FXML private TextField tfDiem;
    @FXML private TextField tfXepLoai;  // Changed from ComboBox to TextField
    @FXML private TextArea taNhanXet;
    @FXML private DatePicker dpNgayCapCC;
    @FXML private DatePicker dpNgayHetHan;
    @FXML private Button btnHuy;
    @FXML private Button btnLuu;

    private final PhieuDuThiDTO phieuDuThi;
    private final KetQuaBUS ketQuaBUS;
    private KetQuaDTO ketQua;

    public NhapKetQuaFormController(PhieuDuThiDTO phieuDuThi, KetQuaBUS ketQuaBUS) {
        this.phieuDuThi = phieuDuThi;
        this.ketQuaBUS = ketQuaBUS;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupForm();
        layKetQuaData();
        setupEventHandlers();
    }

    private void setupForm() {
        // Display phiếu dự thi info
        tfMaLT.setText(phieuDuThi.getMaLT());
        tfSBD.setText(phieuDuThi.getSbd());
    }

    private void layKetQuaData() {
        try {
            // Try to load existing result
            ketQua = ketQuaBUS.layKetQua(phieuDuThi.getMaLT(), phieuDuThi.getSbd());

            if (ketQua != null) {
                // Populate form with existing data
                tfDiem.setText(String.valueOf(ketQua.getDiem()));
                tfXepLoai.setText(ketQua.getXepLoai());  // Set text instead of selection
                taNhanXet.setText(ketQua.getNhanXet());
                dpNgayCapCC.setValue(ketQua.getNgayCapChungChi());
                dpNgayHetHan.setValue(ketQua.getNgayHetHan());
            } else {
                // Create new result object
                ketQua = new KetQuaDTO();
                ketQua.setMaLT(phieuDuThi.getMaLT());
                ketQua.setSbd(phieuDuThi.getSbd());

                // Set default values
                dpNgayCapCC.setValue(LocalDate.now());
                dpNgayHetHan.setValue(LocalDate.now().plusYears(2));
            }
        } catch (IllegalArgumentException | SQLException e) {
            thongBao(Alert.AlertType.ERROR, "Lỗi", null, "Không thể tải thông tin kết quả: " + e.getMessage());
            log.error(e.getMessage());
        }
    }

    private void setupEventHandlers() {
        btnHuy.setOnAction(e -> btnHuyNhap());
        btnLuu.setOnAction(e -> btnLuuKetQua());
    }

    private void btnHuyNhap() {
        // Just close the form
        ((Stage) btnHuy.getScene().getWindow()).close();
    }

    private void btnLuuKetQua() {
        try {
            // Validate input data - this now contains most validation logic
            if (!validateForm()) {
                return;
            }

            // Update result data from form inputs
            ketQua.setDiem(Integer.parseInt(tfDiem.getText().trim()));
            ketQua.setXepLoai(tfXepLoai.getText().trim());
            ketQua.setNhanXet(taNhanXet.getText());
            ketQua.setNgayCapChungChi(dpNgayCapCC.getValue());
            ketQua.setNgayHetHan(dpNgayHetHan.getValue());

            // Default status if empty
            if (ketQua.getTrangThai() == null || ketQua.getTrangThai().trim().isEmpty()) {
                ketQua.setTrangThai("Chưa cấp");
            }

            boolean success = ketQuaBUS.luuKetQua(ketQua);

            if (success) {
                thongBao(Alert.AlertType.INFORMATION, "Thành công", null, "Kết quả thi đã được lưu thành công.");
                ((Stage) btnLuu.getScene().getWindow()).close();
            } else {
                thongBao(Alert.AlertType.ERROR, "Lỗi", null, "Không thể lưu kết quả thi. Vui lòng thử lại sau.");
            }

        } catch (Exception e) {
            // This will catch any remaining validation errors from the BUS layer
            thongBao(Alert.AlertType.ERROR, "Lỗi ", null, e.getMessage());
            log.error(e.getMessage());
        }
    }

    private boolean validateForm() {
        // Check mã lịch thi
        if (ketQua.getMaLT() == null || ketQua.getMaLT().trim().isEmpty()) {
            thongBao(Alert.AlertType.WARNING, "Thiếu thông tin", null, "Mã lịch thi không được để trống.");
            tfMaLT.requestFocus();
            return false;
        }

        // Check số báo danh
        if (ketQua.getSbd() == null || ketQua.getSbd().trim().isEmpty()) {
            thongBao(Alert.AlertType.WARNING, "Thiếu thông tin", null, "Số báo danh không được để trống.");
            tfSBD.requestFocus();
            return false;
        }

        // Check điểm
        if (tfDiem.getText() == null || tfDiem.getText().trim().isEmpty()) {
            thongBao(Alert.AlertType.WARNING, "Thiếu thông tin", null, "Điểm không được để trống.");
            tfDiem.requestFocus();
            return false;
        }

        try {
            int diem = Integer.parseInt(tfDiem.getText().trim());
            if (diem < 0) {
                thongBao(Alert.AlertType.WARNING, "Lỗi dữ liệu", null, "Điểm không được âm.");
                tfDiem.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            thongBao(Alert.AlertType.WARNING, "Lỗi dữ liệu", null, "Điểm phải là số nguyên.");
            tfDiem.requestFocus();
            return false;
        }

        // Check xếp loại
        if (tfXepLoai.getText() == null || tfXepLoai.getText().trim().isEmpty()) {
            thongBao(Alert.AlertType.WARNING, "Thiếu thông tin", null, "Vui lòng nhập xếp loại.");
            tfXepLoai.requestFocus();
            return false;
        }

        // Check ngày cấp chứng chỉ
        if (dpNgayCapCC.getValue() == null) {
            thongBao(Alert.AlertType.WARNING, "Thiếu thông tin", null, "Ngày cấp chứng chỉ không được để trống.");
            dpNgayCapCC.requestFocus();
            return false;
        }

        // Check ngày hết hạn
        if (dpNgayHetHan.getValue() == null) {
            thongBao(Alert.AlertType.WARNING, "Thiếu thông tin", null, "Ngày hết hạn không được để trống.");
            dpNgayHetHan.requestFocus();
            return false;
        }

        if (dpNgayHetHan.getValue().isBefore(dpNgayCapCC.getValue())) {
            thongBao(Alert.AlertType.WARNING, "Lỗi dữ liệu", null, "Ngày hết hạn phải sau ngày cấp chứng chỉ.");
            dpNgayHetHan.requestFocus();
            return false;
        }

        return true;
    }

    private void thongBao(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}