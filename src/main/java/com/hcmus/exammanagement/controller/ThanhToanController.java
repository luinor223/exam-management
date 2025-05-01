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
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

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
    @FXML private ComboBox<String> filterComboBox;

    private final ObservableList<PhieuDangKyDTO> dsPhieu = FXCollections.observableArrayList();
    private final ObservableList<HoaDonDTO> dsHoaDon = FXCollections.observableArrayList();

    private final ThanhToanBUS thanhToanBUS = new ThanhToanBUS();
    private final PhieuDangKyBUS phieuDangKyBUS = new PhieuDangKyBUS();

    @FXML
    public void initialize() throws Exception {
        loadPhieuDangKyChoTT();
        loadHoaDon();
        setupPhieuDangKyTable();
        setupHoaDonTable();
        addActionButtonsToTableDSPhieu();
        addActionButtonsToTableLSThanhToan();
        filterComboBox.getSelectionModel().select("Tất cả");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                filterPhieuDangKy(newValue);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        searchField1.textProperty().addListener((observable, oldValue, newValue) -> {
            filterHoaDon(newValue);
        });

        filterComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filterHoaDonList(newVal);
        });

    }

    private void loadPhieuDangKyChoTT() throws Exception {
        dsPhieu.setAll(phieuDangKyBUS.layDSPhieuDangKyChoThanhToan());
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

        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

        colGiamGia.setCellValueFactory(data -> {
            String formatted = currencyFormat.format(data.getValue().getSoTienGiam()) + "đ";
            return new SimpleStringProperty(formatted);
        });

        colTongTien.setCellValueFactory(data -> {
            String formatted = currencyFormat.format(data.getValue().getTongTien()) + "đ";
            return new SimpleStringProperty(formatted);
        });
    }

    private void addActionButtonsToTableDSPhieu() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnLapHoaDon = new Button();
//            private final Button btnXemCTHDChiTiet = new Button();
            private final HBox actionBox = new HBox(2);

            {
                btnLapHoaDon.setGraphic(new FontIcon("fas-print"));
                btnLapHoaDon.getStyleClass().add("action-button");

                Tooltip tooltip = new Tooltip("Click để lập hóa đơn");
                Tooltip.install(btnLapHoaDon, tooltip);

                tooltip.setShowDelay(Duration.millis(10));
                tooltip.setHideDelay(Duration.millis(300));

                btnLapHoaDon.setOnAction(event -> {
                    PhieuDangKyDTO phieu = getTableView().getItems().get(getIndex());
                    String loaiKH = phieu.getKhachHang().getLoai_kh();
                    if ("Đơn vị".equalsIgnoreCase(loaiKH)) {
                        btnOpenLapHoaDonDVPopup(phieu);
                    } else if ("Cá nhân".equalsIgnoreCase(loaiKH)) {
                        btnOpenLapHoaDonCNPopup(phieu);
                    }
                });

//                btnXemCTHDChiTiet.setGraphic(new FontIcon("fas-eye"));
//                btnXemCTHDChiTiet.getStyleClass().add("action-button");

                actionBox.getChildren().addAll(btnLapHoaDon);
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
            private final Button btnXemCTHD = new Button();
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
                    btnOpenDuyetThanhToan(hoaDon);
                });

                btnXemCTHD.setGraphic(new FontIcon("fas-eye"));
                btnXemCTHD.getStyleClass().add("action-button");

                btnXemCTHD.setOnAction(event -> {
                    HoaDonDTO hoaDon = getTableView().getItems().get(getIndex());
                    btnXemCTHD(hoaDon);
                });

                btnXoa.setGraphic(new FontIcon("fas-trash"));
                btnXoa.getStyleClass().add("action-button");
                btnXoa.setOnAction(event -> {
                    HoaDonDTO hoaDon = getTableView().getItems().get(getIndex());

                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Xác nhận xóa");
                    confirmAlert.setHeaderText(null);
                    confirmAlert.setContentText("Bạn có chắc chắn muốn xóa hóa đơn này?");

                    Optional<ButtonType> result = confirmAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (thanhToanBUS.xoaHoaDon(hoaDon.getMaHd())) {
                            dsHoaDon.remove(hoaDon);

                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Xóa thành công");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("Hóa đơn đã được xóa thành công.");
                            successAlert.showAndWait();
                        } else {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Xóa thất bại");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Không thể xóa hóa đơn. Vui lòng thử lại.");
                            errorAlert.showAndWait();
                        }
                    }
                });

                actionBox.getChildren().addAll(btnDuyet, btnXemCTHD, btnXoa);
