package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.PhieuGiaHanBUS;
import com.hcmus.exammanagement.dao.ChiTietPDKDAO;
import com.hcmus.exammanagement.dto.LichThiDTO;
import com.hcmus.exammanagement.dto.PhieuGiaHanDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Getter;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class DialogThemGiaHanController {

    @FXML private TableView<LichThiDTO> tableLichThi;
    @FXML private TableColumn<LichThiDTO, String> colMaLichThi;
    @FXML private TableColumn<LichThiDTO, Timestamp> colNgayGio;
    @FXML private TableColumn<LichThiDTO, Integer> colThoiLuong;
    @FXML private TableColumn<LichThiDTO, String> colChungChi;
    @FXML private TableColumn<LichThiDTO, String> colSoLuong;

    @Getter
    private LichThiDTO selectedLichThi;

    @FXML private ComboBox<String> cbMaCTPDK;
    @FXML private TextField txtLoaiGH;
    @FXML private TextField txtPhiGH;
    @FXML private TextField txtNhanVienTao;
    @FXML private CheckBox chkDaThanhToan;

    public void initLichThiTable(List<LichThiDTO> lichThiList) {
        colMaLichThi.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getMaLichThi())
        );

        colNgayGio.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getNgayGioThi())
        );

        colThoiLuong.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getThoiLuongThi())
        );

        colChungChi.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getChungChi().getTenChungChi())
        );

        colSoLuong.setCellValueFactory(cell -> {
            String value = cell.getValue().getSoLuongHienTai() + " / " + cell.getValue().getSoLuongToiDa();
            return new SimpleStringProperty(value);
        });

        tableLichThi.setItems(FXCollections.observableArrayList(lichThiList));

        tableLichThi.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedLichThi = newSel;
        });
    }


    public void setDanhSachCTPDK(List<String> maCTPDKList) {
        cbMaCTPDK.setItems(FXCollections.observableArrayList(maCTPDKList));
    }


    @FXML
    private void handleLuu() {
        String maCTPDK = cbMaCTPDK.getValue();
        String loaiGH = txtLoaiGH.getText().trim();
        String nhanVienTao = txtNhanVienTao.getText().trim();
        double phiGH;

        if (selectedLichThi == null) {
            showAlert("Lỗi", "Vui lòng chọn lịch thi từ bảng.");
            return;
        }

        try {
            phiGH = Double.parseDouble(txtPhiGH.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Phí gia hạn phải là số.");
            return;
        }

        if (maCTPDK == null || loaiGH.isEmpty()) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        PhieuGiaHanDTO newPGH = new PhieuGiaHanDTO(
                null, loaiGH, phiGH, nhanVienTao, chkDaThanhToan.isSelected(), maCTPDK
        );

        try {
            if (PhieuGiaHanBUS.taoPhieuGiaHan(newPGH)) {
                ChiTietPDKDAO.updateLichThiForCTPDK(maCTPDK, selectedLichThi.getMaLichThi());
                showAlert("Thành công", "Thêm phiếu gia hạn thành công.");
                closeDialog();
            } else {
                showAlert("Lỗi", "Không thể thêm phiếu gia hạn.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // hoặc ghi log nếu bạn có hệ thống logging
            showAlert("Lỗi", "Đã xảy ra lỗi khi thêm phiếu gia hạn: " + e.getMessage());
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
