package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.LichThiDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LichThiDAO {

    public static List<LichThiDTO> findAll() throws SQLException {
        List<LichThiDTO> lichThiList = new ArrayList<>();
        String sql = "SELECT lt.*, cc.*, SUM(ct.so_luong_hien_tai) AS so_luong_hien_tai, SUM(ct.so_luong_toi_da) AS so_luong_toi_da FROM lich_thi lt " +
                     "JOIN chung_chi cc ON lt.ma_cchi = cc.ma_cchi " +
                "LEFT JOIN chi_tiet_phong_thi ct ON lt.ma_lt = ct.ma_lt " +
                "GROUP BY (lt.ma_lt, lt.ma_cchi, lt.ngay_gio_thi, lt.thoi_luong_thi, cc.ma_cchi, cc.le_phi, cc.ten_chung_chi, cc.thoi_gian_hieu_luc, cc.mo_ta)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lichThiList.add(LichThiDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all lich thi: {}", e.getMessage());
            throw e;
        }

        return lichThiList;
    }

    public static List<LichThiDTO> findAllNew() throws SQLException {
        List<LichThiDTO> lichThiList = new ArrayList<>();
        String sql = "SELECT lt.*, cc.*, SUM(ct.so_luong_hien_tai) AS so_luong_hien_tai, SUM(ct.so_luong_toi_da) AS so_luong_toi_da FROM lich_thi lt " +
                "JOIN chung_chi cc ON lt.ma_cchi = cc.ma_cchi " +
                "LEFT JOIN chi_tiet_phong_thi ct ON lt.ma_lt = ct.ma_lt " +
                "WHERE lt.ngay_gio_thi > NOW() " +
                "GROUP BY (lt.ma_lt, lt.ma_cchi, lt.ngay_gio_thi, lt.thoi_luong_thi, cc.ma_cchi, cc.le_phi, cc.ten_chung_chi, cc.thoi_gian_hieu_luc, cc.mo_ta)" +
                " ORDER BY lt.ngay_gio_thi DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lichThiList.add(LichThiDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all new lich thi: {}", e.getMessage());
            throw e;
        }

        return lichThiList;
    }

    public static LichThiDTO findById(String maLichThi) throws SQLException {
        String sql = "SELECT lt.*, cc.*, SUM(ct.so_luong_hien_tai) AS so_luong_hien_tai, SUM(ct.so_luong_toi_da) AS so_luong_toi_da FROM lich_thi lt " +
                "JOIN chung_chi cc ON lt.ma_cchi = cc.ma_cchi " +
                "LEFT JOIN chi_tiet_phong_thi ct ON lt.ma_lt = ct.ma_lt " +
                "WHERE lt.ma_lt = ? " +
                "GROUP BY (lt.ma_lt, lt.ma_cchi, lt.ngay_gio_thi, lt.thoi_luong_thi, cc.ma_cchi, cc.le_phi, cc.ten_chung_chi, cc.thoi_gian_hieu_luc, cc.mo_ta)";


        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maLichThi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return LichThiDTO.fromResultSet(rs);
            }

        } catch (SQLException e) {
            log.error("Error finding lich thi by ID {}: {}", maLichThi, e.getMessage());
            throw e;
        }

        return null;
    }

    public static int insert(LichThiDTO lichThi) throws SQLException {
        String sql = "INSERT INTO lich_thi (ngay_gio_thi, thoi_luong_thi, ma_cchi) " +
                     "VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, lichThi.getNgayGioThi());
            stmt.setInt(2, lichThi.getThoiLuongThi());
            stmt.setString(3, lichThi.getChungChi().getMaChungChi());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error inserting lich thi: {}", e.getMessage());
            throw e;
        }
    }

    public static int update(LichThiDTO lichThi) throws SQLException {
        String sql = "UPDATE lich_thi SET ngay_gio_thi = ?, thoi_luong_thi = ?, " +
                     "ma_cchi = ? WHERE ma_lt = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, lichThi.getNgayGioThi());
            stmt.setInt(2, lichThi.getThoiLuongThi());
            stmt.setString(3, lichThi.getChungChi().getMaChungChi());
            stmt.setString(4, lichThi.getMaLichThi());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating lich thi with ID {}: {}", lichThi.getMaLichThi(), e.getMessage());
            throw e;
        }
    }

    public static int delete(String maLichThi) throws SQLException {
        String sql = "DELETE FROM lich_thi WHERE ma_lt = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maLichThi);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting lich thi with ID {}: {}", maLichThi, e.getMessage());
            throw e;
        }
    }
}
