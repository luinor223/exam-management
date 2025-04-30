package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.ChiTietPDKBUS;
import com.hcmus.exammanagement.bus.KhachHangBUS;
import com.hcmus.exammanagement.bus.LichThiBUS;
import com.hcmus.exammanagement.bus.PhieuDangKyBUS;
import com.hcmus.exammanagement.bus.ThiSinhBUS;
import com.hcmus.exammanagement.component.StepProgress;
import com.hcmus.exammanagement.dto.ChiTietPDKDTO;
import com.hcmus.exammanagement.dto.KhachHangDTO;
import com.hcmus.exammanagement.dto.LichThiDTO;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;
import com.hcmus.exammanagement.dto.ThiSinhDTO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class DangKyCaNhanController {

    // ===== BUS services =====
    private final KhachHangBUS    khachHangBUS    = new KhachHangBUS();
    private final ThiSinhBUS      thiSinhBUS      = new ThiSinhBUS();
    private final PhieuDangKyBUS  phieuDangKyBUS  = new PhieuDangKyBUS();
    private final LichThiBUS      lichThiBUS      = new LichThiBUS();
    private final ChiTietPDKBUS   chiTietPDKBUS   = new ChiTietPDKBUS();

    private final ObservableList<LichThiDTO> lichThiList = FXCollections.observableArrayList();
    private final ObservableList<LichThiDTO> selectedLichThiList = FXCollections.observableArrayList();

    // ===== Step progress bar =====
    @FXML private VBox progressContainer;
    private StepProgress stepProgress;
    @FXML private Button nextButton;
    @FXML private Button prevButton;

    // ===== Sections =====
    @FXML private AnchorPane infoSection;
    @FXML private AnchorPane examScheduleSection;
    @FXML private AnchorPane confirmationSection;
    private int currentStep = 1;
    private final int totalSteps = 3;

    // ===== Form fields for KhachHang =====
    @FXML private TextField hoTenField;
    @FXML private TextField CCCDField;
    @FXML private TextField emailField;
    @FXML private TextField sdtField;

    // ===== KhachHang table for quick selection =====
    @FXML private TableView<KhachHangDTO> khachHangTable;
    @FXML private TableColumn<KhachHangDTO, String> hoTenKHColumn;
    @FXML private TableColumn<KhachHangDTO, String> CCCDKHColumn;
    @FXML private TableColumn<KhachHangDTO, String> emailKHColumn;
    @FXML private TableColumn<KhachHangDTO, String> sdtKHColumn;

    // ===== Form fields for ThiSinh =====
    @FXML private TextField  hoTenThiSinhField;
    @FXML private TextField  CCCDThiSinhField;
    @FXML private DatePicker ngaySinhThiSinhField;
    @FXML private ComboBox<String> gioiTinhThiSinhCombo;

    // ===== KhachHang table for quick selection =====
    @FXML private TableView<ThiSinhDTO> thiSinhTable;
    @FXML private TableColumn<ThiSinhDTO, String> hoTenTSColumn;
    @FXML private TableColumn<ThiSinhDTO, String> CCCDTSColumn;
    @FXML private TableColumn<ThiSinhDTO, Date>   ngaySinhTSColumn;
    @FXML private TableColumn<ThiSinhDTO, String> gioiTinhTSColumn;

    // ===== Exam schedule fields =====
    @FXML private ComboBox<String> cchiCombo;
    @FXML private DatePicker ngayThiDatePicker;

    // ===== Available exam schedules table =====
    @FXML private TableView<LichThiDTO> lichThiTable;
    @FXML private TableColumn<LichThiDTO, String>  chungChiColumn;
    @FXML private TableColumn<LichThiDTO, Date>    ngayGioThiColumn;
    @FXML private TableColumn<LichThiDTO, Integer> soLuongTSColumn;
    @FXML private TableColumn<LichThiDTO, Double>    phiColumn;

    // ===== Selected exam schedules table =====
    @FXML private TableView<LichThiDTO> lichThiDaChonTable;
    @FXML private TableColumn<LichThiDTO, String> chungChiColumn1;
    @FXML private TableColumn<LichThiDTO, Date>   ngayGioThiColumn1;
    @FXML private TableColumn<LichThiDTO, Double>   phiColumn1;
    @FXML private TableColumn<LichThiDTO, Void>   actionColumn;

    // ===== Confirmation table =====
    @FXML private TableView<LichThiDTO> thiSinhDangKyTable;
    @FXML private TableColumn<LichThiDTO, Integer> sttColumn;
    @FXML private TableColumn<LichThiDTO, String>  cfChungChiColumn;
    @FXML private TableColumn<LichThiDTO, Date>    cfNgayGioThiColumn;
    @FXML private TableColumn<LichThiDTO, Double>    cfPhiColumn;

    // ===== Summary information =====
    @FXML private Label hoTenLabel;
    @FXML private Label CCCDLabel;
    @FXML private Label emailLabel;
    @FXML private Label sdtLabel;
    @FXML private Label hoTenThiSinhLabel;
    @FXML private Label CCCDThiSinhLabel;
    @FXML private Label ngaySinhThiSinhLabel;
    @FXML private Label gioiTinhThiSinhLabel;
    @FXML private Label tongTienLabel;

    @FXML
    public void initialize() {
        stepProgress = new StepProgress();
        List<String> steps = List.of("Thông tin cá nhân", "Lịch thi", "Xác nhận");
        stepProgress.configureSteps(steps);
        stepProgress.setCurrentStep(currentStep);

        progressContainer.getChildren().add(stepProgress);

        gioiTinhThiSinhCombo.getItems().addAll("Nam", "Nữ", "Khác");
        gioiTinhThiSinhCombo.setValue("Nam");

        setupKhachHangTable();
        setupThiSinhTable();
        setupLichThiTable();
        setupLichThiDaChonTable();
        setupThiSinhDangKyTable();
        loadKhachHangData();
        loadThiSinhData();
        loadLichThiData();
        setupChungChiComboBox();

        updateContentVisibility();
        updateButtonVisibility();
    }

    private void setupLichThiTable() {
        chungChiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getChungChi().getTenChungChi()));
        ngayGioThiColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNgayGioThi()));
        soLuongTSColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSoLuongHienTai()));
        phiColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getChungChi().getLePhi()));

        lichThiTable.setItems(lichThiList);
    }

    private void setupLichThiDaChonTable() {
        chungChiColumn1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getChungChi().getTenChungChi()));
        ngayGioThiColumn1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNgayGioThi()));
        phiColumn1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getChungChi().getLePhi()));

        // Add delete button column
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Xóa");

            {
                deleteButton.setOnAction(event -> {
                    LichThiDTO lichThi = getTableView().getItems().get(getIndex());
                    selectedLichThiList.remove(lichThi);
                    updateTongTien();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        lichThiDaChonTable.setItems(selectedLichThiList);
    }

    private void setupThiSinhDangKyTable() {
        sttColumn.setCellValueFactory(data -> {
            int index = thiSinhDangKyTable.getItems().indexOf(data.getValue()) + 1;
            return new SimpleObjectProperty<>(index);
        });
        cfChungChiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getChungChi().getTenChungChi()));
        cfNgayGioThiColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNgayGioThi()));
        cfPhiColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getChungChi().getLePhi()));

        thiSinhDangKyTable.setItems(selectedLichThiList);
    }

    private void setupKhachHangTable() {
        hoTenKHColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoTen()));
        CCCDKHColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCccd()));
        emailKHColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        sdtKHColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSdt()));

        // Add selection listener to populate form fields
        khachHangTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                hoTenField.setText(newSelection.getHoTen());
                CCCDField.setText(newSelection.getCccd());
                emailField.setText(newSelection.getEmail());
                sdtField.setText(newSelection.getSdt());
            }
        });
    }

    private void setupThiSinhTable() {
        hoTenTSColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoTen()));
        CCCDTSColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCccd()));
        ngaySinhTSColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNgaySinh()));
        gioiTinhTSColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGioiTinh()));

        // Add a selection listener to populate form fields
        thiSinhTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                hoTenThiSinhField.setText(newSelection.getHoTen());
                CCCDThiSinhField.setText(newSelection.getCccd());
                java.util.Date date = newSelection.getNgaySinh();
                java.time.LocalDate localDate;
                if (date instanceof java.sql.Date) {
                    localDate = ((java.sql.Date) date).toLocalDate();
                } else {
                    localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                }
                ngaySinhThiSinhField.setValue(localDate);
                gioiTinhThiSinhCombo.setValue(newSelection.getGioiTinh());
            }
        });
    }

    private void loadKhachHangData() {
        try {
            List<KhachHangDTO> khachHangList = khachHangBUS.layDSKhachHangCaNhan();
            khachHangTable.setItems(FXCollections.observableArrayList(khachHangList));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách khách hàng", e.getMessage());
        }
    }

    private void loadThiSinhData() {
        try {
            List<ThiSinhDTO> thiSinhList = thiSinhBUS.layDSThiSinh();
            thiSinhTable.setItems(FXCollections.observableArrayList(thiSinhList));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách thí sinh", e.getMessage());
        }
    }

    private void loadLichThiData() {
        try {
            List<LichThiDTO> allLichThi = lichThiBUS.layDSLichThiMoi();
            lichThiList.setAll(allLichThi);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách lịch thi", e.getMessage());
        }
    }

    private void setupChungChiComboBox() {
        try {
            // Get unique certificate names from the exam schedule list
            ObservableList<String> chungChiNames = FXCollections.observableArrayList();
            chungChiNames.add("Tất cả");
            for (LichThiDTO lichThi : lichThiList) {
                String tenChungChi = lichThi.getChungChi().getTenChungChi();
                if (!chungChiNames.contains(tenChungChi)) {
                    chungChiNames.add(tenChungChi);
                }
            }
            cchiCombo.setItems(chungChiNames);

            // Add listener for filtering
            cchiCombo.valueProperty().addListener((obs, oldVal, newVal) -> filterLichThi());
            ngayThiDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> filterLichThi());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách chứng chỉ", e.getMessage());
        }
    }

    private void filterLichThi() {
        try {
            String selectedChungChi = cchiCombo.getValue();
            LocalDate selectedDate = ngayThiDatePicker.getValue();

            List<LichThiDTO> allLichThi = lichThiBUS.layDSLichThiMoi();
            ObservableList<LichThiDTO> filteredList = FXCollections.observableArrayList();

            for (LichThiDTO lichThi : allLichThi) {
                boolean matchesChungChi = selectedChungChi == null || selectedChungChi.equals("Tất cả") ||
                    lichThi.getChungChi().getTenChungChi().equals(selectedChungChi);

                boolean matchesDate = selectedDate == null || 
                    lichThi.getNgayGioThi().toLocalDateTime().toLocalDate().equals(selectedDate);

                if (matchesChungChi && matchesDate) {
                    filteredList.add(lichThi);
                }
            }

            lichThiList.setAll(filteredList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể lọc danh sách lịch thi", e.getMessage());
        }
    }

    @FXML
    void themLichThi(ActionEvent event) {
        LichThiDTO selectedLichThi = lichThiTable.getSelectionModel().getSelectedItem();
        if (selectedLichThi == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Chưa chọn lịch thi", 
                "Vui lòng chọn một lịch thi từ danh sách.");
            return;
        }

        // Check if the exam schedule is already selected
        for (LichThiDTO lichThi : selectedLichThiList) {
            if (lichThi.getMaLichThi().equals(selectedLichThi.getMaLichThi())) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Lịch thi đã được chọn", 
                    "Lịch thi này đã được thêm vào danh sách.");
                return;
            }
        }

        // Add to selected list
        selectedLichThiList.add(selectedLichThi);
        updateTongTien();
    }

    private void updateTongTien() {
        double tongTien = 0;
        for (LichThiDTO lichThi : selectedLichThiList) {
            tongTien += lichThi.getChungChi().getLePhi();
        }
        tongTienLabel.setText(String.format("%,.0f đ", tongTien));
    }

    @FXML
    void btnTiepTheo(ActionEvent event) {
        if (currentStep < totalSteps) {
            if (kiemTraForm()) {
                currentStep++;
                stepProgress.setCurrentStep(currentStep);
                updateContentVisibility();
                updateButtonVisibility();

                if (currentStep == totalSteps) {
                    populateConfirmationSection();
                }
            }
        } else {
            btnDangKyThi(event);
        }
    }

    @FXML
    void btnQuayLai(ActionEvent event) {
        if (currentStep > 1) {
            currentStep--;
            stepProgress.setCurrentStep(currentStep);
            updateContentVisibility();
            updateButtonVisibility();
        }
    }

    @FXML
    void btnDangKyThi(ActionEvent event) {
        try {
            // Create KhachHangDTO
            // maKH will be generated by the database
            // diaChi is empty for individual
            // loai_kh
            // ===== Data =====
            KhachHangDTO khachHang = new KhachHangDTO(
                    null, // maKH will be generated by the database
                    hoTenField.getText().trim(),
                    emailField.getText().trim(),
                    CCCDField.getText().trim(),
                    sdtField.getText().trim(),
                    "", // diaChi is empty for individual
                    "Cá nhân" // loai_kh
            );

            // Save the customer
            khachHangBUS.taoKhachHang(khachHang);

            // Reload the customer to get the generated ID
            List<KhachHangDTO> khachHangList = khachHangBUS.layDSKhachHangCaNhan();
            for (KhachHangDTO kh : khachHangList) {
                if (kh.getCccd().equals(khachHang.getCccd())) {
                    khachHang = kh;
                    break;
                }
            }

            // Create ThiSinhDTO
            // maThiSinh will be generated by the database
            ThiSinhDTO thiSinh = new ThiSinhDTO(
                    null, // maThiSinh will be generated by the database
                    hoTenThiSinhField.getText().trim(),
                    CCCDThiSinhField.getText().trim(),
                    java.sql.Date.valueOf(ngaySinhThiSinhField.getValue()),
                    gioiTinhThiSinhCombo.getValue()
            );

            // Save the candidate
            thiSinhBUS.taoThiSinh(thiSinh);

            // Reload the candidate to get the generated ID
            thiSinh = thiSinhBUS.layThiSinhBangCCCD(thiSinh.getCccd());

            // Create PhieuDangKyDTO
            PhieuDangKyDTO phieuDangKy = new PhieuDangKyDTO(
                null, // maPhieuDangKy will be generated by the database
                "Chờ xử lý", // trangThai
                new Date(), // ngayLap - current date
                "", // diaChiGiao is empty for individual
                    khachHang, // maKH
                "NV000001" // maNVTao - placeholder
            );

            // Save the registration form
            phieuDangKyBUS.taoPhieuDangKy(phieuDangKy);

            // Register for all selected exam schedules
            for (LichThiDTO lichThi : selectedLichThiList) {
                ChiTietPDKDTO chiTietPDK = new ChiTietPDKDTO(
                    null, // maCTPDK will be generated by the database
                    phieuDangKy.getMaPhieuDangKy(),
                    thiSinh.getMaThiSinh(),
                    lichThi.getMaLichThi()
                );
                chiTietPDKBUS.themChiTietPDK(chiTietPDK);
            }

            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng ký thành công",
                "Bạn đã đăng ký thành công cho " + selectedLichThiList.size() + " lịch thi.");

            // Reset the form
            resetForm();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể đăng ký",
                "Có lỗi xảy ra khi đăng ký: " + e.getMessage());
        }
    }

    private void resetForm() {
        // Reset customer fields
        hoTenField.clear();
        CCCDField.clear();
        emailField.clear();
        sdtField.clear();

        // Reset candidate fields
        hoTenThiSinhField.clear();
        CCCDThiSinhField.clear();
        ngaySinhThiSinhField.setValue(null);
        gioiTinhThiSinhCombo.setValue("Nam");

        // Reset selected exam schedules
        selectedLichThiList.clear();
        updateTongTien();

        // Reset filters
        cchiCombo.setValue(null);
        ngayThiDatePicker.setValue(null);
        loadLichThiData();

        // Reset step progress
        currentStep = 1;
        stepProgress.setCurrentStep(currentStep);
        updateContentVisibility();
        updateButtonVisibility();
    }

    private boolean kiemTraForm() {
        return switch (currentStep) {
            case 1 -> kiemTraThongTinCaNhan();
            case 2 -> kiemTraLichThi();
            default -> true;
        };
    }

    private boolean kiemTraThongTinCaNhan() {
        // Validate customer information
        if (hoTenField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Họ tên trống", "Vui lòng nhập họ tên khách hàng.");
            return false;
        }

        if (CCCDField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "CCCD trống", "Vui lòng nhập CCCD khách hàng.");
            return false;
        }

        if (!CCCDField.getText().trim().matches("^[0-9]{12}$")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "CCCD không hợp lệ", "CCCD phải có 12 chữ số.");
            return false;
        }

        // Validate email format if provided
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Email không hợp lệ",
                "Vui lòng nhập đúng định dạng email.");
            return false;
        }

        // Validate phone number format if provided
        String sdt = sdtField.getText().trim();
        if (!sdt.isEmpty() && !sdt.matches("^[0-9]{10,11}$")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại không hợp lệ",
                "Vui lòng nhập đúng định dạng số điện thoại (10-11 số).");
            return false;
        }

        // Validate candidate information
        if (hoTenThiSinhField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Họ tên thí sinh trống", "Vui lòng nhập họ tên thí sinh.");
            return false;
        }

        if (CCCDThiSinhField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "CCCD thí sinh trống", "Vui lòng nhập CCCD thí sinh.");
            return false;
        }

        if (!CCCDThiSinhField.getText().trim().matches("^[0-9]{12}$")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "CCCD thí sinh không hợp lệ", "CCCD phải có 12 chữ số.");
            return false;
        }

        if (ngaySinhThiSinhField.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Ngày sinh thí sinh trống", "Vui lòng chọn ngày sinh thí sinh.");
            return false;
        }

        return true;
    }

    private boolean kiemTraLichThi() {
        if (selectedLichThiList.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Chưa chọn lịch thi",
                "Vui lòng chọn ít nhất một lịch thi để đăng ký.");
            return false;
        }
        return true;
    }

    private void updateContentVisibility() {
        // Hide all sections
        infoSection.setVisible(false);
        examScheduleSection.setVisible(false);
        confirmationSection.setVisible(false);

        // Show only the current section
        switch (currentStep) {
            case 1:
                infoSection.setVisible(true);
                break;
            case 2:
                examScheduleSection.setVisible(true);
                break;
            case 3:
                confirmationSection.setVisible(true);
                break;
        }
    }

    private void populateConfirmationSection() {
        // Populate customer information
        hoTenLabel.setText(hoTenField.getText());
        CCCDLabel.setText(CCCDField.getText());
        emailLabel.setText(emailField.getText());
        sdtLabel.setText(sdtField.getText());

        // Populate candidate information
        hoTenThiSinhLabel.setText(hoTenThiSinhField.getText());
        CCCDThiSinhLabel.setText(CCCDThiSinhField.getText());
        ngaySinhThiSinhLabel.setText(ngaySinhThiSinhField.getValue().toString());
        gioiTinhThiSinhLabel.setText(gioiTinhThiSinhCombo.getValue());

        // Update total cost
        updateTongTien();
    }

    private void updateButtonVisibility() {
        // Disable the "Previous" button on the first step
        prevButton.setDisable(currentStep == 1);

        // Change the "Next" button text on the last step
        if (currentStep == totalSteps) {
            nextButton.setText("Đăng ký thi");
        } else {
            nextButton.setText("Tiếp theo");
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
