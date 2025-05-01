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
    @FXML private ComboBox<GiamThiDTO> giamThiComboBox;
    @FXML private TextField maLichThiField;
    @FXML private ComboBox<PhongDTO> phongComboBox;
    @FXML private TextField soLuongToiDaField;

    private LichThiDTO lichThi;
    private final ChiTietPhongThiBUS chiTietPhongThiBUS = new ChiTietPhongThiBUS();

    @FXML
    private void initialize() {
        phongComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(PhongDTO phong) {
                return phong != null ? phong.getTenPhong() + " (" + phong.getSoGhe() + " chỗ)" : "";
            }

            @Override
            public PhongDTO fromString(String string) {
                return null;
            }
        });

        giamThiComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(GiamThiDTO giamThi) {
                return giamThi != null ? giamThi.getHoTen() + " (" + giamThi.getSdt() + ")" : "";
            }

            @Override
            public GiamThiDTO fromString(String string) {
                return null;
            }
        });

        phongComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
            phongComboBox.setItems(FXCollections.observableArrayList(availableRooms));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách phòng trống", e.getMessage());
        }
    }

    private void loadGiamThiRanh() {
        try {
            List<GiamThiDTO> supervisors = GiamThiBUS.layDSGiamThiRanh(this.lichThi.getNgayGioThi(), this.lichThi.getThoiLuongThi());
            giamThiComboBox.setItems(FXCollections.observableArrayList(supervisors));
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
            if (phongComboBox.getSelectionModel().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Chưa chọn phòng", "Vui lòng chọn phòng cần phân công");
                return;
            }

            if (giamThiComboBox.getSelectionModel().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Chưa chọn giám thị", "Vui lòng chọn giám thị cần phân công");
                return;
            }

            PhongDTO selectedPhong = phongComboBox.getValue();
            GiamThiDTO selectedGiamThi = giamThiComboBox.getValue();

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

            boolean success = chiTietPhongThiBUS.themChiTietPhongThi(chiTietPhongThi);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phân công phòng và giám thị thành công", null);
                ((Stage) btnLuu.getScene().getWindow()).close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể phân công phòng và giám thị", null);
            }
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