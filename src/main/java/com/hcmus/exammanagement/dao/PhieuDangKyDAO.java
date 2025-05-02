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
    public static List<PhieuDangKyDTO> findAll() throws SQLException {
        List<PhieuDangKyDTO> listPDK = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM phieu_dang_ky pdk JOIN khach_hang kh ON pdk.ma_kh = kh.ma_kh";

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

    public static List<PhieuDangKyDTO> findByTrangThai(String trangThai) throws SQLException {
        List<PhieuDangKyDTO> listPDK = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM phieu_dang_ky pdk JOIN khach_hang kh ON pdk.ma_kh = kh.ma_kh " +
                "WHERE trang_thai = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listPDK.add(PhieuDangKyDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding phieu dang ky by trang thai {}: {}", trangThai, e.getMessage());
            throw e;
        }

        return listPDK;
    }

    public static PhieuDangKyDTO findById(String maPhieuDangKy) throws SQLException {
        String sql = "SELECT * " +
                "FROM phieu_dang_ky pdk JOIN khach_hang kh ON pdk.ma_kh = kh.ma_kh " +
                "WHERE ma_pdk = ?";

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

    public static PhieuDangKyDTO insert(PhieuDangKyDTO phieuDangKy) throws SQLException {
        String sql = "INSERT INTO phieu_dang_ky (trang_thai, dia_chi_giao, ma_kh) VALUES (?, ?, ?) RETURNING ma_pdk";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phieuDangKy.getTrangThai());
            stmt.setString(2, phieuDangKy.getDiaChiGiao());
            stmt.setString(3, phieuDangKy.getKhachHang().getMaKH());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String maPhieuDangKy = rs.getString("ma_pdk");
                phieuDangKy.setMaPhieuDangKy(maPhieuDangKy);
                return phieuDangKy;
            } else {
                throw new SQLException("Creating chi tiet phieu dang ky failed, no ID obtained.");
            }
        } catch (SQLException e) {
            log.error("Error inserting phieu dang ky: {}", e.getMessage());
            throw e;
        }
    }

    public static List<PhieuDangKyDTO> findAllByTinhTrang(String trangThai) throws SQLException {
        List<PhieuDangKyDTO> listPDK = new ArrayList<>();
        String sql = """
        SELECT pdk.*, kh.*
        FROM phieu_dang_ky pdk
            JOIN khach_hang kh ON pdk.ma_kh = kh.ma_kh
        WHERE pdk.trang_thai = ?;
    """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trangThai);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    listPDK.add(PhieuDangKyDTO.fromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            log.error("Error finding phieu dang ky by trang thai '{}': {}", trangThai, e.getMessage());
            throw e;
        }

        return listPDK;
    }

    public static void capNhatTrangThai(String maPhieu, String trangThai) {
        String sql = "UPDATE phieu_dang_ky SET trang_thai = ? WHERE ma_pdk = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            stmt.setString(2, maPhieu);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating phieu dang ky with ID {}: {}", maPhieu, e.getMessage());
        }
    }

    public boolean huyPhieuDangKy(String maPhieuDangKy) {
        String deleteChiTietSQL = "DELETE FROM chi_tiet_phieu_dk WHERE ma_pdk = ?";
        String updatePhieuSQL = "UPDATE phieu_dang_ky SET trang_thai = 'Đã hủy' WHERE ma_pdk = ?";


        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteChiTietSQL);
                    PreparedStatement updateStmt = conn.prepareStatement(updatePhieuSQL)
            ) {
                deleteStmt.setString(1, maPhieuDangKy);
                deleteStmt.executeUpdate();

                updateStmt.setString(1, maPhieuDangKy);
                int affectedRows = updateStmt.executeUpdate();

                System.out.println(updatePhieuSQL);
                System.out.println(deleteChiTietSQL);

                conn.commit();
                return affectedRows > 0;
            } catch (SQLException e) {
                conn.rollback();
                log.error("Error deleting phieu dang ky with ID {}: {}", maPhieuDangKy, e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            log.error("Error connecting to database: {}", e.getMessage());
            return false;
        }
    }
}
