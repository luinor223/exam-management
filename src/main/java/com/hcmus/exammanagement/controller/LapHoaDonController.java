package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.ThongTinLapHDBUS;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;
import com.hcmus.exammanagement.dto.ThongTinLapHDDTO;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Setter;

public class LapHoaDonController {

    @FXML private TableView<ThongTinLapHDDTO> tableThongTinLapHoaDon;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colMaPhieuDangKy;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colMaKhachHang;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colLoaiKhachHang;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colTenChungChi;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colNgayGioThi;
    @FXML private TableColumn<ThongTinLapHDDTO, Float> colLePhi;
    @FXML private TableColumn<ThongTinLapHDDTO, Integer> colSoLuongThiSinh;
    @FXML private TableColumn<ThongTinLapHDDTO, Float> colTongTien;

    @FXML private TextField tamTinh;
    @FXML private TextField giamGia;
    @FXML private TextField tongTien;
    @FXML private ComboBox<String> phuongThuc;
    @FXML private TextField emailGuiHoaDon;
    @FXML private TextField maThanhToan;

    @FXML private Button btnGuiHoaDon;
    @FXML private Button btnThanhToan;

    private ObservableList<ThongTinLapHDDTO> danhSachLapHD;

    private PhieuDangKyDTO phieuDangKy;

    public void setPhieuDangKy(PhieuDangKyDTO phieuDangKy) {
        this.phieuDangKy = phieuDangKy;
        loadData();
    }

    private ThongTinLapHDBUS thongTinLapHDBUS;

    @FXML
    public void initialize() {
        thongTinLapHDBUS = new ThongTinLapHDBUS();

        colMaPhieuDangKy.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaPhieuDangKy()));
        colMaKhachHang.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaKhachHang()));
        colLoaiKhachHang.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLoaiKhachHang()));
        colTenChungChi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTenChungChi()));
        colNgayGioThi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNgayGioThi()));
        colLePhi.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getLePhi()).asObject());
        colSoLuongThiSinh.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSoLuongThiSinh()).asObject());
        colTongTien.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getTongTien()).asObject());

        danhSachLapHD = FXCollections.observableArrayList();
        tableThongTinLapHoaDon.setItems(danhSachLapHD);

        phuongThuc.setItems(FXCollections.observableArrayList("Tiền mặt", "Chuyển khoản", "MOMO", "VNPAY"));
        phuongThuc.getSelectionModel().selectFirst();

        btnGuiHoaDon.setOnAction(e -> guiHoaDon());
        btnThanhToan.setOnAction(e -> thanhToan());
    }

    private void loadData() {
        danhSachLapHD.setAll(thongTinLapHDBUS.getAllThongTinLapHDbyMapdk(phieuDangKy.getMaPhieuDangKy()));
    }

    private void guiHoaDon() {
        String email = emailGuiHoaDon.getText();
        if (email == null || email.isBlank()) {
            showAlert("Lỗi", "Vui lòng nhập email để gửi hóa đơn.");
            return;
        }

        showAlert("Thành công", "Đã gửi hóa đơn tới " + email);
    }

    private void thanhToan() {
        String phuongThucTT = phuongThuc.getValue();
        String maTT = maThanhToan.getText();
        if (maTT == null || maTT.isBlank()) {
            showAlert("Lỗi", "Vui lòng nhập mã thanh toán.");
            return;
        }
        showAlert("Thành công", "Thanh toán thành công bằng " + phuongThucTT);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}