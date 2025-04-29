package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.PhieuDangKyBUS;
import com.hcmus.exammanagement.bus.ThanhToanBUS;
import com.hcmus.exammanagement.dto.HoaDonDTO;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ThanhToanController {

    @FXML private TextField searchField;
    @FXML private TableView<PhieuDangKyDTO> DSPhieuDK;
    @FXML private TableColumn<PhieuDangKyDTO, String> colMaPhieu2;
    @FXML private TableColumn<PhieuDangKyDTO, String> colTen;
    @FXML private TableColumn<PhieuDangKyDTO, String> colLoaiKH;
    @FXML private TableColumn<PhieuDangKyDTO, String> colNgayDangKy;
    @FXML private TableColumn<PhieuDangKyDTO, String> colTrangThai;
    @FXML private TableColumn<PhieuDangKyDTO, Void> colAction;

    @FXML private TextField searchField1;
    @FXML private TableView<HoaDonDTO> LichSuThanhToan;
    @FXML private TableColumn<HoaDonDTO, String> colMaHoaDon;
    @FXML private TableColumn<HoaDonDTO, String> colMaPhieu;
    @FXML private TableColumn<HoaDonDTO, String> colGiamGia;
    @FXML private TableColumn<HoaDonDTO, String> colPTTT;
    @FXML private TableColumn<HoaDonDTO, String> colLoaiKH1;
    @FXML private TableColumn<HoaDonDTO, String> colTongTien;
    @FXML private TableColumn<HoaDonDTO, String> colTenKH;
    @FXML private TableColumn<HoaDonDTO, Void> colAction1;

    private final ObservableList<PhieuDangKyDTO> dsPhieu = FXCollections.observableArrayList();
    private final ObservableList<HoaDonDTO> dsHoaDon = FXCollections.observableArrayList();

    private final ThanhToanBUS thanhToanBUS = new ThanhToanBUS();
    private final PhieuDangKyBUS phieuDangKyBUS = new PhieuDangKyBUS();

    @FXML
    public void initialize() throws Exception {
        loadPhieuDangKy();
        loadHoaDon();
        setupPhieuDangKyTable();
        setupHoaDonTable();
        addActionButtonsToTableDSPhieu();
        addActionButtonsToTableLSThanhToan();
    }

    private void loadPhieuDangKy() throws Exception {
        dsPhieu.setAll(phieuDangKyBUS.layDSPhieuDangKy());
        DSPhieuDK.setItems(dsPhieu);
    }

    private void loadHoaDon() {
        dsHoaDon.setAll(thanhToanBUS.LayDsHoaDon());
        LichSuThanhToan.setItems(dsHoaDon);
    }

    private void setupPhieuDangKyTable() {
        colMaPhieu2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaPhieuDangKy()));
        colTen.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKhachHang().getHoTen()));
        colLoaiKH.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKhachHang().getLoai_kh()));
        colNgayDangKy.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getNgayLap()));
        colTrangThai.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTrangThai()));
    }

    private void setupHoaDonTable() {
        colMaHoaDon.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaHd()));
        colMaPhieu.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhieuDangKy().getMaPhieuDangKy()));
        colTenKH.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhieuDangKy().getKhachHang().getHoTen()));
        colGiamGia.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getSoTienGiam()));
        colPTTT.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPtThanhToan()));
        colLoaiKH1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhieuDangKy().getKhachHang().getLoai_kh()));
        colTongTien.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getTongTien()));
    }

    private void addActionButtonsToTableDSPhieu() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnLapHoaDon = new Button();
            private final Button btnXemChiTiet = new Button();
            private final HBox actionBox = new HBox(2);

            {
                btnLapHoaDon.setGraphic(new FontIcon("fas-print"));
                btnLapHoaDon.getStyleClass().add("action-button");
                btnLapHoaDon.setOnAction(event -> {
                    PhieuDangKyDTO phieu = getTableView().getItems().get(getIndex());
                    String loaiKH = phieu.getKhachHang().getLoai_kh();
                    if ("Đơn vị".equalsIgnoreCase(loaiKH)) {
                        openLapHoaDonDVPopup(phieu);
                    } else if ("Cá nhân".equalsIgnoreCase(loaiKH)) {
                        openLapHoaDonCNPopup(phieu);
                    }
                });

                btnXemChiTiet.setGraphic(new FontIcon("fas-eye"));
                btnXemChiTiet.getStyleClass().add("action-button");

                actionBox.getChildren().addAll(btnLapHoaDon, btnXemChiTiet);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionBox);
            }
        });
    }

    private void addActionButtonsToTableLSThanhToan() {
        colAction1.setCellFactory(param -> new TableCell<>() {
            private final Button btnDuyet = new Button();
            private final Button btnXem = new Button();
            private final Button btnXoa = new Button();
            private final HBox actionBox = new HBox(2);

            {
                btnDuyet.setGraphic(new FontIcon("fas-check-circle"));
                btnDuyet.getStyleClass().add("action-button");
                Tooltip tooltip = new Tooltip("Click để duyệt thanh toán đơn vị");
                Tooltip.install(btnDuyet, tooltip);

                tooltip.setShowDelay(Duration.millis(10));
                tooltip.setHideDelay(Duration.millis(300));

                btnDuyet.setOnAction(event -> {
                    HoaDonDTO hoaDon = getTableView().getItems().get(getIndex());
                    openDuyetThanhToanPopup(hoaDon);
                });

                btnXem.setGraphic(new FontIcon("fas-eye"));
                btnXem.getStyleClass().add("action-button");

                btnXoa.setGraphic(new FontIcon("fas-trash"));
                btnXoa.getStyleClass().add("action-button");
                btnXoa.setOnAction(event -> {
                    HoaDonDTO hoaDon = getTableView().getItems().get(getIndex());
                    if (thanhToanBUS.xoaHoaDon(hoaDon.getMaHd())) {
                        dsHoaDon.remove(hoaDon);
                    }
                });

                actionBox.getChildren().addAll(btnDuyet, btnXem, btnXoa);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HoaDonDTO hoaDon = getTableView().getItems().get(getIndex());
                    actionBox.getChildren().clear();

                    if (hoaDon.getMaTt() == null &&
                            "Đơn vị".equalsIgnoreCase(hoaDon.getPhieuDangKy().getKhachHang().getLoai_kh()) &&
                            "Chuyển khoản".equalsIgnoreCase(hoaDon.getPtThanhToan())) {
                        actionBox.getChildren().add(btnDuyet);
                    }

                    actionBox.getChildren().addAll(btnXem, btnXoa);
                    setGraphic(actionBox);
                }

            }
        });
    }

    private void openLapHoaDonDVPopup(PhieuDangKyDTO phieu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/ThanhToan/lap-hoa-don-donvi.fxml"));
            Parent root = loader.load();
            LapHoaDonController controller = loader.getController();

            if (controller != null) {
                controller.setPhieuDangKy(phieu);
            } else {
                System.out.println("Controller is null, cannot set PhieuDangKy.");
            }

            String maPhieu = phieu.getMaPhieuDangKy();
            String tenKH = phieu.getKhachHang().getHoTen();
            String title = "Lập hóa đơn cho PDK mã: " + maPhieu + " - Tên KH: " + tenKH;
            showPopup(root, title);
            loadHoaDon();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openLapHoaDonCNPopup(PhieuDangKyDTO phieu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/ThanhToan/lap-hoa-don-canhan.fxml"));
            Parent root = loader.load();
            // LapHoaDonController controller = loader.getController();
            // controller.setPhieuDangKy(phieu);
            String maPhieu = phieu.getMaPhieuDangKy();
            String tenKH = phieu.getKhachHang().getHoTen();
            String title = "Lập hóa đơn cho PDK mã: " + maPhieu + " - Tên KH: " + tenKH;

            showPopup(root, title);
            loadHoaDon();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDuyetThanhToanPopup(HoaDonDTO hoaDon) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/ThanhToan/duyet-thanh-toan.fxml"));
            Parent root = loader.load();
            // DuyetThanhToanController controller = loader.getController();
            // controller.setHoaDon(hoaDon);
            showPopup(root, "Duyệt Thanh Toán");
            loadHoaDon();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPopup(Parent root, String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}
