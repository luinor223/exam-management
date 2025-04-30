package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.ChiTietPhongThiDTO;
import com.hcmus.exammanagement.dto.Database;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ChiTietPhongThiDAO {

    public static boolean checkExist(String maLichThi, String maPhong) throws SQLException {
        String sql = "SELECT 1 FROM chi_tiet_phong_thi WHERE ma_lt = ? AND ma_phong = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maLichThi);
            stmt.setString(2, maPhong);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            log.error("Error finding chi tiet phong thi by lich thi ID {} and phong ID {}: {}", maLichThi, maPhong, e.getMessage());
            throw e;
        }

        return false;
    }

    public static List<ChiTietPhongThiDTO> findByLichThi(String maLichThi) throws SQLException {
        List<ChiTietPhongThiDTO> chiTietPhongThiList = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM chi_tiet_phong_thi ct JOIN phong p ON ct.ma_phong = p.ma_phong " +
                "JOIN giam_thi gt ON ct.ma_gt = gt.ma_gt " +
                "WHERE ma_lt = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maLichThi);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                chiTietPhongThiList.add(ChiTietPhongThiDTO.fromResultSet(rs));
            }

        } catch (SQLException e) {
            log.error("Error finding chi tiet phong thi by lich thi ID {}: {}", maLichThi, e.getMessage());
            throw e;
        }

        return chiTietPhongThiList;
    }

    public static int insert(ChiTietPhongThiDTO chiTietPhongThi) throws SQLException {
        String sql = "INSERT INTO chi_tiet_phong_thi (ma_lt, ma_phong, ma_gt, so_luong_hien_tai, so_luong_toi_da) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, chiTietPhongThi.getMaLichThi());
            stmt.setString(2, chiTietPhongThi.getPhongDTO().getMaPhong());
            stmt.setString(3, chiTietPhongThi.getGiamThiDTO().getMaGiamThi());
            stmt.setInt(4, chiTietPhongThi.getSoLuongHienTai());
            stmt.setInt(5, chiTietPhongThi.getSoLuongToiDa());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error inserting chi tiet phong thi for lich thi ID {} and phong ID {}: {}", 
                      chiTietPhongThi.getMaLichThi(), chiTietPhongThi.getPhongDTO().getMaPhong(), e.getMessage());
            throw e;
        }
    }

    public static int update(ChiTietPhongThiDTO chiTietPhongThi) throws SQLException {
        String sql = "UPDATE chi_tiet_phong_thi SET ma_gt = ?, so_luong_hien_tai = ?, so_luong_toi_da = ? WHERE ma_lt = ? AND ma_phong = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, chiTietPhongThi.getGiamThiDTO().getMaGiamThi());
            stmt.setInt(2, chiTietPhongThi.getSoLuongHienTai());
            stmt.setInt(3, chiTietPhongThi.getSoLuongToiDa());
            stmt.setString(4, chiTietPhongThi.getMaLichThi());
            stmt.setString(5, chiTietPhongThi.getPhongDTO().getMaPhong());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating chi tiet phong thi with lich thi ID {} and phong ID {}: {}", 
                      chiTietPhongThi.getMaLichThi(), chiTietPhongThi.getPhongDTO().getMaPhong(), e.getMessage());
            throw e;
        }
    }

    public static int delete(String maLichThi, String maPhong) throws SQLException {
        String sql = "DELETE FROM chi_tiet_phong_thi WHERE ma_lt = ? AND ma_phong = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maLichThi);
            stmt.setString(2, maPhong);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting chi tiet phong thi with lich thi ID {} and phong ID {}: {}", 
                      maLichThi, maPhong, e.getMessage());
            throw e;
        }
    }
}
