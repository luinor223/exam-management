package com.hcmus.exammanagement.controller;

import com.hcmus.exammanagement.dao.PhieuGiaHanDAO;
import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.PhieuGiaHanDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.sql.*;

public class LapPhieuGiaHanController {

    @FXML
    private ComboBox<String> cbMaCTPDK;

    @FXML
    private TextField txtLoaiGH;

    @FXML
    private TextField txtPhiGH;

    @FXML
    private CheckBox chkDaThanhToan;

    @FXML
    private Label lblThongBao;

    @FXML private TextField txtMaThiSinh;
    @FXML private VBox vboxLapPhieu;

    @FXML
    public void initialize() {
        vboxLapPhieu.setVisible(false);

        lblThongBao.setText("");
        lblThongBao.setStyle("-fx-text-fill: red;");
    }

    private void loadMaCTPDKTheoMaPhieu(String maPhieu) {
        ObservableList<String> list = FXCollections.observableArrayList();
        String query = "SELECT ma_ctpdk FROM chi_tiet_phieu_dk WHERE ma_pdk = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maPhieu);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("ma_ctpdk"));
            }
            cbMaCTPDK.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleLuuPhieu() {
        String maCTPDK = cbMaCTPDK.getValue();
        String loaiGH = txtLoaiGH.getText();
        String phiGHStr = txtPhiGH.getText();
        boolean daThanhToan = chkDaThanhToan.isSelected();

        if (maCTPDK == null || loaiGH.isEmpty() || phiGHStr.isEmpty()) {
            lblThongBao.setText("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try {
            double phiGH = Double.parseDouble(phiGHStr);
            // Giả định nhân viên tạo đang đăng nhập là mã "NV001"
            PhieuGiaHanDTO dto = new PhieuGiaHanDTO(null, loaiGH, phiGH, null, daThanhToan, maCTPDK);
            boolean success = PhieuGiaHanDAO.insertPhieuGiaHan(dto);
            if (success) {
                lblThongBao.setStyle("-fx-text-fill: green;");
                lblThongBao.setText("Lưu phiếu thành công!");
                txtLoaiGH.clear();
                txtPhiGH.clear();
                chkDaThanhToan.setSelected(false);
            } else {
                lblThongBao.setStyle("-fx-text-fill: red;");
                lblThongBao.setText("Lưu phiếu thất bại!");
            }
        } catch (NumberFormatException e) {
            lblThongBao.setText("Phí gia hạn phải là số!");
        }
    }

    @FXML
    private void handleTraCuu() {
        String maThiSinh = txtMaThiSinh.getText();
        if (maThiSinh.isEmpty()) {
            lblThongBao.setText("Vui lòng nhập mã thí sinh!");
            return;
        }

        try (Connection conn = Database.getConnection()) {
            // Truy vấn phiếu dự thi
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM phieu_du_thi p " +
                            "JOIN chi_tiet_phieu_dk c ON p.ma_ctpdk = c.ma_ctpdk " +
                            "JOIN lich_thi l ON c.ma_lt = l.ma_lt " +
                            "WHERE c.ma_ts = ?");
            stmt.setString(1, maThiSinh);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                lblThongBao.setText("Không tìm thấy phiếu dự thi!");
                vboxLapPhieu.setVisible(false);
                return;
            }

            String maPhieu = rs.getString("ma_pdk");

            // Lấy lịch thi và lịch sử gia hạn
            StringBuilder info = new StringBuilder("Thông tin phiếu dự thi:\n");
            info.append("Mã phiếu: ").append(maPhieu).append("\n");
            info.append("Ngày thi: ").append(rs.getDate("ngay_gio_thi")).append("\n");

            info.append("\nLịch sử gia hạn:\n");
            PreparedStatement stmtGH = conn.prepareStatement(
                    "SELECT * FROM phieu_gia_han WHERE ma_ctpdk IN " +
                            "(SELECT ma_ctpdk FROM chi_tiet_phieu_dk WHERE ma_pdk = ?)");
            stmtGH.setString(1, maPhieu);
            ResultSet rsGH = stmtGH.executeQuery();
            while (rsGH.next()) {
                info.append("- ").append(rsGH.getString("loai_gh"))
                        .append(", phí: ").append(rsGH.getDouble("phi_gh"))
                        .append(", thanh toán: ").append(rsGH.getBoolean("da_thanh_toan") ? "rồi" : "chưa")
                        .append("\n");
            }

            // Hiển thị dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông tin dự thi");
            alert.setHeaderText("Kết quả tra cứu cho thí sinh " + maThiSinh);
            alert.setContentText(info.toString());
            alert.showAndWait();

            // Cho phép hiện phần lập phiếu
            loadMaCTPDKTheoMaPhieu(maPhieu);
            vboxLapPhieu.setVisible(true);
            lblThongBao.setText("");

        } catch (SQLException e) {
            e.printStackTrace();
            lblThongBao.setText("Lỗi khi truy vấn dữ liệu!");
        }
    }

}