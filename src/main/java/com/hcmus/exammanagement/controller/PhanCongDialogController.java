package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.ChiTietPhongThiBUS;
import com.hcmus.exammanagement.bus.GiamThiBUS;
import com.hcmus.exammanagement.bus.PhongBUS;
import com.hcmus.exammanagement.dto.ChiTietPhongThiDTO;
import com.hcmus.exammanagement.dto.GiamThiDTO;
import com.hcmus.exammanagement.dto.LichThiDTO;
import com.hcmus.exammanagement.dto.PhongDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class PhanCongDialogController {

    @FXML private Button btnHuy;
    @FXML private Button btnLuu;
    @FXML private ComboBox<GiamThiDTO> giamThiCombo;
    @FXML private TextField maLichThiField;
    @FXML private ComboBox<PhongDTO> phongCombo;
    @FXML private TextField soLuongToiDaField;

    private LichThiDTO lichThi;

    @FXML
    private void initialize() {
        phongCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(PhongDTO phong) {
                return phong != null ? phong.getTenPhong() + " (" + phong.getSoGhe() + " chỗ)" : "";
            }

            @Override
            public PhongDTO fromString(String string) {
                return null;
            }
        });

        giamThiCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(GiamThiDTO giamThi) {
                return giamThi != null ? giamThi.getHoTen() + " (" + giamThi.getSdt() + ")" : "";
            }

            @Override
            public GiamThiDTO fromString(String string) {
                return null;
            }
        });

        phongCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                soLuongToiDaField.setText(newValue.getSoGhe().toString());
            } else {
                soLuongToiDaField.clear();
            }
        });
    }

    public void setLichThi(LichThiDTO lichThi) {
        this.lichThi = lichThi;

        // If UI is already initialized, populate it with data
        if (maLichThiField != null) {
            maLichThiField.setText(lichThi.getMaLichThi());
            loadPhongTrong();
            loadGiamThiRanh();
        }
    }

    private void loadPhongTrong() {
        try {
            List<PhongDTO> availableRooms = PhongBUS.layDSPhongTrong(lichThi.getNgayGioThi(), lichThi.getThoiLuongThi());
            phongCombo.setItems(FXCollections.observableArrayList(availableRooms));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách phòng trống", e.getMessage());
        }
    }

    private void loadGiamThiRanh() {
        try {
            List<GiamThiDTO> supervisors = GiamThiBUS.layDSGiamThiRanh(this.lichThi.getNgayGioThi(), this.lichThi.getThoiLuongThi());
            giamThiCombo.setItems(FXCollections.observableArrayList(supervisors));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách giám thị", e.getMessage());
        }
    }

    @FXML
    void btnHuy(ActionEvent event) {
        // Close the dialog
        ((Stage) btnHuy.getScene().getWindow()).close();
    }

    @FXML
    void btnLuu(ActionEvent event) {
        try {
            // Validate selections
            if (phongCombo.getSelectionModel().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Chưa chọn phòng", "Vui lòng chọn phòng cần phân công");
                return;
            }

            if (giamThiCombo.getSelectionModel().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Chưa chọn giám thị", "Vui lòng chọn giám thị cần phân công");
                return;
            }

            PhongDTO selectedPhong = phongCombo.getValue();
            GiamThiDTO selectedGiamThi = giamThiCombo.getValue();

            if (selectedPhong.getSoGhe() < Integer.parseInt(soLuongToiDaField.getText())) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số ghế không hợp lệ", "Vui lòng chọn phòng có số ghế lớn hơn hoặc bằng số lượng tối đa");
                return;
            }

            ChiTietPhongThiDTO chiTietPhongThi = new ChiTietPhongThiDTO(
                    lichThi.getMaLichThi(),
                    selectedPhong,
                    selectedGiamThi,
                    0, // soLuongHienTai
                    Integer.parseInt(soLuongToiDaField.getText()) // soLuongToiDa
            );

            ChiTietPhongThiBUS.themChiTietPhongThi(chiTietPhongThi);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phân công phòng và giám thị thành công", null);
            ((Stage) btnLuu.getScene().getWindow()).close();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể phân công phòng và giám thị", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}