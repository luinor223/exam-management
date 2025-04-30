package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PhieuDangKyDAO {
    public List<PhieuDangKyDTO> findAll() throws SQLException {
        List<PhieuDangKyDTO> listPDK = new ArrayList<>();
        String sql = "SELECT * FROM phieu_dang_ky";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                listPDK.add(PhieuDangKyDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all phieu dang ky: {}", e.getMessage());
            throw e;
        }

        return listPDK;
    }

    public PhieuDangKyDTO findById(String maPhieuDangKy) throws SQLException {
        String sql = "SELECT * FROM phieu_dang_ky WHERE ma_pdk = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhieuDangKy);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return PhieuDangKyDTO.fromResultSet(rs);
            }

        } catch (SQLException e) {
            log.error("Error finding phieu dang ky by ID {}: {}", maPhieuDangKy, e.getMessage());
            throw e;
        }

        return null;
    }

    public int insert(PhieuDangKyDTO phieuDangKy) throws SQLException {
        String sql = "INSERT INTO phieu_dang_ky (trang_thai, dia_chi_giao, ma_kh, nhan_vien_tao) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, phieuDangKy.getTrangThai());
                stmt.setString(2, phieuDangKy.getDiaChiGiao());
                stmt.setString(3, phieuDangKy.getKhachHang().getMaKH());
                stmt.setString(4, phieuDangKy.getMaNVTao());

                return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error inserting phieu dang ky: {}", e.getMessage());
            throw e;
        }
    }

    public List<PhieuDangKyDTO> findAllChoThanhToan() throws SQLException {
        List<PhieuDangKyDTO> listPDK = new ArrayList<>();
        String sql = """
            SELECT pdk.*, kh.*
            FROM phieu_dang_ky pdk
                JOIN khach_hang kh ON pdk.ma_kh = kh.ma_kh
            WHERE pdk.trang_thai = 'Chờ xử lý';
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                listPDK.add(PhieuDangKyDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all phieu dang ky cho thanh toan: {}", e.getMessage());
            throw e;
        }

        return listPDK;
    }

    public void capNhatTrangThai(String maPhieu, String trangThai) {
        String sql = "UPDATE phieu_dang_ky SET trang_thai = ? WHERE ma_pdk = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            stmt.setString(2, maPhieu);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean huyPhieuDangKy(String maPhieuDangKy) {
        String sql = "UPDATE phieu_dang_ky SET trang_thai = 'Đã hủy' WHERE ma_pdk = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPhieuDangKy);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
