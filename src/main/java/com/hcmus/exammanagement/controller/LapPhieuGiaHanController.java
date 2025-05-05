package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.bus.LichThiBUS;
import com.hcmus.exammanagement.bus.PhieuGiaHanBUS;
import com.hcmus.exammanagement.dao.LichThiDAO;
import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.LichThiDTO;
import com.hcmus.exammanagement.dto.PhieuGiaHanDTO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LapPhieuGiaHanController {

    @FXML private TextField txtMaTS;
    @FXML private TableView<Map<String, Object>> tblLichSuGH;
    @FXML private TableColumn<Map<String, Object>, String> colMaPGH;
    @FXML private TableColumn<Map<String, Object>, String> colLoaiGH;
    @FXML private TableColumn<Map<String, Object>, Double> colPhiGH;
    @FXML private TableColumn<Map<String, Object>, String> colMaCTPDK;
    @FXML private TableColumn<Map<String, Object>, String> colNgayThi;
    @FXML private TableColumn<Map<String, Object>, Boolean> colThanhToan;
    @FXML private Button btnThemGiaHan;
    @FXML private TableColumn<Map<String, Object>, Void> colHanhDong;


    @FXML private Label lblTieuDe;

    @FXML private TableColumn<Map<String, Object>, String> colMaCCHI;
    @FXML private TableColumn<Map<String, Object>, String> colSBD;
    @FXML private TableColumn<Map<String, Object>, String> colMaPhong;

    private final ObservableList<Map<String, Object>> dataList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Các cột cũ...
        colMaPGH.setCellValueFactory(cell -> new SimpleStringProperty(((PhieuGiaHanDTO) cell.getValue().get("dto")).getMaPhieuGH()));
        colLoaiGH.setCellValueFactory(cell -> new SimpleStringProperty(((PhieuGiaHanDTO) cell.getValue().get("dto")).getLoaiGH()));
        colPhiGH.setCellValueFactory(cell -> new SimpleDoubleProperty(((PhieuGiaHanDTO) cell.getValue().get("dto")).getPhiGH()).asObject());
        colThanhToan.setCellValueFactory(cellData -> {
            PhieuGiaHanDTO dto = (PhieuGiaHanDTO) cellData.getValue().get("dto");
            BooleanProperty booleanProperty = new SimpleBooleanProperty(dto.isDaThanhToan());

            // Lắng nghe thay đổi
            booleanProperty.addListener((obs, oldVal, newVal) -> {
                dto.setDaThanhToan(newVal);
                boolean success = PhieuGiaHanBUS.capNhatThanhToan(dto.getMaPhieuGH(), newVal);
                if (!success) {
                    showAlert("Lỗi", "Cập nhật trạng thái thanh toán thất bại!");
                    booleanProperty.set(oldVal); // rollback nếu lỗi
                }
            });

            return booleanProperty;
        });
        colThanhToan.setCellFactory(tc -> new CheckBoxTableCell<>());

        colMaCTPDK.setCellValueFactory(cell -> new SimpleStringProperty((String) cell.getValue().get("ma_ctpdk")));
        colNgayThi.setCellValueFactory(cell -> {
            Timestamp ts = (Timestamp) cell.getValue().get("ngay_thi");
            String formatted = (ts != null) ? ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
            return new SimpleStringProperty(formatted);
        });

        // Cột mới
        colMaCCHI.setCellValueFactory(cell -> new SimpleStringProperty((String) cell.getValue().get("ma_cchi")));
        colSBD.setCellValueFactory(cell -> new SimpleStringProperty((String) cell.getValue().get("sbd")));
        colMaPhong.setCellValueFactory(cell -> new SimpleStringProperty((String) cell.getValue().get("ma_phong")));

        colHanhDong.setCellFactory(col -> new TableCell<>() {
            private final Button btnXoa = new Button("Xóa");

            {
                btnXoa.setOnAction(event -> {
                    Map<String, Object> item = getTableView().getItems().get(getIndex());
                    PhieuGiaHanDTO dto = (PhieuGiaHanDTO) item.get("dto");
                    String maPGH = dto.getMaPhieuGH();

                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "Bạn có chắc chắn muốn xóa phiếu gia hạn: " + maPGH + "?",
                            ButtonType.YES, ButtonType.NO);
                    confirm.setTitle("Xác nhận xóa");

                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            xoaPhieuGiaHan(maPGH);
                            handleTraCuu();  // reload lại bảng
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnXoa);
                }
            }
        });

        tblLichSuGH.setItems(dataList);
    }


    @FXML
    private void handleTraCuu() {
        String maTS = txtMaTS.getText().trim();
        if (maTS.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập mã thí sinh.");
            return;
        }
        loadLichSuGiaHan(maTS);
    }

    private void loadLichSuGiaHan(String maTS) {
        dataList.clear();
        try (Connection conn = Database.getConnection()) {
            String sql = """
            SELECT pgh.ma_pgh, pgh.loai_gh, pgh.phi_gh, pgh.nhan_vien_tao, pgh.da_thanh_toan,
                   pgh.ma_ctpdk, ct.ma_ctpdk, lt.ngay_gio_thi,
                   lt.ma_cchi, pdt.sbd, pdt.ma_phong
            FROM phieu_gia_han pgh
            JOIN chi_tiet_phieu_dk ct ON pgh.ma_ctpdk = ct.ma_ctpdk
            JOIN phieu_du_thi pdt ON ct.ma_ctpdk = pdt.ma_ctpdk
            JOIN lich_thi lt ON pdt.ma_lt = lt.ma_lt
            WHERE ct.ma_ts = ?
            ORDER BY lt.ngay_gio_thi DESC
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, maTS);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PhieuGiaHanDTO dto = new PhieuGiaHanDTO(
                        rs.getString("ma_pgh"),
                        rs.getString("loai_gh"),
                        rs.getDouble("phi_gh"),
                        rs.getString("nhan_vien_tao"),
                        rs.getBoolean("da_thanh_toan"),
                        rs.getString("ma_ctpdk")
                );

                Map<String, Object> row = new HashMap<>();
                row.put("dto", dto);
                row.put("ma_ctpdk", rs.getString("ma_ctpdk"));
                row.put("ngay_thi", rs.getTimestamp("ngay_gio_thi"));
                row.put("ma_cchi", rs.getString("ma_cchi"));
                row.put("sbd", rs.getString("sbd"));
                row.put("ma_phong", rs.getString("ma_phong"));

                dataList.add(row);
            }

            lblTieuDe.setText("Lịch sử gia hạn cho thí sinh: " + maTS);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải dữ liệu từ cơ sở dữ liệu.");
        }
    }

    @FXML
    private void handleThemGiaHan() {
        String maTS = txtMaTS.getText().trim();
        if (maTS.isEmpty()) {
            showAlert("Thông báo", "Vui lòng tra cứu mã thí sinh trước.");
            return;
        }

        try {
            // Load FXML and controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/LapPhieuGiaHan/dialog-them-gia-han.fxml"));
            Parent root = loader.load();
            DialogThemGiaHanController controller = loader.getController();

            // Lấy danh sách mã ct_pdk
            List<String> maCTPDKList;
            try {
                maCTPDKList = dataList.stream()
                        .map(row -> {
                            try {
                                return (String) row.get("ma_ctpdk");
                            } catch (Exception e) {
                                System.err.println("Lỗi lấy ma_ctpdk: " + e.getMessage());
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể lấy danh sách mã chi tiết phiếu đăng ký.");
                return;
            }

            // Lấy danh sách lịch thi
            List<LichThiDTO> danhSachLichThi;
            try {
                danhSachLichThi = LichThiDAO.findAll();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể lấy danh sách lịch thi.");
                return;
            }

            // Khởi tạo dữ liệu cho dialog
            controller.setDanhSachCTPDK(maCTPDKList);
            controller.initLichThiTable(danhSachLichThi);

            // Hiển thị dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm Gia Hạn");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();

            // Làm mới bảng sau khi thêm
            try {
                loadLichSuGiaHan(maTS);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể tải lại lịch sử gia hạn.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở giao diện thêm gia hạn.");
        }
    }



    private void xoaPhieuGiaHan(String maPGH) {
        try {
            PhieuGiaHanBUS.xoaPhieuGiaHan(maPGH);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể xóa phiếu gia hạn.");
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
