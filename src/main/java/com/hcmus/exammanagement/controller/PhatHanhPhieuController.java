package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.LichThiBUS;
import com.hcmus.exammanagement.bus.PhieuDuThiBUS;
import com.hcmus.exammanagement.dao.PhieuDuThiDAO;
import com.hcmus.exammanagement.dto.LichThiDTO;
import com.hcmus.exammanagement.dto.PhieuDuThiDTO;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Slf4j
public class PhatHanhPhieuController implements Initializable {

    // Tab 1 - Lịch thi components
    @FXML private TableView<LichThiDTO> DSLichThi;
    @FXML private TableColumn<LichThiDTO, String> colMaLT;
    @FXML private TableColumn<LichThiDTO, String> colNgayGioThi;
    @FXML private TableColumn<LichThiDTO, Integer> colThoiLuong;
    @FXML private TableColumn<LichThiDTO, String> colTinhTrang;
    @FXML private TableColumn<LichThiDTO, Void> colAction;

    @FXML private TextField searchFieldLichThi;
    @FXML private ComboBox<String> filterTrangThai;
    @FXML private DatePicker filterNgayBD;
    @FXML private DatePicker filterNgayKT;

    // Tab 2 - Phiếu dự thi components
    @FXML private TableView<PhieuDuThiDTO> DSPhieuDuThi;
    @FXML private TableColumn<PhieuDuThiDTO, String> colMaLTPDT;
    @FXML private TableColumn<PhieuDuThiDTO, String> colSBD;
    @FXML private TableColumn<PhieuDuThiDTO, String> colMaPhong;
    @FXML private TableColumn<PhieuDuThiDTO, String> colMaCTPDK;
    @FXML private TableColumn<PhieuDuThiDTO, LocalDate> colNgayCapPDT;
    @FXML private TableColumn<PhieuDuThiDTO, LocalDate> colNgayThiPDT;
    @FXML private TableColumn<PhieuDuThiDTO, Void> colActionPDT;

    @FXML private TextField searchFieldPhieuDuThi;
    @FXML private DatePicker filterNgayBdPDT;
    @FXML private DatePicker filterNgayKtPDT;

    // Data collections
    private ObservableList<LichThiDTO> lichThiList;
    private FilteredList<LichThiDTO> filteredLichThiList;

    private ObservableList<PhieuDuThiDTO> phieuDuThiList;
    private FilteredList<PhieuDuThiDTO> filteredDSPhieuDuThi;

