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

public class LapHoaDonDVController {

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

    private float tongTamTinhThucTe = 0;
    private float troGiaThucTe = 0;
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
        tongTamTinhThucTe = 0;
        int tongSoLuongThiSinh = 0;
        boolean laDonVi = false;

        for (ThongTinLapHDDTO item : danhSachLapHD) {
            tongTamTinhThucTe += item.getTongTien();
            tongSoLuongThiSinh += item.getSoLuongThiSinh();

            if ("Đơn vị".equalsIgnoreCase(item.getLoaiKhachHang())) {
                laDonVi = true;
            }
        }

        // Khách hàng là đơn vị sẽ được trợ giá 10%, nếu số thí sinh dự thi trên 20 sẽ được giảm tối đa 15%.
        troGiaThucTe = 0;
        String troGiaMessage = "";

        if (laDonVi) {
            troGiaThucTe = 0.10f * tongTamTinhThucTe;

            if (tongSoLuongThiSinh > 20) {
                float tongGiamToiDa = 0.15f * tongTamTinhThucTe;
                if (troGiaThucTe > tongGiamToiDa) {
                    troGiaThucTe = tongGiamToiDa;
                    if (troGiaThucTe < 0) troGiaThucTe = 0;
                }
                troGiaMessage = "Trợ giá 15%";
            } else {
                troGiaMessage = "Trợ giá 10%";
            }
        }

        tongThanhToanThucTe = Math.max(tongTamTinhThucTe - troGiaThucTe, 0);

        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);

        tamTinh.setText(currencyFormat.format(tongTamTinhThucTe) + "đ");
        giamGia.setText(currencyFormat.format(troGiaThucTe) + "đ");
        tongTien.setText(currencyFormat.format(tongThanhToanThucTe) + "đ");
        labelTroGia.setText(troGiaMessage);
    }

    private void huyHoaDon() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
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
                tongThanhToanThucTe,
                phuongThucTT,
                troGiaThucTe,
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