package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.PhieuDangKyBUS;
import com.hcmus.exammanagement.bus.ThanhToanBUS;
import com.hcmus.exammanagement.bus.ThongTinLapHDBUS;
import com.hcmus.exammanagement.dto.HoaDonDTO;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;
import com.hcmus.exammanagement.dto.ThongTinLapHDDTO;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;

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
    @FXML private Label labelTroGia;
    @FXML private Label labelNgayLap;

    @FXML private Button btnHuy;
    @FXML private Button btnThanhToan;

    private ObservableList<ThongTinLapHDDTO> danhSachLapHD;

    private PhieuDangKyDTO phieuDangKy;

    public void setPhieuDangKy(PhieuDangKyDTO phieuDangKy) {
        this.phieuDangKy = phieuDangKy;
        loadData();
    }

    private ThongTinLapHDBUS thongTinLapHDBUS;
    private final ThanhToanBUS thanhToanBUS = new ThanhToanBUS();
    private final PhieuDangKyBUS phieuDangKyBUS = new PhieuDangKyBUS();


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

        labelNgayLap.setText(java.time.LocalDate.now().toString());
        danhSachLapHD = FXCollections.observableArrayList();
        tableThongTinLapHoaDon.setItems(danhSachLapHD);

        phuongThuc.setItems(FXCollections.observableArrayList("Tiền mặt", "Chuyển khoản"));
        phuongThuc.getSelectionModel().selectFirst();

        btnHuy.setOnAction(e -> huyHoaDon());
        btnThanhToan.setOnAction(e -> thanhToan());
    }

    private void loadData() {
        danhSachLapHD.setAll(thongTinLapHDBUS.getAllThongTinLapHDbyMapdk(phieuDangKy.getMaPhieuDangKy()));
        tinhToanTongTien();
    }

    private void tinhToanTongTien() {
        float tongTamTinh = 0;
        int tongSoLuongThiSinh = 0;
        boolean laDonVi = false;

        for (ThongTinLapHDDTO item : danhSachLapHD) {
            tongTamTinh += item.getTongTien();
            tongSoLuongThiSinh += item.getSoLuongThiSinh();

            if ("Đơn vị".equalsIgnoreCase(item.getLoaiKhachHang())) {
                laDonVi = true;
            }
        }

        tamTinh.setText(String.format("%.2f", tongTamTinh));

        // Khách hàng là đơn vị sẽ được trợ giá 10%, nếu số thí sinh dự thi trên 20 sẽ được giảm tối đa 15%.
        float troGia = 0;
        String troGiaMessage = "";

        if (laDonVi) {
            troGia = 0.10f * tongTamTinh;

            if (tongSoLuongThiSinh > 20) {
                float tongGiamToiDa = 0.15f * tongTamTinh;
                float tongGiam = troGia;

                if (tongGiam > tongGiamToiDa) {
                    troGia = tongGiamToiDa;
                    if (troGia < 0) troGia = 0;
                }
                troGiaMessage = String.format("Trợ giá 15%");
            } else {
                troGiaMessage = "Trợ giá 10%";
            }
        }
        giamGia.setText(String.format("%.2f", troGia));

        float tongThanhToan = Math.max(tongTamTinh - troGia, 0);
        tongTien.setText(String.format("%.2f", tongThanhToan));
        labelTroGia.setText(troGiaMessage);
    }

    private void huyHoaDon() {

    }

    private void thanhToan() {
        String phuongThucTT = phuongThuc.getValue();
        String maTT = maThanhToan.getText();
        String email = emailGuiHoaDon.getText();

        if (email == null || email.isBlank()) {
            showAlert("Lỗi", "Vui lòng nhập email để gửi hóa đơn.");
            return;
        }

        HoaDonDTO hoaDon = new HoaDonDTO(
                null,
                Float.parseFloat(tongTien.getText()),
                phuongThucTT,
                Float.parseFloat(giamGia.getText()),
                java.sql.Date.valueOf(labelNgayLap.getText()),
                "NV000001",
                phieuDangKy,
                null
        );

        if (maTT == null || maTT.isBlank()) {
            // Nếu chưa có mã thanh toán thì đưa vào danh sách chờ duyệt
            phieuDangKyBUS.capNhatTrangThai(phieuDangKy.getMaPhieuDangKy(), "Chờ xét duyệt");
            thanhToanBUS.taoHoaDon(hoaDon);
            showAlert("Chờ duyệt", "Chưa có mã thanh toán.\nHóa đơn đã được đưa vào danh sách chờ duyệt.\nĐã gửi hoá đơn tới " + email);

            Stage stage = (Stage) btnThanhToan.getScene().getWindow();
            stage.close();
            return;
        }

        // Nếu có mã thanh toán
        hoaDon.setMaTt(maTT);
        phieuDangKyBUS.capNhatTrangThai(phieuDangKy.getMaPhieuDangKy(), "Đã xác nhận");
        thanhToanBUS.taoHoaDon(hoaDon);
        showAlert("Thành công", "Xác nhận thanh toán!\nĐã gửi hoá đơn tới " + email);

        Stage stage = (Stage) btnThanhToan.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}