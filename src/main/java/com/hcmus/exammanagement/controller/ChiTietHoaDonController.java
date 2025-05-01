package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.ThongTinLapHDBUS;
import com.hcmus.exammanagement.dto.HoaDonDTO;
import com.hcmus.exammanagement.dto.KhachHangDTO;
import com.hcmus.exammanagement.dto.ThongTinLapHDDTO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.NumberFormat;
import java.util.Locale;


public class ChiTietHoaDonController {

    @FXML private Label labelNgayLap;
    @FXML private TableView<ThongTinLapHDDTO> tableThongTinLapHoaDon;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colMaPhieuDangKy;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colMaKhachHang;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colLoaiKhachHang;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colTenChungChi;
    @FXML private TableColumn<ThongTinLapHDDTO, String> colNgayGioThi;
    @FXML private TableColumn<ThongTinLapHDDTO, Double> colLePhi;
    @FXML private TableColumn<ThongTinLapHDDTO, Integer> colSoLuongThiSinh;
    @FXML private TableColumn<ThongTinLapHDDTO, Double> colTongTien;
    private ObservableList<ThongTinLapHDDTO> danhSachLapHD;

    @FXML private TextField giamGia;
    @FXML private TextField tongTien;
    @FXML private TextField phuongThuc;
    @FXML private Label labelTenKH;
    @FXML private Label labelLoaiKH;
    @FXML private Label labelSDT;
    @FXML private Label labelMaPDK;
    @FXML private Label labelNgayDK;

    private HoaDonDTO hoaDon;
    private KhachHangDTO khachHang;

    public void setHoaDon(HoaDonDTO hoaDon) {
        this.hoaDon = hoaDon;
        loadHoaDonDetails();
        loadData();
    }

    public void initialize() {

        colMaPhieuDangKy.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaPhieuDangKy()));
        colMaKhachHang.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaKhachHang()));
        colLoaiKhachHang.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLoaiKhachHang()));
        colTenChungChi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTenChungChi()));
        colNgayGioThi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNgayGioThi()));

        colLePhi.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getLePhi()).asObject());
        colSoLuongThiSinh.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSoLuongThiSinh()).asObject());

        colTongTien.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTongTien()).asObject());

        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);

        colLePhi.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
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
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item) + "đ");
                }
            }
        });

        danhSachLapHD = FXCollections.observableArrayList();
        tableThongTinLapHoaDon.setItems(danhSachLapHD);
    }

    public void setKhachHang(KhachHangDTO khachHang) {
        this.khachHang = khachHang;
        loadKhachHangDetails();
    }

    private void loadData() {
        danhSachLapHD.setAll(ThongTinLapHDBUS.LayThongTinLapHDbyMapdk(hoaDon.getPhieuDangKy().getMaPhieuDangKy()));
    }

    private void loadHoaDonDetails() {
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);

        labelNgayLap.setText(hoaDon.getNgayLap().toString());
        giamGia.setText(currencyFormat.format(hoaDon.getSoTienGiam()) + "đ");
        tongTien.setText(currencyFormat.format(hoaDon.getTongTien()) + "đ");
        phuongThuc.setText(hoaDon.getPtThanhToan());
    }

    private void loadKhachHangDetails() {
        labelTenKH.setText(khachHang.getHoTen());
        labelLoaiKH.setText(khachHang.getLoai_kh());
        labelSDT.setText(khachHang.getSdt());
        labelMaPDK.setText(hoaDon.getPhieuDangKy().getMaPhieuDangKy());
        labelNgayDK.setText(hoaDon.getPhieuDangKy().getNgayLap().toString());
    }
}

