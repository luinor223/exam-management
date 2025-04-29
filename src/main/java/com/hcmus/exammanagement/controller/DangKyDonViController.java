package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.KhachHangBUS;
import com.hcmus.exammanagement.bus.PhieuDangKyBUS;
import com.hcmus.exammanagement.bus.ThiSinhBUS;
import com.hcmus.exammanagement.component.StepProgress;
import com.hcmus.exammanagement.dto.KhachHangDTO;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;
import com.hcmus.exammanagement.dto.ThiSinhDTO;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.Date;
import java.util.List;

public class DangKyDonViController {

    // BUS services
    private final KhachHangBUS khachHangBUS = new KhachHangBUS();
    private final ThiSinhBUS thiSinhBUS = new ThiSinhBUS();
    private final PhieuDangKyBUS phieuDangKyBUS = new PhieuDangKyBUS();

    // Data
    private KhachHangDTO selectedDonVi;
    private final ObservableList<ThiSinhDTO> thiSinhList = FXCollections.observableArrayList();

    // Step progress bar
    @FXML private VBox progressContainer;
    private StepProgress stepProgress;
    @FXML private Button nextButton;
    @FXML private Button prevButton;

    // Sections
    @FXML private AnchorPane organizationSection;
    @FXML private AnchorPane candidateSection;
    @FXML private ScrollPane confirmationSection;
    private int currentStep = 1;
    private final int totalSteps = 3;

    // Form fields for organization
    @FXML private TextField tenDonViField;
    @FXML private TextField maSoThueField;
    @FXML private TextField emailField;
    @FXML private TextField sdtField;
    @FXML private TextField diaChiField;

    // Form fields for candidate
    @FXML private TextField candidateNameField;
    @FXML private TextField candidateIdField;
    @FXML private DatePicker candidateDobPicker;
    @FXML private JFXComboBox<String> genderComboBox;

    // Table DonVi
    @FXML private TableView<KhachHangDTO> donViTable;
    @FXML private TableColumn<KhachHangDTO, String> tenDonViColumn;
    @FXML private TableColumn<KhachHangDTO, String> maSoThueColumn;
    @FXML private TableColumn<KhachHangDTO, String> emailColumn;
    @FXML private TableColumn<KhachHangDTO, String> sdtColumn;
    @FXML private TableColumn<KhachHangDTO, String> diaChiColumn;

    // Table ThiSinh
    @FXML private TableView<ThiSinhDTO> thiSinhTable;
    @FXML private TableColumn<ThiSinhDTO, String> tenTSColumn;
    @FXML private TableColumn<ThiSinhDTO, String> cccdTSColumn;
    @FXML private TableColumn<ThiSinhDTO, Date> ngSinhTSColumn;
    @FXML private TableColumn<ThiSinhDTO, String> gioiTinhTSColumn;
    @FXML private TableColumn<ThiSinhDTO, Void> thiSinhActionColumn;

    // Summary information
    @FXML private Label tenDonViLabel;
    @FXML private Label maSoThueLabel;
    @FXML private Label emailLabel;
    @FXML private Label sdtLabel;
    @FXML private Label diaChiLabel;
    @FXML private Label maLichThiConfirmLabel;
    @FXML private Label ngayGioThiConfirmLabel;
    @FXML private Label thoiLuongConfirmLabel;
    @FXML private Label soLuongTSConfirmLabel;
    @FXML private Label chungChiConfirmLabel;

    // Confirmation table
    @FXML private TableView<ThiSinhDTO> thiSinhDangKyTable;
    @FXML private TableColumn<ThiSinhDTO, Integer> sttColumn;
    @FXML private TableColumn<ThiSinhDTO, String> hoTenTSDKColumn;
    @FXML private TableColumn<ThiSinhDTO, String> gioiTinhTSDKColumn;
    @FXML private TableColumn<ThiSinhDTO, Date> ngSinhTSDKColumn;
    @FXML private TableColumn<ThiSinhDTO, String> cccdTSDKColumn;

    @FXML private Label tongThiSinhLabel;
    @FXML private Label tongTienLabel;

    @FXML
    public void initialize() {
        stepProgress = new StepProgress();
        List<String> steps = List.of("Thông tin đơn vị", "Danh sách thí sinh", "Xác nhận");
        stepProgress.configureSteps(steps);
        stepProgress.setCurrentStep(currentStep);

        progressContainer.getChildren().add(stepProgress);

        genderComboBox.getItems().addAll("Nam", "Nữ");
        genderComboBox.setValue("Nam");

        setupDonViTable();
        loadDonViData();

        setupThiSinhTable();
        thiSinhTable.setItems(thiSinhList);

        setupThiSinhDangKyTable();

        updateContentVisibility();
        updateButtonVisibility();
    }

