package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.KhachHangDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class KhachHangDAO {
    public static List<KhachHangDTO> findAll() throws SQLException {
        List<KhachHangDTO> khList = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                khList.add(KhachHangDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all khach hang: {}", e.getMessage());
            throw e;
        }

        return khList;
    }

    public static KhachHangDTO findByCCCD(String cccd) throws SQLException {
        String sql = "SELECT * FROM khach_hang WHERE cccd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cccd);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return KhachHangDTO.fromResultSet(rs);
            }
        } catch (SQLException e) {
            log.error("Error finding khach hang by CCCD {}: {}", cccd, e.getMessage());
            throw e;
        }

        return null;
    }

    public static List<KhachHangDTO> findByLoaiKh(String loaiKh) throws SQLException {
        List<KhachHangDTO> khList = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang WHERE loai_kh = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, loaiKh);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                khList.add(KhachHangDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all khach hang by loai kh: {}", e.getMessage());
            throw e;
        }

        return khList;
    }

    public static KhachHangDTO findById(String maKH) throws SQLException {
        String sql = "SELECT * FROM khach_hang WHERE ma_kh = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return KhachHangDTO.fromResultSet(rs);
            }
        } catch (SQLException e) {
            log.error("Error finding khach hang by ID {}: {}", maKH, e.getMessage());
            throw e;
        }

        return null;
    }

    public static KhachHangDTO insert(KhachHangDTO khachHang) throws SQLException {
        String sql = "INSERT INTO khach_hang (ho_ten, cccd, email, sdt, dia_chi, loai_kh) VALUES (?, ?, ?, ?, ?, ?) RETURNING ma_kh";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, khachHang.getHoTen());
            stmt.setString(2, khachHang.getCccd());
            stmt.setString(3, khachHang.getEmail());
            stmt.setString(4, khachHang.getSdt());
            stmt.setString(5, khachHang.getDiaChi());
            stmt.setString(6, khachHang.getLoaiKh());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String maKH = rs.getString("ma_kh");
                khachHang.setMaKH(maKH);
                return khachHang;
            } else {
                throw new SQLException("Failed to insert khach hang, no ID obtained.");
            }
        } catch (SQLException e) {
            log.error("Error inserting khach hang: {}", e.getMessage());
            throw e;
        }
    }
}
