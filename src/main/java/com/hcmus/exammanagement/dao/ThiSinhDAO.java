package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.ThiSinhDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ThiSinhDAO {

    public static List<ThiSinhDTO> findAll() throws SQLException {
        List<ThiSinhDTO> thiSinhList = new ArrayList<>();
        String sql = "SELECT * FROM thi_sinh";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                thiSinhList.add(ThiSinhDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all thi sinh: {}", e.getMessage());
            throw e;
        }

        return thiSinhList;
    }

    public static List<ThiSinhDTO> findByPDK(String maPDK) throws SQLException {
        List<ThiSinhDTO> thiSinhList = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM thi_sinh ts JOIN chi_tiet_phieu_dk ct ON ts.ma_ts = ct.ma_ts " +
                "WHERE ct.ma_pdk = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPDK);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                thiSinhList.add(ThiSinhDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding thi sinh by ma_pdk {}: {}", maPDK, e.getMessage());
            throw e;
        }

        return thiSinhList;
    }

    public static ThiSinhDTO findById(String maThiSinh) throws SQLException {
        String sql = "SELECT * FROM thi_sinh WHERE ma_ts = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maThiSinh);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return ThiSinhDTO.fromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            log.error("Error finding thi sinh by ID {}: {}", maThiSinh, e.getMessage());
            throw e;
        }

        return null;
    }

    public static ThiSinhDTO findByCCCD(String cccd) throws SQLException {
        String sql = "SELECT * FROM thi_sinh WHERE cccd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cccd);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return ThiSinhDTO.fromResultSet(rs);
            }

        } catch (SQLException e) {
            log.error("Error finding thi sinh by CCCD {}: {}", cccd, e.getMessage());
            throw e;
        }

        return null;
    }

    public static ThiSinhDTO insert(ThiSinhDTO thiSinh) throws SQLException {
        String sql = "INSERT INTO thi_sinh (ho_ten, cccd, ngay_sinh, gioi_tinh) VALUES (?, ?, ?, ?) RETURNING ma_ts";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, thiSinh.getHoTen());
            stmt.setString(2, thiSinh.getCccd());
            stmt.setDate(3, new java.sql.Date(thiSinh.getNgaySinh().getTime()));
            stmt.setString(4, thiSinh.getGioiTinh());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                thiSinh.setMaThiSinh(rs.getString("ma_ts"));
                return thiSinh;
            } else {
                throw new SQLException("Failed to insert thi sinh, no ID obtained.");
            }
        } catch (SQLException e) {
            log.error("Error inserting thi sinh: {}", e.getMessage());
            throw e;
        }
    }
}