    private void setupDonViTable() {
        tenDonViColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoTen()));
        maSoThueColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCccd()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        sdtColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSdt()));
        diaChiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDiaChi()));

        donViTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedDonVi = newSelection;
                tenDonViField.setText(newSelection.getHoTen());
                maSoThueField.setText(newSelection.getCccd());
                emailField.setText(newSelection.getEmail());
                sdtField.setText(newSelection.getSdt());
                diaChiField.setText(newSelection.getDiaChi());
            }
        });
    }

    private void loadDonViData() {
        try {
            List<KhachHangDTO> donViList = khachHangBUS.layDSKhachHangDonVi();
            donViTable.setItems(FXCollections.observableArrayList(donViList));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách đơn vị", e.getMessage());
        }
    }

    private void setupThiSinhTable() {
        tenTSColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoTen()));
        cccdTSColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCccd()));
        ngSinhTSColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNgaySinh()));
        gioiTinhTSColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGioiTinh()));

        // Add delete button column
        thiSinhActionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Xóa");

            {
                deleteButton.setOnAction(event -> {
                    ThiSinhDTO thiSinh = getTableView().getItems().get(getIndex());
                    thiSinhList.remove(thiSinh);
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
    }

    private void setupThiSinhDangKyTable() {
        sttColumn.setCellValueFactory(data -> {
            int index = thiSinhDangKyTable.getItems().indexOf(data.getValue()) + 1;
            return new javafx.beans.property.SimpleObjectProperty<>(index);
        });
        hoTenTSDKColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoTen()));
        gioiTinhTSDKColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGioiTinh()));
        ngSinhTSDKColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNgaySinh()));
        cccdTSDKColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCccd()));
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleNextStep() {
        if (currentStep < totalSteps) {
            if (validateCurrentStep()) {
                currentStep++;
                stepProgress.setCurrentStep(currentStep);
                updateContentVisibility();
                updateButtonVisibility();

                if (currentStep == totalSteps) {
                    populateConfirmationSection();
                }
            }
        } else {
            handleRegister();
        }
    }

    @FXML
    private void handlePrevStep() {
        if (currentStep > 1) {
            currentStep--;
            stepProgress.setCurrentStep(currentStep);
            updateContentVisibility();
            updateButtonVisibility();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            // Create or use existing KhachHangDTO
            KhachHangDTO khachHang;
            if (selectedDonVi != null) {
                // Use existing organization
                khachHang = selectedDonVi;
            } else {
                // Create a new organization
                khachHang = new KhachHangDTO(
                        null, // maKH will be generated by the database
                        tenDonViField.getText().trim(),
                        emailField.getText().trim(),
                        maSoThueField.getText().trim(), // Using maSoThue as cccd
                        sdtField.getText().trim(),
                        diaChiField.getText().trim(),
                        "Đơn vị" // loai_kh
                );

                // Save the new organization
                boolean success = khachHangBUS.taoKhachHang(khachHang);
                if (!success) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo đơn vị",
                            "Có lỗi xảy ra khi tạo đơn vị. Vui lòng thử lại.");
                    return;
                }

                // Reload the organization to get the generated ID
                List<KhachHangDTO> donViList = khachHangBUS.layDSKhachHangDonVi();
                for (KhachHangDTO dv : donViList) {
                    if (dv.getHoTen().equals(khachHang.getHoTen()) &&
                            dv.getCccd().equals(khachHang.getCccd())) {
                        khachHang = dv;
                        break;
                    }
                }
            }

            // Create PhieuDangKyDTO
            PhieuDangKyDTO phieuDangKy = new PhieuDangKyDTO(
                    null, // maPhieuDangKy will be generated by the database
                    null, // hanNop - not used in this context
                    "Chờ xử lý", // trangThai
                    new Date(), // ngayLap - current date
                    diaChiField.getText().trim(), // diaChiGiao
                    khachHang.getMaKH(), // maKH
                    "NV001" // maNVTao - placeholder, in a real app this would come from the logged-in user
            );

            // Save the registration form
            phieuDangKyBUS.taoPhieuDangKy(phieuDangKy);

            // Register all candidates
            for (ThiSinhDTO thiSinh : thiSinhList) {
                thiSinhBUS.createThiSinh(thiSinh);
            }

            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng ký thành công",
                    "Đơn vị " + khachHang.getHoTen() + " đã đăng ký thành công cho " +
                            thiSinhList.size() + " thí sinh.");

            // Reset the form
            resetForm();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể đăng ký",
                    "Có lỗi xảy ra khi đăng ký: " + e.getMessage());
        }
    }

    private void resetForm() {
        // Reset organization fields
        tenDonViField.clear();
        maSoThueField.clear();
        emailField.clear();
        sdtField.clear();
        diaChiField.clear();
        selectedDonVi = null;

        // Reset candidate list
        thiSinhList.clear();

        // Reset candidate fields
        candidateNameField.clear();
        candidateIdField.clear();
        candidateDobPicker.setValue(null);
        genderComboBox.setValue("Nam");

        // Reset step progress
        currentStep = 1;
        stepProgress.setCurrentStep(currentStep);
        updateContentVisibility();
        updateButtonVisibility();
    }

    @FXML
    private void handleAddCandidate() {
        // Validate candidate information
        String hoTen = candidateNameField.getText().trim();
        String cccd = candidateIdField.getText().trim();
        java.time.LocalDate ngaySinhLocal = candidateDobPicker.getValue();
        String gioiTinh = genderComboBox.getValue();

        if (hoTen.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Họ tên trống", "Vui lòng nhập họ tên thí sinh.");
            return;
        }

        if (cccd.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "CCCD trống", "Vui lòng nhập CCCD thí sinh.");
            return;
        }

        if (!cccd.matches("^[0-9]{12}$")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "CCCD không hợp lệ", "CCCD phải có 12 chữ số.");
            return;
        }

        if (ngaySinhLocal == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Ngày sinh trống", "Vui lòng chọn ngày sinh thí sinh.");
            return;
        }

        // Check if candidate already exists in the list
        for (ThiSinhDTO ts : thiSinhList) {
            if (ts.getCccd().equals(cccd)) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Thí sinh đã tồn tại",
                        "Thí sinh với CCCD " + cccd + " đã được thêm vào danh sách.");
                return;
            }
        }

        // Convert LocalDate to Date
        Date ngaySinh = java.sql.Date.valueOf(ngaySinhLocal);

        // Create new ThiSinhDTO and add to list
        ThiSinhDTO thiSinh = new ThiSinhDTO(null, hoTen, cccd, ngaySinh, gioiTinh);
        thiSinhList.add(thiSinh);

        // Clear input fields
        candidateNameField.clear();
        candidateIdField.clear();
        candidateDobPicker.setValue(null);
        genderComboBox.setValue("Nam");

        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm thí sinh thành công",
                "Thí sinh " + hoTen + " đã được thêm vào danh sách.");
    }

    @FXML
    private void handleChooseSchedule() {
        // In a real implementation, this would open a dialog to select an exam schedule
        // For now, we'll just show a dialog to inform the user that this functionality is not yet implemented
        showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Chức năng chưa được triển khai",
                "Chức năng chọn lịch thi chưa được triển khai trong phiên bản này.");
    }

    private boolean validateCurrentStep() {
        return switch (currentStep) {
            case 1 -> validateOrganizationInfo();
            case 2 -> validateCandidateInfo();
            case 3 -> validateExamSchedule();
            default -> true;
        };
    }

    private boolean validateOrganizationInfo() {
        // Check if an organization is selected or new information is entered
        if (selectedDonVi == null &&
                (tenDonViField.getText().trim().isEmpty() ||
                        maSoThueField.getText().trim().isEmpty() ||
                        emailField.getText().trim().isEmpty() ||
                        sdtField.getText().trim().isEmpty() ||
                        diaChiField.getText().trim().isEmpty())) {

            showAlert(Alert.AlertType.ERROR, "Lỗi", "Thông tin không hợp lệ",
                    "Vui lòng chọn một đơn vị từ danh sách hoặc điền đầy đủ thông tin đơn vị mới.");
            return false;
        }

        // Validate email format if provided
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
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

        return true;
    }

    private boolean validateCandidateInfo() {
        // Check if there are any candidates added
        if (thiSinhList.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Danh sách thí sinh trống",
                    "Vui lòng thêm ít nhất một thí sinh vào danh sách.");
            return false;
        }
        return true;
    }

    private boolean validateExamSchedule() {
        // In a real implementation, we would validate that an exam schedule has been selected
        // For now, we'll just return true since we're using placeholder values
        return true;
    }

    private void updateContentVisibility() {
        // Hide all sections
        organizationSection.setVisible(false);
        candidateSection.setVisible(false);
        confirmationSection.setVisible(false);

        // Show only the current section
        switch (currentStep) {
            case 1:
                organizationSection.setVisible(true);
                break;
            case 2:
                candidateSection.setVisible(true);
                break;
            case 3:
                confirmationSection.setVisible(true);
                break;
        }
    }

    private void populateConfirmationSection() {
        // Populate organization information
        tenDonViLabel.setText(tenDonViField.getText());
        maSoThueLabel.setText(maSoThueField.getText());
        emailLabel.setText(emailField.getText());
        sdtLabel.setText(sdtField.getText());
        diaChiLabel.setText(diaChiField.getText());

        // Populate candidate information
        thiSinhDangKyTable.setItems(thiSinhList);

        // Update total candidates and cost
        int totalCandidates = thiSinhList.size();
        tongThiSinhLabel.setText(String.valueOf(totalCandidates));

        // Calculate total cost (assuming 500,000 VND per candidate)
        long totalCost = totalCandidates * 500000L;
        tongTienLabel.setText(String.format("%,d đ", totalCost));

        // For now, we'll use placeholder values for exam schedule information
        // In a real implementation, this would come from the selected exam schedule
        maLichThiConfirmLabel.setText("LT001");
        ngayGioThiConfirmLabel.setText("01/01/2024 08:00");
        thoiLuongConfirmLabel.setText("120 phút");
        soLuongTSConfirmLabel.setText(String.valueOf(totalCandidates));
        chungChiConfirmLabel.setText("TOEIC");
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
}
