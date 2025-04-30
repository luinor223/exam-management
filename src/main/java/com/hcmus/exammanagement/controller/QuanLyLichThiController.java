package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.ChiTietPhongThiBUS;
import com.hcmus.exammanagement.bus.LichThiBUS;
import com.hcmus.exammanagement.dto.ChiTietPhongThiDTO;
import com.hcmus.exammanagement.dto.LichThiDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
public class QuanLyLichThiController {

    // Data
    private final ObservableList<LichThiDTO> lichThiList = FXCollections.observableArrayList();
    private final ObservableList<ChiTietPhongThiDTO> chiTietPhongThiList = FXCollections.observableArrayList();

    // Selected items
    private LichThiDTO selectedLichThi = null;

    // UI components - Lịch thi table
    @FXML private TableView<LichThiDTO> lichThiTable;
    @FXML private TableColumn<LichThiDTO, String> maLichThiColumn;
    @FXML private TableColumn<LichThiDTO, String> ngayGioThiColumn;
    @FXML private TableColumn<LichThiDTO, String> thoiLuongThiColumn;
    @FXML private TableColumn<LichThiDTO, String> chungChiColumn;
    @FXML private TableColumn<LichThiDTO, String> soLuongHienTaiColumn;
    @FXML private TableColumn<LichThiDTO, String> soLuongToiDaColumn;
    @FXML private TableColumn<LichThiDTO, String> actionColumn;

    // UI components - Chi tiết phòng thi
    @FXML private AnchorPane chiTietPhongThiSection;
    @FXML private TableView<ChiTietPhongThiDTO> chiTietPhongThiTable;
    @FXML private TableColumn<ChiTietPhongThiDTO, String> maPhongColumn;
    @FXML private TableColumn<ChiTietPhongThiDTO, String> tenPhongColumn;
    @FXML private TableColumn<ChiTietPhongThiDTO, String> maGiamThiColumn;
    @FXML private TableColumn<ChiTietPhongThiDTO, String> tenGiamThiColumn;
    @FXML private TableColumn<ChiTietPhongThiDTO, String> soLuongHienTaiPhongColumn;
    @FXML private TableColumn<ChiTietPhongThiDTO, String> soLuongToiDaPhongColumn;
    @FXML private TableColumn<ChiTietPhongThiDTO, String> actionPhongColumn;

    @FXML
    public void initialize() {
        setupTables();
        loadData();
        setupEventHandlers();
    }

    private void setupTables() {
        // Setup Lich Thi Table
        maLichThiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaLichThi()));
        ngayGioThiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNgayGioThi().toString()));
        thoiLuongThiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getThoiLuongThi().toString()));
        chungChiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getChungChi().getTenChungChi()));
        soLuongHienTaiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSoLuongHienTai().toString()));
        soLuongToiDaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSoLuongToiDa().toString()));

        // Add Edit and Delete buttons to actionColumn
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Sửa");
            private final Button deleteButton = new Button("Xóa");
            private final HBox pane = new HBox(5, editButton, deleteButton);

            {
                editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    LichThiDTO lichThi = getTableView().getItems().get(getIndex());
                    handleShowSuaLichThi(lichThi);
                });

                deleteButton.setOnAction(event -> {
                    LichThiDTO lichThi = getTableView().getItems().get(getIndex());
                    try {
                        boolean success = LichThiBUS.xoaLichThi(lichThi.getMaLichThi());
                        if (success) {
                            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa lịch thi thành công", null);
                            loadData();
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa lịch thi", null);
                        }
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa lịch thi", e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        // Setup Chi Tiet Phong Thi Table
        maPhongColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhongDTO().getMaPhong()));
        maGiamThiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGiamThiDTO().getMaGiamThi()));
        soLuongHienTaiPhongColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSoLuongHienTai().toString()));
        soLuongToiDaPhongColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSoLuongToiDa().toString()));
        tenPhongColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhongDTO().getTenPhong()));
        tenGiamThiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGiamThiDTO().getHoTen()));

        // Add Delete button to actionPhongColumn
        actionPhongColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Xóa");

            {
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                deleteButton.setOnAction(event -> {
                    ChiTietPhongThiDTO chiTietPhongThi = getTableView().getItems().get(getIndex());
                    try {
                        boolean success = ChiTietPhongThiBUS.xoaChiTietPhongThi(
                            chiTietPhongThi.getMaLichThi(), chiTietPhongThi.getPhongDTO().getMaPhong());
                        if (success) {
                            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Hủy phân công phòng thành công", null);
                            loadData();
                            loadChiTietPhongThi(chiTietPhongThi.getMaLichThi());
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể hủy phân công phòng", null);
                        }
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể hủy phân công phòng", e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
    }

    private void loadData() {
        try {
            lichThiList.clear();
            lichThiList.addAll(LichThiBUS.layDSLichThi());
            lichThiTable.setItems(lichThiList);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu", e.getMessage());
        }
    }

    private void setupEventHandlers() {
        lichThiTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedLichThi = newSelection;
                loadChiTietPhongThi(selectedLichThi.getMaLichThi());
            }
        });
    }

    private void loadChiTietPhongThi(String maLichThi) {
        try {
            chiTietPhongThiList.clear();
            chiTietPhongThiList.addAll(ChiTietPhongThiBUS.layDSChiTietPhongThiTheoLichThi(maLichThi));
            chiTietPhongThiTable.setItems(chiTietPhongThiList);
            chiTietPhongThiSection.setVisible(selectedLichThi != null);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải chi tiết phòng thi", e.getMessage());
        }
    }

    @FXML
    public void handleShowThemLichThi() {
        moLichThiDialog(null);
    }

    @FXML
    public void handleShowSuaLichThi(LichThiDTO lichThi) {
        selectedLichThi = lichThi;
        moLichThiDialog(lichThi);
    }

    @FXML
    public void handleShowPhanCong() {
        if (selectedLichThi == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Chưa chọn lịch thi", "Vui lòng chọn lịch thi cần phân công");
            return;
        }

        moPhanCongDialog();
    }

    private void moLichThiDialog(LichThiDTO lichThi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/DangKy/lich-thi-dialog.fxml"));
            Parent root = loader.load();

            LichThiDialogController controller = loader.getController();
            controller.setLichThi(lichThi);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(lichThi == null ? "Thêm lịch thi" : "Chỉnh sửa lịch thi");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

            loadData();
            selectedLichThi = null;
        } catch (IOException e) {
            log.error("Error when loading LichThiDialog.fxml", e);
        }
    }

    private void moPhanCongDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/DangKy/phan-cong-dialog.fxml"));
            Parent root = loader.load();

            PhanCongDialogController controller = loader.getController();
            controller.setLichThi(selectedLichThi);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Phân công phòng thi");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

            loadData();
            loadChiTietPhongThi(selectedLichThi.getMaLichThi());
        } catch (IOException e) {
            log.error("Error when loading PhanCongDialog.fxml", e);
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