//                actionBox.getChildren().addAll(btnDuyet, btnXemCTHD);
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

                    actionBox.getChildren().addAll(btnXemCTHD, btnXoa);
                    setGraphic(actionBox);
                }

            }
        });
    }

    private void btnOpenLapHoaDonDVPopup(PhieuDangKyDTO phieu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/ThanhToan/lap-hoa-don-donvi.fxml"));
            Parent root = loader.load();
            LapHoaDonDVController controller = loader.getController();

            if (controller != null) {
                controller.setPhieuDangKy(phieu);
            } else {
                System.out.println("Controller is null, cannot set PhieuDangKy.");
            }

            String maPhieu = phieu.getMaPhieuDangKy();
            String tenKH = phieu.getKhachHang().getHoTen();
            String title = "Lập hóa đơn cho PDK mã: " + maPhieu + " - Tên KH: " + tenKH;
            showPopup(root, title);

            loadPhieuDangKyChoTT();
            loadHoaDon();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnOpenLapHoaDonCNPopup(PhieuDangKyDTO phieu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/ThanhToan/lap-hoa-don-canhan.fxml"));
            Parent root = loader.load();
            LapHoaDonCNController controller = loader.getController();
            controller.setPhieuDangKy(phieu);
            String maPhieu = phieu.getMaPhieuDangKy();
            String tenKH = phieu.getKhachHang().getHoTen();
            String title = "Lập hóa đơn cho PDK mã: " + maPhieu + " - Tên KH: " + tenKH;

            showPopup(root, title);

            loadPhieuDangKyChoTT();
            loadHoaDon();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnOpenDuyetThanhToan(HoaDonDTO hoaDon) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/ThanhToan/duyet-thanh-toan.fxml"));
            Parent root = loader.load();
            DuyetThanhToanController controller = loader.getController();
            controller.setHoaDon(hoaDon);

            showPopup(root, "Duyệt Thanh Toán");

            loadPhieuDangKyChoTT();
            loadHoaDon();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void btnXemCTHD(HoaDonDTO hoaDon) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/ThanhToan/chi-tiet-hoa-don.fxml"));
            Parent root = loader.load();
            ChiTietHoaDonController controller = loader.getController();
            controller.setHoaDon(hoaDon);
            controller.setKhachHang(hoaDon.getPhieuDangKy().getKhachHang());

            showPopup(root, "Xem chi tiết hóa đơn");
            loadHoaDon();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterPhieuDangKy(String keyword) throws Exception {
        if (keyword == null || keyword.isEmpty()) {
            loadPhieuDangKyChoTT();
            return;
        }

        ObservableList<PhieuDangKyDTO> filteredList = FXCollections.observableArrayList();
        String lowerKeyword = keyword.toLowerCase();

        for (PhieuDangKyDTO pdk : dsPhieu) {
            if (pdk.getMaPhieuDangKy().toLowerCase().contains(lowerKeyword)
                    || pdk.getKhachHang().getHoTen().toLowerCase().contains(lowerKeyword)
                    || pdk.getKhachHang().getLoai_kh().toLowerCase().contains(lowerKeyword)
                    || pdk.getNgayLap().toString().contains(lowerKeyword)
                    || pdk.getTrangThai().toLowerCase().contains(lowerKeyword)) {
                filteredList.add(pdk);
            }
        }

        DSPhieuDK.setItems(filteredList);
    }

    private void filterHoaDon(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            loadHoaDon();
            return;
        }

        ObservableList<HoaDonDTO> filteredList = FXCollections.observableArrayList();
        String lowerKeyword = keyword.toLowerCase();

        for (HoaDonDTO hd : dsHoaDon) {
            if (hd.getMaHd().toLowerCase().contains(lowerKeyword)
                    || hd.getPhieuDangKy().getMaPhieuDangKy().toLowerCase().contains(lowerKeyword)
                    || hd.getPhieuDangKy().getKhachHang().getHoTen().toLowerCase().contains(lowerKeyword)
                    || hd.getPtThanhToan().toLowerCase().contains(lowerKeyword)
                    || hd.getPhieuDangKy().getKhachHang().getLoai_kh().toLowerCase().contains(lowerKeyword)
                    || String.valueOf(hd.getTongTien()).contains(lowerKeyword)) {
                filteredList.add(hd);
            }
        }

        LichSuThanhToan.setItems(filteredList);
    }

    private void filterHoaDonList(String filter) {
        if (filter == null || filter.equals("Tất cả")) {
            loadHoaDon();
            return;
        }

        ObservableList<HoaDonDTO> filteredList = FXCollections.observableArrayList();

        for (HoaDonDTO hoaDon : dsHoaDon) {
            boolean isDonVi = "Đơn vị".equalsIgnoreCase(hoaDon.getPhieuDangKy().getKhachHang().getLoai_kh());
            boolean isChuyenKhoan = "Chuyển khoản".equalsIgnoreCase(hoaDon.getPtThanhToan());
            boolean isChuaDuyet = hoaDon.getMaTt() == null;

            if (filter.equals("Chờ duyệt") && isDonVi && isChuyenKhoan && isChuaDuyet) {
                filteredList.add(hoaDon);
            }
        }

        LichSuThanhToan.setItems(filteredList);
    }


    private void showPopup(Parent root, String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}
