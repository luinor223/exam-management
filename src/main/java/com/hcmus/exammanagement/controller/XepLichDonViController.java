package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.*;
import com.hcmus.exammanagement.dto.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class XepLichDonViController {

    private final ObservableList<ThiSinhDTO> thiSinhList = FXCollections.observableArrayList();

    private PhieuDangKyDTO selectedPhieuDangKy;
    private LichThiDTO selectedLichThi;

    // Phiếu đăng ký table
    @FXML private TableView<PhieuDangKyDTO> phieuDangKyTable;
    @FXML private TableColumn<PhieuDangKyDTO, String> maPDKColumn;
    @FXML private TableColumn<PhieuDangKyDTO, String> tenDonViColumn;
    @FXML private TableColumn<PhieuDangKyDTO, Date> ngayLapColumn;
    @FXML private TableColumn<PhieuDangKyDTO, Integer> soLuongTSColumn;

    // Thí sinh table
    @FXML private TableView<ThiSinhDTO> thiSinhTable;
    @FXML private TableColumn<ThiSinhDTO, String> maThiSinhColumn;
    @FXML private TableColumn<ThiSinhDTO, String> hoTenColumn;
    @FXML private TableColumn<ThiSinhDTO, String> cccdColumn;

    // Lịch thi table
    @FXML private TableView<LichThiDTO> lichThiTable;
    @FXML private TableColumn<LichThiDTO, String> maLichThiColumn;
    @FXML private TableColumn<LichThiDTO, String> tenChungChiColumn;
    @FXML private TableColumn<LichThiDTO, String> ngayGioThiColumn;
    @FXML private TableColumn<LichThiDTO, Integer> soLuongHienTaiColumn;
    @FXML private TableColumn<LichThiDTO, Integer> soLuongToiDaColumn;

    // Labels
    @FXML private Label selectedPDKLabel;
    @FXML private Label selectedDonViLabel;
    @FXML private Label soLuongThiSinhLabel;
    @FXML private Label selectedLichThiLabel;
    @FXML private Label soLuongHienTaiLabel;
    @FXML private Label soLuongToiDaLabel;

    // Buttons
    @FXML private Button xepLichButton;

    @FXML
    public void initialize() {
        setupPhieuDangKyTable();
        setupThiSinhTable();
        setupLichThiTable();

        loadPhieuDangKyData();
        loadLichThiData();

        xepLichButton.setDisable(true);
    }

    private void setupPhieuDangKyTable() {
        maPDKColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaPhieuDangKy()));
        tenDonViColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKhachHang().getHoTen()));
        ngayLapColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNgayLap()));
        soLuongTSColumn.setCellValueFactory(data -> {
            try {
                List<ChiTietPDKDTO> chiTietList = ChiTietPDKBUS.layDSChiTietPDKTheoPDK(data.getValue().getMaPhieuDangKy());
                return new SimpleIntegerProperty(chiTietList.size()).asObject();
            } catch (Exception e) {
                return new SimpleIntegerProperty(0).asObject();
            }
        });

        phieuDangKyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedPhieuDangKy = newSelection;
                selectedPDKLabel.setText(newSelection.getMaPhieuDangKy());
                selectedDonViLabel.setText(newSelection.getKhachHang().getHoTen());
                loadThiSinhData(newSelection.getMaPhieuDangKy());

                soLuongThiSinhLabel.setText(String.valueOf(thiSinhList.size()));

                // If a lịch thi is already selected, enable the xếp lịch button
                if (selectedLichThi != null) {
                    xepLichButton.setDisable(false);
                } else {
                    // Reset lịch thi labels
                    selectedLichThiLabel.setText("--");
                    soLuongHienTaiLabel.setText("--");
                    soLuongToiDaLabel.setText("--");
                    xepLichButton.setDisable(true);
                }
            }
        });
    }

    private void setupThiSinhTable() {
        maThiSinhColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaThiSinh()));
        hoTenColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoTen()));
        cccdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCccd()));
    }

    private void setupLichThiTable() {
        maLichThiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaLichThi()));
        tenChungChiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getChungChi().getTenChungChi()));
        ngayGioThiColumn.setCellValueFactory(data -> {
            Timestamp ngayGioThi = data.getValue().getNgayGioThi();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(sdf.format(ngayGioThi));
        });
        soLuongHienTaiColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSoLuongHienTai()).asObject());
        soLuongToiDaColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSoLuongToiDa()).asObject());

        lichThiTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedLichThi = newSelection;
                selectedLichThiLabel.setText(newSelection.getChungChi().getTenChungChi() + " - " + 
                        new SimpleDateFormat("dd/MM/yyyy HH:mm").format(newSelection.getNgayGioThi()));
                soLuongHienTaiLabel.setText(String.valueOf(newSelection.getSoLuongHienTai()));
                soLuongToiDaLabel.setText(String.valueOf(newSelection.getSoLuongToiDa()));

                // Enable xếp lịch button if both phiếu đăng ký and lịch thi are selected
                xepLichButton.setDisable(selectedPhieuDangKy == null);
            }
        });
    }

    private void loadPhieuDangKyData() {
        try {
            List<PhieuDangKyDTO> phieuDangKyDTOList = PhieuDangKyBUS.layDSPDKTheoTrangThai("Chờ xếp lịch");
            // Data
            ObservableList<PhieuDangKyDTO> phieuDangKyList = FXCollections.observableArrayList(phieuDangKyDTOList);
            phieuDangKyTable.setItems(phieuDangKyList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách phiếu đăng ký", e.getMessage());
        }
    }

    private void loadThiSinhData(String maPDK) {
        try {
            thiSinhList.clear();
            thiSinhList.addAll(ThiSinhBUS.layDSThiSinhBangPDK(maPDK));
            thiSinhTable.setItems(thiSinhList);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách thí sinh", e.getMessage());
        }
    }

    private void loadLichThiData() {
        try {
            List<LichThiDTO> lichThiDTOList = LichThiBUS.layDSLichThiMoi();
            ObservableList<LichThiDTO> lichThiList = FXCollections.observableArrayList(lichThiDTOList);
            lichThiTable.setItems(lichThiList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách lịch thi", e.getMessage());
        }
    }

    @FXML
    public void handleXepLich() {
        if (selectedPhieuDangKy == null || selectedLichThi == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Thiếu thông tin", 
                    "Vui lòng chọn phiếu đăng ký và lịch thi.");
            return;
        }

        try {
            List<ChiTietPDKDTO> chiTietList = ChiTietPDKBUS.layDSChiTietPDKTheoPDK(selectedPhieuDangKy.getMaPhieuDangKy());

            int candidatesToSchedule = 0;
            for (ChiTietPDKDTO chiTiet : chiTietList) {
                if (chiTiet.getMaLichThi() == null) {
                    candidatesToSchedule++;
                }
            }

            if (candidatesToSchedule == 0) {
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Không có thí sinh cần xếp lịch", 
                        "Tất cả thí sinh trong phiếu đăng ký này đã được xếp lịch.");
                return;
            }

            int soLuongHienTai = selectedLichThi.getSoLuongHienTai();
            int soLuongToiDa = selectedLichThi.getSoLuongToiDa();

            if (soLuongHienTai + candidatesToSchedule > soLuongToiDa) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Lịch thi không đủ chỗ", 
                        "Lịch thi không đủ chỗ cho tất cả thí sinh. Số lượng hiện tại: " + 
                        soLuongHienTai + ", Số lượng tối đa: " + soLuongToiDa + 
                        ", Số lượng thí sinh cần xếp lịch: " + candidatesToSchedule);
                return;
            }

            int scheduledCount = 0;
            for (ChiTietPDKDTO chiTiet : chiTietList) {
                chiTiet.setMaLichThi(selectedLichThi.getMaLichThi());
                ChiTietPDKBUS.capNhatChiTietPDK(chiTiet);
                scheduledCount++;
            }

            PhieuDangKyBUS.capNhatTrangThai(selectedPhieuDangKy.getMaPhieuDangKy(), "Chờ xử lý");

            loadPhieuDangKyData();
            loadLichThiData();

            selectedLichThi = null;

            selectedLichThiLabel.setText("--");
            soLuongHienTaiLabel.setText("--");
            soLuongToiDaLabel.setText("--");
            selectedPDKLabel.setText("--");
            selectedDonViLabel.setText("--");
            soLuongThiSinhLabel.setText("--");
            thiSinhList.clear();

            xepLichButton.setDisable(true);

            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xếp lịch thành công", 
                    "Đã xếp lịch thành công cho " + scheduledCount + " thí sinh.");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xếp lịch", e.getMessage());
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
