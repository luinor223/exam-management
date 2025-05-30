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
//    @FXML private TextField emailGuiHoaDon;
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
        loadTTLapHD();
    }


    @FXML
    public void initialize() {

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
        phuongThuc.getSelectionModel().select("Chuyển khoản");

        btnHuy.setOnAction(e -> btnHuy());
        btnThanhToan.setOnAction(e -> btnTaoHoaDon());
    }

    private void loadTTLapHD() {
        danhSachLapHD.setAll(ThongTinLapHDBUS.LayThongTinLapHDbyMapdk(phieuDangKy.getMaPhieuDangKy()));
        tinhToanTongTien();
    }

    private void tinhToanTongTien() {
        tongTamTinhThucTe = 0;
        for (ThongTinLapHDDTO item : danhSachLapHD) {
            tongTamTinhThucTe += item.getTongTien();
        }

        troGiaThucTe = ThanhToanBUS.KiemTraTroGia(danhSachLapHD);
        tongThanhToanThucTe = Math.max(tongTamTinhThucTe - troGiaThucTe, 0);
        String troGiaMessage = ThanhToanBUS.getMoTaTroGia(danhSachLapHD);

        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);

        tamTinh.setText(currencyFormat.format(tongTamTinhThucTe) + "đ");
        giamGia.setText(currencyFormat.format(troGiaThucTe) + "đ");
        tongTien.setText(currencyFormat.format(tongThanhToanThucTe) + "đ");
        labelTroGia.setText(troGiaMessage);
    }


    private void btnHuy() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }

    private void btnTaoHoaDon() {
        String phuongThucTT = phuongThuc.getValue();
        String maTT = maThanhToan.getText();
        String email = phieuDangKy.getKhachHang().getEmail();

//        if (email == null || email.trim().isEmpty()) {
//            hienThongBao("Lỗi", "Vui lòng nhập email");
//            return;
//        }
//
//        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
//            hienThongBao("Lỗi", "Email không hợp lệ");
//            return;
//        }

        HoaDonDTO hoaDon = new HoaDonDTO(
                null,
                tongThanhToanThucTe,
                phuongThucTT,
                troGiaThucTe,
                java.sql.Date.valueOf(labelNgayLap.getText()),
                null,
                phieuDangKy,
                null
        );

        if ((maTT == null || maTT.isBlank()) && phuongThucTT.equals("Chuyển khoản")) {
            // Nếu chưa có mã thanh toán thì đưa vào danh sách chờ duyệt
            PhieuDangKyBUS.capNhatTrangThai(phieuDangKy.getMaPhieuDangKy(), "Chờ xét duyệt");
            ThanhToanBUS.taoHoaDon(hoaDon);
            hienThongBao("Chờ duyệt", "Chưa có mã thanh toán.\nHóa đơn đã được đưa vào danh sách chờ duyệt.\nĐã gửi hoá đơn tới " + email);

            Stage stage = (Stage) btnThanhToan.getScene().getWindow();
            stage.close();
            return;
        }

        // Nếu có mã thanh toán
        if (!(maTT == null || maTT.isBlank())) {
            hoaDon.setMaTt(maTT);
        }
        PhieuDangKyBUS.capNhatTrangThai(phieuDangKy.getMaPhieuDangKy(), "Đã xác nhận");
        ThanhToanBUS.taoHoaDon(hoaDon);
        hienThongBao("Thành công", "Xác nhận thanh toán!\nĐã gửi hoá đơn tới " + email);

        Stage stage = (Stage) btnThanhToan.getScene().getWindow();
        stage.close();
    }

    private void hienThongBao(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}