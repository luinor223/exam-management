package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.KetQuaBUS;
import com.hcmus.exammanagement.dto.KetQuaDayDuDTO;

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
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Slf4j
public class CapChungChiController implements Initializable {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterStatus;
    @FXML private DatePicker filterDateStart;
    @FXML private DatePicker filterDateEnd;
    @FXML private Button btnClear;

    @FXML private TableView<KetQuaDayDuDTO> tableKetQua;
    @FXML private TableColumn<KetQuaDayDuDTO, String> colMaLT;
    @FXML private TableColumn<KetQuaDayDuDTO, String> colSBD;
    @FXML private TableColumn<KetQuaDayDuDTO, String> colHoTen;
    @FXML private TableColumn<KetQuaDayDuDTO, String> colCCCD;
    @FXML private TableColumn<KetQuaDayDuDTO, String> colTenChungChi;
    @FXML private TableColumn<KetQuaDayDuDTO, Integer> colDiem;
    @FXML private TableColumn<KetQuaDayDuDTO, String> colXepLoai;
    @FXML private TableColumn<KetQuaDayDuDTO, LocalDate> colNgayThi;
    @FXML private TableColumn<KetQuaDayDuDTO, String> colTrangThai;
    @FXML private TableColumn<KetQuaDayDuDTO, Void> colAction;

    private ObservableList<KetQuaDayDuDTO> ketQuaList;
    private FilteredList<KetQuaDayDuDTO> filteredKetQuaList;

