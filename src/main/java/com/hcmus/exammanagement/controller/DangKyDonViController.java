package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.ChiTietPDKBUS;
import com.hcmus.exammanagement.bus.KhachHangBUS;
import com.hcmus.exammanagement.bus.PhieuDangKyBUS;
import com.hcmus.exammanagement.bus.ThiSinhBUS;
import com.hcmus.exammanagement.component.StepProgress;
import com.hcmus.exammanagement.dto.ChiTietPDKDTO;
import com.hcmus.exammanagement.dto.KhachHangDTO;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;
import com.hcmus.exammanagement.dto.ThiSinhDTO;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    @FXML private AnchorPane confirmationSection;
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

    // Confirmation table
    @FXML private TableView<ThiSinhDTO> thiSinhDangKyTable;
    @FXML private TableColumn<ThiSinhDTO, Integer> sttColumn;
    @FXML private TableColumn<ThiSinhDTO, String> hoTenTSDKColumn;
    @FXML private TableColumn<ThiSinhDTO, String> gioiTinhTSDKColumn;
    @FXML private TableColumn<ThiSinhDTO, Date> ngSinhTSDKColumn;
    @FXML private TableColumn<ThiSinhDTO, String> cccdTSDKColumn;

    @FXML
    public void initialize() {
        stepProgress = new StepProgress();
        List<String> steps = List.of("Thông tin đơn vị", "Danh sách thí sinh", "Xác nhận");
        stepProgress.configureSteps(steps);
        stepProgress.setCurrentStep(currentStep);

        progressContainer.getChildren().add(stepProgress);

        genderComboBox.getItems().addAll("Nam", "Nữ", "Khác");
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
    private void btnQuayLai() {
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
            btnDangKyThi();
        }
    }

    @FXML
    private void btnTiepTheo() {
        if (currentStep > 1) {
            currentStep--;
            stepProgress.setCurrentStep(currentStep);
            updateContentVisibility();
            updateButtonVisibility();
        }
    }

    @FXML
    private void btnDangKyThi() {
        try {
            KhachHangDTO khachHang;
            if (selectedDonVi != null) {
                khachHang = selectedDonVi;
            } else {
                khachHang = new KhachHangDTO(
                        null, // maKH will be generated by the database
                        tenDonViField.getText().trim(),
                        emailField.getText().trim(),
                        maSoThueField.getText().trim(), // Using maSoThue as cccd
                        sdtField.getText().trim(),
                        diaChiField.getText().trim(),
                        "Đơn vị"
                );

                khachHangBUS.taoKhachHang(khachHang);

                // Reload the organization to get the generated ID
                List<KhachHangDTO> donViList = khachHangBUS.layDSKhachHangDonVi();
                for (KhachHangDTO dv : donViList) {
                    if (dv.getCccd().equals(khachHang.getCccd())) {
                        khachHang = dv;
                        break;
                    }
                }
            }

            // Create PhieuDangKyDTO
            PhieuDangKyDTO phieuDangKy = new PhieuDangKyDTO(
                    null, // maPhieuDangKy will be generated by the database
                    "Chờ xếp lịch", // trangThai
                    new Date(), // ngayLap - current date
                    diaChiField.getText().trim(), // diaChiGiao
                    khachHang, // maKH
                    "NV000001" // TODO: - placeholder
            );

            phieuDangKyBUS.taoPhieuDangKy(phieuDangKy);

            for (ThiSinhDTO thiSinh : thiSinhList) {
                ThiSinhDTO existingThiSinh = thiSinhBUS.layThiSinhBangCCCD(thiSinh.getCccd());
                String maThiSinh;

                if (existingThiSinh != null) {
                    // Update existing candidate
                    thiSinh.setMaThiSinh(existingThiSinh.getMaThiSinh());
                    thiSinhBUS.capNhatThiSinh(thiSinh);
                    maThiSinh = existingThiSinh.getMaThiSinh();
                } else {
                    // Create new candidate
                    thiSinhBUS.taoThiSinh(thiSinh);
                    // Get the newly created candidate ID
                    maThiSinh = thiSinhBUS.layThiSinhBangCCCD(thiSinh.getCccd()).getMaThiSinh();
                }

                // Create registration detail
                ChiTietPDKBUS.themChiTietPDK(new ChiTietPDKDTO(
                        null,
                        phieuDangKy.getMaPhieuDangKy(),
                        maThiSinh,
                        null
                ));
            }

            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng ký thành công",
                    "Đơn vị " + khachHang.getHoTen() + " đã đăng ký thành công cho " +
                            thiSinhList.size() + " thí sinh.");

            resetForm();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể đăng ký",
                    "Có lỗi xảy ra khi đăng ký: " + e.getMessage());
        }
    }

    private void resetForm() {
        tenDonViField.clear();
        maSoThueField.clear();
        emailField.clear();
        sdtField.clear();
        diaChiField.clear();
        selectedDonVi = null;

        thiSinhList.clear();

        candidateNameField.clear();
        candidateIdField.clear();
        candidateDobPicker.setValue(null);
        genderComboBox.setValue("Nam");

        currentStep = 1;
        stepProgress.setCurrentStep(currentStep);
        updateContentVisibility();
        updateButtonVisibility();
    }

    @FXML
    private void btnThemThiSinh() {
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

        for (ThiSinhDTO ts : thiSinhList) {
            if (ts.getCccd().equals(cccd)) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Thí sinh đã tồn tại",
                        "Thí sinh với CCCD " + cccd + " đã được thêm vào danh sách.");
                return;
            }
        }

        Date ngaySinh = java.sql.Date.valueOf(ngaySinhLocal);

        ThiSinhDTO thiSinh = new ThiSinhDTO(null, hoTen, cccd, ngaySinh, gioiTinh);
        thiSinhList.add(thiSinh);

        candidateNameField.clear();
        candidateIdField.clear();
        candidateDobPicker.setValue(null);
        genderComboBox.setValue("Nam");

        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm thí sinh thành công",
                "Thí sinh " + hoTen + " đã được thêm vào danh sách.");
    }

    private boolean kiemTraForm() {
        return switch (currentStep) {
            case 1 -> kiemTraThongTinDonVi();
            case 2 -> kiemTraThongTinThiSinh();
            default -> true;
        };
    }

    private boolean kiemTraThongTinDonVi() {
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

        String email = emailField.getText().trim();
        if (!email.isEmpty() && !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Email không hợp lệ",
                    "Vui lòng nhập đúng định dạng email.");
            return false;
        }

        String sdt = sdtField.getText().trim();
        if (!sdt.isEmpty() && !sdt.matches("^[0-9]{10,11}$")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại không hợp lệ",
                    "Vui lòng nhập đúng định dạng số điện thoại (10-11 số).");
            return false;
        }

        return true;
    }

    private boolean kiemTraThongTinThiSinh() {
        if (thiSinhList.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Danh sách thí sinh trống",
                    "Vui lòng thêm ít nhất một thí sinh vào danh sách.");
            return false;
        }
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

        thiSinhDangKyTable.setItems(thiSinhList);
    }

    private void updateButtonVisibility() {
        prevButton.setDisable(currentStep == 1);

        if (currentStep == totalSteps) {
            nextButton.setText("Đăng ký thi");
        } else {
            nextButton.setText("Tiếp theo");
        }
    }
}
