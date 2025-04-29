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

    public List<ThiSinhDTO> findAll() {
        List<ThiSinhDTO> thiSinhList = new ArrayList<>();
        String sql = "SELECT * FROM thi_sinh";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                thiSinhList.add(ThiSinhDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return thiSinhList;
    }

    public ThiSinhDTO findById(String maThiSinh) {
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
            log.error(e.getMessage());
        }

        return null;
    }

    public List<ThiSinhDTO> findByPhieuDangKy(String maPhieuDangKy) {
        List<ThiSinhDTO> thiSinhList = new ArrayList<>();
        String sql = "SELECT * FROM thi_sinh WHERE ma_ts = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPhieuDangKy);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    thiSinhList.add(ThiSinhDTO.fromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return thiSinhList;
    }

    public boolean insert(ThiSinhDTO thiSinh) {
        String sql = "INSERT INTO thi_sinh (ho_ten, cccd, ngay_sinh, gioi_tinh) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, thiSinh.getHoTen());
            stmt.setString(2, thiSinh.getCccd());
            stmt.setDate(3, new java.sql.Date(thiSinh.getNgaySinh().getTime()));
            stmt.setString(4, thiSinh.getGioiTinh());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean update(ThiSinhDTO thiSinh) {
        String sql = "UPDATE thi_sinh SET ho_ten = ?, cccd = ?, ngay_sinh = ?, gioi_tinh = ? WHERE ma_ts = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, thiSinh.getHoTen());
            stmt.setString(2, thiSinh.getCccd());
            stmt.setDate(3, new java.sql.Date(thiSinh.getNgaySinh().getTime()));
            stmt.setString(4, thiSinh.getGioiTinh());
            stmt.setString(5, thiSinh.getMaThiSinh());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean delete(String maThiSinh) {
        String sql = "DELETE FROM thi_sinh WHERE ma_ts = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maThiSinh);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}