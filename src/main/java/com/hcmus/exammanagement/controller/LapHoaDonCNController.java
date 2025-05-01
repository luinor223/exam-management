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

import java.text.NumberFormat;
import java.util.Locale;

public class LapHoaDonCNController {

    @FXML
    private TableView<ThongTinLapHDDTO> tableThongTinLapHoaDon;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colMaPhieuDangKy;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colMaKhachHang;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colLoaiKhachHang;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colTenChungChi;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colNgayGioThi;
    @FXML private TableColumn<ThongTinLapHDDTO, Float> colLePhi;
    @FXML private TableColumn<ThongTinLapHDDTO, Integer> colSoLuongThiSinh;
    @FXML private TableColumn<ThongTinLapHDDTO, Float> colTongTien;

    @FXML private TextField tongTien;
    @FXML private Label labelNgayLap;

    @FXML private Button btnHuy;
    @FXML private Button btnThanhToan;

    private float tongThanhToanThucTe = 0;


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

        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0); // 100,000

        colLePhi.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item) + "đ");
                }
            }
        });

        colTongTien.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item) + "đ");
                }
            }
        });


        labelNgayLap.setText(java.time.LocalDate.now().toString());
        danhSachLapHD = FXCollections.observableArrayList();
        tableThongTinLapHoaDon.setItems(danhSachLapHD);

        btnHuy.setOnAction(e -> huyHoaDon());
        btnThanhToan.setOnAction(e -> thanhToan());
    }

    private void loadData() {
        danhSachLapHD.setAll(thongTinLapHDBUS.LayThongTinLapHDbyMapdk(phieuDangKy.getMaPhieuDangKy()));
        tinhToanTongTien();
    }

    private void tinhToanTongTien() {
        for (ThongTinLapHDDTO item : danhSachLapHD) {
            tongThanhToanThucTe += item.getTongTien();
        }

        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);

        tongTien.setText(currencyFormat.format(tongThanhToanThucTe) + "đ");
    }

    private void huyHoaDon() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }

    private void thanhToan() {
        String phuongThucTT = "Tiền mặt";

        HoaDonDTO hoaDon = new HoaDonDTO(
                null,
                tongThanhToanThucTe,
                phuongThucTT,
                0,
                java.sql.Date.valueOf(labelNgayLap.getText()),
                "NV000001",
                phieuDangKy,
                null
        );

        phieuDangKyBUS.capNhatTrangThai(phieuDangKy.getMaPhieuDangKy(), "Đã xác nhận");
        thanhToanBUS.taoHoaDon(hoaDon);
        showAlert("Thành công", "Xác nhận thanh toán!");

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