    // Business logic
    private final KetQuaBUS ketQuaBUS = new KetQuaBUS();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        setupFilters();
        loadData();
        setupFilterListeners();
    }

    private void setupTable() {
        colMaLT.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaLT()));
        colSBD.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSbd()));
        colHoTen.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoTenThiSinh()));
        colCCCD.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCccd()));
        colTenChungChi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTenChungChi()));
        colDiem.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getDiem()).asObject());
        colXepLoai.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getXepLoai()));

        // Get ngayThi from the ngayGioThi
        colNgayThi.setCellValueFactory(data -> {
            if (data.getValue().getNgayGioThi() != null) {
                return new SimpleObjectProperty<>(data.getValue().getNgayGioThi().toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });

        // Format date column
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colNgayThi.setCellFactory(column -> new TableCell<>() {
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

        // Status column
        colTrangThai.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTrangThai()));

        // Action column with view details and issue certificate buttons
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnXem = new Button();
            private final Button btnCapCC = new Button();
            private final HBox actionBox = new HBox(5);

            {
                btnXem.setGraphic(new FontIcon("fas-eye"));
                btnXem.getStyleClass().add("action-button");
                btnXem.setOnAction(event -> {
                    KetQuaDayDuDTO ketQua = getTableView().getItems().get(getIndex());
                    xemChiTietKetQua(ketQua);
                });

                btnCapCC.setGraphic(new FontIcon("fas-certificate"));
                btnCapCC.getStyleClass().add("action-button");
                btnCapCC.setOnAction(event -> {
                    KetQuaDayDuDTO ketQua = getTableView().getItems().get(getIndex());
                    capChungChi(ketQua);
                });

                actionBox.getChildren().addAll(btnXem, btnCapCC);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                KetQuaDayDuDTO ketQua = getTableView().getItems().get(getIndex());

                // Disable issue button if certificate already issued
                boolean daCap = "Đã cấp".equals(ketQua.getTrangThai());
                btnCapCC.setDisable(daCap);

                setGraphic(actionBox);
            }
        });
    }

    private void setupFilters() {
        // Set up status filter
        filterStatus.getItems().addAll("Tất cả", "Đã cấp", "Chưa cấp");
        filterStatus.getSelectionModel().selectFirst();

        // Set up date pickers
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

        filterDateStart.setConverter(dateConverter);
        filterDateEnd.setConverter(dateConverter);

        // Set up clear filter button
        btnClear.setOnAction(e -> {
            searchField.clear();
            filterStatus.getSelectionModel().selectFirst();
            filterDateStart.setValue(null);
            filterDateEnd.setValue(null);
            applyFilters();
        });
    }

    private void loadData() {
        try {
            // Load all results with joined data using KetQuaDayDuDTO
            ketQuaList = FXCollections.observableArrayList(ketQuaBUS.getAllKetQuaWithDetails());
            filteredKetQuaList = new FilteredList<>(ketQuaList, p -> true);
            tableKetQua.setItems(filteredKetQuaList);
        } catch (Exception e) {
            log.error("Error loading data", e);
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu",
                    "Đã xảy ra lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

    private void setupFilterListeners() {
        // Search field listener
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        // Status filter listener
        filterStatus.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        // Date range filter listeners
        filterDateStart.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        filterDateEnd.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void applyFilters() {
        filteredKetQuaList.setPredicate(createFilterPredicate());
    }

    private Predicate<KetQuaDayDuDTO> createFilterPredicate() {
        return ketQua -> {
            boolean matchesSearch = true;
            boolean matchesStatus = true;
            boolean matchesDateRange = true;

            // Filter by search text (SBD, CCCD, or Họ tên)
            String searchText = searchField.getText();
            if (searchText != null && !searchText.isEmpty()) {
                searchText = searchText.toLowerCase();

                // Now directly use properties from KetQuaDayDuDTO
                boolean matchesSBD = ketQua.getSbd().toLowerCase().contains(searchText);
                boolean matchesCCCD = ketQua.getCccd().toLowerCase().contains(searchText);
                boolean matchesHoTen = ketQua.getHoTenThiSinh().toLowerCase().contains(searchText);

                matchesSearch = matchesSBD || matchesCCCD || matchesHoTen;
            }

            // Filter by status
            String selectedStatus = filterStatus.getSelectionModel().getSelectedItem();
            if (!"Tất cả".equals(selectedStatus)) {
                matchesStatus = selectedStatus.equals(ketQua.getTrangThai());
            }

            // Filter by date range
            LocalDate startDate = filterDateStart.getValue();
            LocalDate endDate = filterDateEnd.getValue();

            if (startDate != null || endDate != null) {
                if (ketQua.getNgayGioThi() != null) {
                    LocalDate examDate = ketQua.getNgayGioThi().toLocalDate();

                    if (startDate != null && endDate != null) {
                        matchesDateRange = !examDate.isBefore(startDate) && !examDate.isAfter(endDate);
                    } else if (startDate != null) {
                        matchesDateRange = !examDate.isBefore(startDate);
                    } else {
                        matchesDateRange = !examDate.isAfter(endDate);
                    }
                } else {
                    matchesDateRange = false;
                }
            }

            return matchesSearch && matchesStatus && matchesDateRange;
        };
    }

    private void xemChiTietKetQua(KetQuaDayDuDTO ketQua) {
        // Create dialog for detailed view
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Chi tiết kết quả thi");
        dialog.setHeaderText("Thông tin chi tiết kết quả");

        // Create a VBox to hold information
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20px;");

        // Add result information
        content.getChildren().addAll(
                new Label("Thông tin kết quả:"),
                new Separator(),
                new Label("Mã lịch thi: " + ketQua.getMaLT()),
                new Label("Số báo danh: " + ketQua.getSbd()),
                new Label("Điểm: " + ketQua.getDiem()),
                new Label("Xếp loại: " + ketQua.getXepLoai()),
                new Label("Nhận xét: " + (ketQua.getNhanXet() != null ? ketQua.getNhanXet() : "")),
                new Label("Ngày cấp chứng chỉ: " + formatDate(ketQua.getNgayCapChungChi())),
                new Label("Ngày hết hạn: " + formatDate(ketQua.getNgayHetHan())),
                new Label("Trạng thái: " + ketQua.getTrangThai())
        );

        // Add thí sinh information (directly from KetQuaDayDuDTO)
        content.getChildren().addAll(
                new Label("\nThông tin thí sinh:"),
                new Separator(),
                new Label("Mã thí sinh: " + ketQua.getMaThiSinh()),
                new Label("Họ tên: " + ketQua.getHoTenThiSinh()),
                new Label("CCCD: " + ketQua.getCccd())
        );

        // Add chứng chỉ information (directly from KetQuaDayDuDTO)
        content.getChildren().addAll(
                new Label("\nThông tin chứng chỉ:"),
                new Separator(),
                new Label("Mã chứng chỉ: " + ketQua.getMaChungChi()),
                new Label("Tên chứng chỉ: " + ketQua.getTenChungChi()),
                new Label("Thời gian hiệu lực: " + ketQua.getThoiGianHieuLuc() + " ngày")
        );

        // Add exam date information
        if (ketQua.getNgayGioThi() != null) {
            content.getChildren().addAll(
                    new Label("\nThông tin lịch thi:"),
                    new Separator(),
                    new Label("Ngày giờ thi: " + ketQua.getNgayGioThi().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            );
        }

        // Configure dialog
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);

        // Show dialog
        dialog.showAndWait();
    }

    private void capChungChi(KetQuaDayDuDTO ketQua) {
        try {
            // Update status to "Đã cấp" without confirmation
            boolean success = ketQuaBUS.capChungChi(ketQua.getMaLT(), ketQua.getSbd());

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cấp chứng chỉ thành công",
                        "Đã cập nhật trạng thái thành 'Đã cấp'.");

                // Update in-memory data and refresh table
                ketQua.setTrangThai("Đã cấp");
                tableKetQua.refresh();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cấp chứng chỉ",
                        "Đã xảy ra lỗi khi cập nhật trạng thái.");
            }
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cấp chứng chỉ",
                    "Đã xảy ra lỗi: " + e.getMessage());
            log.error("Error issuing certificate", e);
        }
    }

    private String formatDate(LocalDate date) {
        if (date == null) return "N/A";
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}