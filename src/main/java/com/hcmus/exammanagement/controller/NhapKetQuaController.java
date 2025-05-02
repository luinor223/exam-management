package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.KetQuaBUS;
import com.hcmus.exammanagement.bus.PhieuDuThiBUS;
import com.hcmus.exammanagement.dto.PhieuDuThiDTO;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Slf4j
public class NhapKetQuaController implements Initializable {

    @FXML private TableView<PhieuDuThiDTO> DSPhieuDuThi;
    @FXML private TableColumn<PhieuDuThiDTO, String> colMaLT;
    @FXML private TableColumn<PhieuDuThiDTO, String> colSBD;
    @FXML private TableColumn<PhieuDuThiDTO, LocalDate> colNgayThi;
    @FXML private TableColumn<PhieuDuThiDTO, LocalDate> colNgayCap;
    @FXML private TableColumn<PhieuDuThiDTO, String> colTrangThai;
    @FXML private TableColumn<PhieuDuThiDTO, Void> colAction;

    @FXML private DatePicker filterNgayBD;
    @FXML private DatePicker filterNgayKT;
    @FXML private ComboBox<String> filterTinhTrang;
    @FXML private TextField searchField;

    // Data collections
    private ObservableList<PhieuDuThiDTO> phieuDuThiList;
    private FilteredList<PhieuDuThiDTO> filteredPhieuDuThiList;

    // Business Logic objects
    private final PhieuDuThiBUS phieuDuThiBUS = new PhieuDuThiBUS();
    private final KetQuaBUS ketQuaBUS = new KetQuaBUS();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        setupFilters();
        loadData();
        setupFilterListeners();
    }

    private void setupTable() {
        // Set up column cell value factories
        colMaLT.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaLT()));
        colSBD.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSbd()));
        colNgayThi.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNgayThi()));
        colNgayCap.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNgayCap()));

        // Format date columns
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

        colNgayCap.setCellFactory(column -> new TableCell<>() {
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

        colTrangThai.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTrangThai()));

        // Set up action column with buttons
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnNhap = new Button();
            private final HBox actionBox = new HBox(5);

            {
                btnNhap.setGraphic(new FontIcon("fas-edit"));
                btnNhap.getStyleClass().add("action-button");
                btnNhap.setOnAction(event -> {
                    PhieuDuThiDTO phieuDuThi = getTableView().getItems().get(getIndex());
                    openKetQuaForm(phieuDuThi);
                });

                actionBox.getChildren().addAll(btnNhap);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionBox);
            }
        });
    }

    private void setupFilters() {
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

        filterNgayBD.setConverter(dateConverter);
        filterNgayKT.setConverter(dateConverter);

        // Set default selection for status filter
        filterTinhTrang.getSelectionModel().selectFirst();
    }

    private void loadData() {
        try {
            // Load all exam tickets
            phieuDuThiList = FXCollections.observableArrayList(phieuDuThiBUS.layDSPhieuDuThi());

            // Create a filtered list wrapper
            filteredPhieuDuThiList = new FilteredList<>(phieuDuThiList, p -> true);

            // Set the filtered list as the table's items
            DSPhieuDuThi.setItems(filteredPhieuDuThiList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", null, "Không thể tải dữ liệu: " + e.getMessage());
            log.error(e.getMessage());
        }
    }

    private void setupFilterListeners() {
        // Search by SBD
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        // Filter by status
        filterTinhTrang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        // Filter by date range
        filterNgayBD.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        filterNgayKT.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    private void applyFilters() {
        filteredPhieuDuThiList.setPredicate(createFilterPredicate());
    }

    private Predicate<PhieuDuThiDTO> createFilterPredicate() {
        return phieuDuThi -> {
            boolean matchesSearch = true;
            boolean matchesStatus = true;
            boolean matchesDateRange = true;

            // Filter by SBD
            String searchText = searchField.getText();
            if (searchText != null && !searchText.isEmpty()) {
                searchText = searchText.toLowerCase();
                matchesSearch = phieuDuThi.getSbd().toLowerCase().contains(searchText) ||
                        phieuDuThi.getMaLT().toLowerCase().contains(searchText);
            }

            // Filter by status
            String selectedStatus = filterTinhTrang.getSelectionModel().getSelectedItem();
            if (!"Tất cả".equals(selectedStatus)) {
                matchesStatus = selectedStatus.equals(phieuDuThi.getTrangThai());
            }

            // Filter by date range
            LocalDate startDate = filterNgayBD.getValue();
            LocalDate endDate = filterNgayKT.getValue();

            if (startDate != null && endDate != null) {
                if (phieuDuThi.getNgayThi() != null) {
                    matchesDateRange = !phieuDuThi.getNgayThi().isBefore(startDate) &&
                            !phieuDuThi.getNgayThi().isAfter(endDate);
                } else {
                    matchesDateRange = false;
                }
            } else if (startDate != null) {
                if (phieuDuThi.getNgayThi() != null) {
                    matchesDateRange = !phieuDuThi.getNgayThi().isBefore(startDate);
                } else {
                    matchesDateRange = false;
                }
            } else if (endDate != null) {
                if (phieuDuThi.getNgayThi() != null) {
                    matchesDateRange = !phieuDuThi.getNgayThi().isAfter(endDate);
                } else {
                    matchesDateRange = false;
                }
            }

            return matchesSearch && matchesStatus && matchesDateRange;
        };
    }

    private void openKetQuaForm(PhieuDuThiDTO phieuDuThi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/CapChungChi/nhap-ket-qua-form.fxml"));

            NhapKetQuaFormController formController = new NhapKetQuaFormController(phieuDuThi, ketQuaBUS);
            loader.setController(formController);

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nhập kết quả thi");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            stage.setOnHidden(event -> loadData());

            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", null, "Không thể mở form nhập kết quả: " + e.getMessage());
            log.error(e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}