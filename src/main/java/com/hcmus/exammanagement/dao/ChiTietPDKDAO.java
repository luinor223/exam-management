package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.ChiTietPDKDTO;
import com.hcmus.exammanagement.dto.Database;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ChiTietPDKDAO {

    public static List<ChiTietPDKDTO> findAll() throws SQLException {
        List<ChiTietPDKDTO> chiTietPDKList = new ArrayList<>();
        String sql = "SELECT * FROM chi_tiet_phieu_dk";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                chiTietPDKList.add(ChiTietPDKDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all chi tiet phieu dang ky: {}", e.getMessage());
            throw e;
        }

        return chiTietPDKList;
    }

    public static ChiTietPDKDTO findById(String maCTPDK) throws SQLException {
        String sql = "SELECT * FROM chi_tiet_phieu_dk WHERE ma_ctpdk = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maCTPDK);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return ChiTietPDKDTO.fromResultSet(rs);
            }

        } catch (SQLException e) {
            log.error("Error finding chi tiet phieu dang ky by ID {}: {}", maCTPDK, e.getMessage());
            throw e;
        }

        return null;
    }

    public static List<ChiTietPDKDTO> findByPDK(String maPDK) throws SQLException {
        List<ChiTietPDKDTO> chiTietPDKList = new ArrayList<>();
        String sql = "SELECT * FROM chi_tiet_phieu_dk WHERE ma_pdk = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPDK);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                chiTietPDKList.add(ChiTietPDKDTO.fromResultSet(rs));
            }

        } catch (SQLException e) {
            log.error("Error finding chi tiet phieu dang ky by phieu dang ky ID {}: {}", maPDK, e.getMessage());
            throw e;
        }

        return chiTietPDKList;
    }

    public static List<ChiTietPDKDTO> findByThiSinh(String maThiSinh) throws SQLException {
        List<ChiTietPDKDTO> chiTietPDKList = new ArrayList<>();
        String sql = "SELECT * FROM chi_tiet_phieu_dk WHERE ma_ts = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maThiSinh);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                chiTietPDKList.add(ChiTietPDKDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding chi tiet phieu dang ky by thi sinh ID {}: {}", maThiSinh, e.getMessage());
            throw e;
        }

        return chiTietPDKList;
    }

    public static List<ChiTietPDKDTO> findByLichThi(String maLichThi) throws SQLException {
        List<ChiTietPDKDTO> chiTietPDKList = new ArrayList<>();
        String sql = "SELECT * FROM chi_tiet_phieu_dk WHERE ma_lt = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maLichThi);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                chiTietPDKList.add(ChiTietPDKDTO.fromResultSet(rs));
            }

        } catch (SQLException e) {
            log.error("Error finding chi tiet phieu dang ky by lich thi ID {}: {}", maLichThi, e.getMessage());
            throw e;
        }

        return chiTietPDKList;
    }

    public static int insert(ChiTietPDKDTO chiTietPDK) throws SQLException {
        String sql = "INSERT INTO chi_tiet_phieu_dk (ma_pdk, ma_ts, ma_lt) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, chiTietPDK.getMaPDK());
            stmt.setString(2, chiTietPDK.getMaThiSinh());
            stmt.setString(3, chiTietPDK.getMaLichThi());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error inserting chi tiet phieu dang ky: {}", e.getMessage());
            throw e;
        }
    }

    public static int update(ChiTietPDKDTO chiTietPDK) throws SQLException {
        String sql = "UPDATE chi_tiet_phieu_dk SET ma_pdk = ?, ma_ts = ?, ma_lt = ? WHERE ma_ctpdk = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, chiTietPDK.getMaPDK());
            stmt.setString(2, chiTietPDK.getMaThiSinh());
            stmt.setString(3, chiTietPDK.getMaLichThi());
            stmt.setString(4, chiTietPDK.getMaCTPDK());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating chi tiet phieu dang ky with ID {}: {}", chiTietPDK.getMaCTPDK(), e.getMessage());
            throw e;
        }
    }

    public static int delete(String maCTPDK) throws SQLException {
        String sql = "DELETE FROM chi_tiet_phieu_dk WHERE ma_ctpdk = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maCTPDK);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting chi tiet phieu dang ky with ID {}: {}", maCTPDK, e.getMessage());
            throw e;
        }
    }
}