    // Business logic
    private final PhieuDuThiBUS phieuDuThiBUS = new PhieuDuThiBUS();
    private final PhieuDuThiDAO phieuDuThiDAO = new PhieuDuThiDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupLichThiTable();
        setupPhieuDuThiTable();
        setupFilters();
        loadData();
        setupFilterListeners();
    }

    private void setupLichThiTable() {
        // Configure columns for the exam schedule table
        colMaLT.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaLichThi()));

        colNgayGioThi.setCellValueFactory(data -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dateTime = data.getValue().getNgayGioThi().toLocalDateTime();
            return new SimpleStringProperty(dateTime.format(formatter));
        });

        colThoiLuong.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getThoiLuongThi()).asObject());

        colTinhTrang.setCellValueFactory(data -> {
            boolean daCoPhieu = checkDaCoPhieuDuThi(data.getValue().getMaLichThi());
            return new SimpleStringProperty(daCoPhieu ? "Đã phát phiếu" : "Chưa phát phiếu");
        });

        // Action column with issue ticket button
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnPhatPhieu = new Button();
            private final Button btnXemDS = new Button();
            private final HBox actionBox = new HBox(5);

            {
                btnPhatPhieu.setGraphic(new FontIcon("fas-ticket-alt"));
                btnPhatPhieu.getStyleClass().add("action-button");
                btnPhatPhieu.setOnAction(event -> {
                    LichThiDTO lichThi = getTableView().getItems().get(getIndex());
                    btnPhatPhieu(lichThi);
                });

                btnXemDS.setGraphic(new FontIcon("fas-list"));
                btnXemDS.getStyleClass().add("action-button");
                btnXemDS.setOnAction(event -> {
                    LichThiDTO lichThi = getTableView().getItems().get(getIndex());
                    btnXemDS(lichThi);
                });

                actionBox.getChildren().addAll(btnPhatPhieu, btnXemDS);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                LichThiDTO lichThi = getTableView().getItems().get(getIndex());
                boolean daCoPhieu = checkDaCoPhieuDuThi(lichThi.getMaLichThi());

                // Disable issue button if tickets already issued
                btnPhatPhieu.setDisable(daCoPhieu);

                setGraphic(actionBox);
            }
        });
    }

    private void setupPhieuDuThiTable() {
        // Configure columns for the exam tickets table
        colMaLTPDT.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaLT()));
        colSBD.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSbd()));
        colMaPhong.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaPhong()));
        colMaCTPDK.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaCtpdk()));

        // Date formatters for date columns
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colNgayCapPDT.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getNgayCap()));

        colNgayCapPDT.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        colNgayThiPDT.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getNgayThi()));

        colNgayThiPDT.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        // Action column
        colActionPDT.setCellFactory(param -> new TableCell<>() {
            private final Button btnXem = new Button();

            {
                btnXem.setGraphic(new FontIcon("fas-eye"));
                btnXem.getStyleClass().add("action-button");
                btnXem.setOnAction(event -> {
                    PhieuDuThiDTO phieuDuThi = getTableView().getItems().get(getIndex());
                    btnXemChiTiet(phieuDuThi);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnXem);
            }
        });
    }

    private void setupFilters() {
        // Date pickers
        StringConverter<LocalDate> dateConverter = new StringConverter<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, formatter);
                } else {
                    return null;
                }
            }
        };

        filterNgayBD.setConverter(dateConverter);
        filterNgayKT.setConverter(dateConverter);
        filterNgayBdPDT.setConverter(dateConverter);
        filterNgayKtPDT.setConverter(dateConverter);

        // Set default selection for status filter
        filterTrangThai.getSelectionModel().selectFirst();
    }

    private void loadData() {
        try {
            // Load exam schedules
            List<LichThiDTO> lichThis = LichThiBUS.layDSLichThi();
            lichThiList = FXCollections.observableArrayList(lichThis);
            filteredLichThiList = new FilteredList<>(lichThiList, p -> true);
            DSLichThi.setItems(filteredLichThiList);

            // Load exam tickets
            List<PhieuDuThiDTO> dsPhieuDuThi = phieuDuThiBUS.layDSPhieuDuThi();
            phieuDuThiList = FXCollections.observableArrayList(dsPhieuDuThi);
            filteredDSPhieuDuThi = new FilteredList<>(phieuDuThiList, p -> true);
            DSPhieuDuThi.setItems(filteredDSPhieuDuThi);

        } catch (Exception e) {
            thongBao(Alert.AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu",
                    "Đã xảy ra lỗi khi tải dữ liệu: " + e.getMessage());
            log.error(e.getMessage());
        }
    }

    private void setupFilterListeners() {
        // Listeners for lịch thi tab
        searchFieldLichThi.textProperty().addListener((obs, oldVal, newVal) ->
                applyLichThiFilters());

        filterTrangThai.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
                applyLichThiFilters());

        filterNgayBD.valueProperty().addListener((obs, oldVal, newVal) ->
                applyLichThiFilters());

        filterNgayKT.valueProperty().addListener((obs, oldVal, newVal) ->
                applyLichThiFilters());

        // Listeners for phiếu dự thi tab
        searchFieldPhieuDuThi.textProperty().addListener((obs, oldVal, newVal) ->
                applyPhieuDuThiFilters());

        filterNgayBdPDT.valueProperty().addListener((obs, oldVal, newVal) ->
                applyPhieuDuThiFilters());

        filterNgayKtPDT.valueProperty().addListener((obs, oldVal, newVal) ->
                applyPhieuDuThiFilters());
    }

    private void applyLichThiFilters() {
        filteredLichThiList.setPredicate(createLichThiFilterPredicate());
    }

    private void applyPhieuDuThiFilters() {
        filteredDSPhieuDuThi.setPredicate(createPhieuDuThiFilterPredicate());
    }

    private Predicate<LichThiDTO> createLichThiFilterPredicate() {
        return lichThi -> {
            boolean matchesSearch = true;
            boolean matchesStatus = true;
            boolean matchesDateRange = true;

            // Filter by exam code
            String searchText = searchFieldLichThi.getText();
            if (searchText != null && !searchText.isEmpty()) {
                searchText = searchText.toLowerCase();
                matchesSearch = lichThi.getMaLichThi().toLowerCase().contains(searchText);
            }

            // Filter by status
            String selectedStatus = filterTrangThai.getSelectionModel().getSelectedItem();
            if (!"Tất cả".equals(selectedStatus)) {
                boolean daCoPhieu = checkDaCoPhieuDuThi(lichThi.getMaLichThi());
                if ("Đã phát phiếu".equals(selectedStatus)) {
                    matchesStatus = daCoPhieu;
                } else if ("Chưa phát phiếu".equals(selectedStatus)) {
                    matchesStatus = !daCoPhieu;
                }
            }

            // Filter by date range
            LocalDate startDate = filterNgayBD.getValue();
            LocalDate endDate = filterNgayKT.getValue();

            if (startDate != null || endDate != null) {
                LocalDate examDate = lichThi.getNgayGioThi().toLocalDateTime().toLocalDate();

                if (startDate != null && endDate != null) {
                    matchesDateRange = !examDate.isBefore(startDate) && !examDate.isAfter(endDate);
                } else if (startDate != null) {
                    matchesDateRange = !examDate.isBefore(startDate);
                } else if (endDate != null) {
                    matchesDateRange = !examDate.isAfter(endDate);
                }
            }

            return matchesSearch && matchesStatus && matchesDateRange;
        };
    }

    private Predicate<PhieuDuThiDTO> createPhieuDuThiFilterPredicate() {
        return phieuDuThi -> {
            boolean matchesSearch = true;
            boolean matchesDateRange = true;

            // Filter by SBD
            String searchText = searchFieldPhieuDuThi.getText();
            if (searchText != null && !searchText.isEmpty()) {
                searchText = searchText.toLowerCase();
                matchesSearch = phieuDuThi.getSbd().toLowerCase().contains(searchText) ||
                        phieuDuThi.getMaLT().toLowerCase().contains(searchText);
            }

            // Filter by date range
            LocalDate startDate = filterNgayBdPDT.getValue();
            LocalDate endDate = filterNgayKtPDT.getValue();

            if (startDate != null || endDate != null) {
                LocalDate examDate = phieuDuThi.getNgayThi();

                if (examDate != null) {
                    if (startDate != null && endDate != null) {
                        matchesDateRange = !examDate.isBefore(startDate) && !examDate.isAfter(endDate);
                    } else if (startDate != null) {
                        matchesDateRange = !examDate.isBefore(startDate);
                    } else if (endDate != null) {
                        matchesDateRange = !examDate.isAfter(endDate);
                    }
                } else {
                    matchesDateRange = false;
                }
            }

            return matchesSearch && matchesDateRange;
        };
    }

    // Method to check if an exam schedule already has issued tickets
    private boolean checkDaCoPhieuDuThi(String maLichThi) {
        try {
            return phieuDuThiDAO.countByLichThi(maLichThi) > 0;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    // Method to issue exam tickets
    private void btnPhatPhieu(LichThiDTO lichThi) {
        try {
            int count = phieuDuThiBUS.phatPhieuDuThi(lichThi.getMaLichThi());

            if (count > 0) {
                thongBao(Alert.AlertType.INFORMATION, "Thành công", "Phát hành phiếu dự thi thành công",
                        "Đã phát hành " + count + " phiếu dự thi cho lịch thi " + lichThi.getMaLichThi());

                // Refresh data
                loadData();
            } else {
                thongBao(Alert.AlertType.WARNING, "Cảnh báo", "Không có phiếu để phát hành",
                        "Không có phiếu đăng ký nào cho lịch thi này hoặc tất cả đã được phát hành.");
            }
        } catch (SQLException e) {
            thongBao(Alert.AlertType.ERROR, "Lỗi", "Không thể phát hành phiếu dự thi",
                    "Đã xảy ra lỗi: " + e.getMessage());
            log.error(e.getMessage());
        }
    }

    // Method to view list of issued tickets for a schedule
    private void btnXemDS(LichThiDTO lichThi) {
        // Switch to tab 2
        TabPane tabPane = (TabPane) DSLichThi.getScene().lookup(".tab-pane");
        tabPane.getSelectionModel().select(1);

        // Filter tickets to show only those for this schedule
        searchFieldPhieuDuThi.setText(lichThi.getMaLichThi());
        applyPhieuDuThiFilters();
    }

    // Method to view ticket details
    private void btnXemChiTiet(PhieuDuThiDTO phieuDuThi) {
        String details = "Chi tiết phiếu dự thi:\n\n" +
                "Mã lịch thi: " + phieuDuThi.getMaLT() + "\n" +
                "Số báo danh: " + phieuDuThi.getSbd() + "\n" +
                "Mã phòng: " + phieuDuThi.getMaPhong() + "\n" +
                "Mã chi tiết phiếu ĐK: " + phieuDuThi.getMaCtpdk() + "\n" +
                "Ngày cấp: " + formatDate(phieuDuThi.getNgayCap()) + "\n" +
                "Ngày thi: " + formatDate(phieuDuThi.getNgayThi()) + "\n" +
                "Trạng thái: " + phieuDuThi.getTrangThai();

        thongBao(Alert.AlertType.INFORMATION, "Chi tiết phiếu dự thi",
                "Thông tin phiếu dự thi " + phieuDuThi.getSbd(), details);
    }

    private String formatDate(LocalDate date) {
        if (date == null) return "N/A";
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private void thongBao(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}