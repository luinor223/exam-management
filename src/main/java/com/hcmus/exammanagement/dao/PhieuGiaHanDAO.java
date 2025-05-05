package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.PhieuGiaHanDTO;

import java.sql.*;
import java.util.*;

public class PhieuGiaHanDAO {
    public static boolean insertPhieuGiaHan(PhieuGiaHanDTO pgh) {
        String query = "INSERT INTO phieu_gia_han (loai_gh, phi_gh, da_thanh_toan, ma_ctpdk) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, pgh.getLoaiGH());
            ps.setDouble(2, pgh.getPhiGH());
            ps.setBoolean(3, pgh.isDaThanhToan());
            ps.setString(4, pgh.getMaCTPDK());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<PhieuGiaHanDTO> getAllPhieuGiaHan() {
        List<PhieuGiaHanDTO> list = new ArrayList<>();
        String query = "SELECT * FROM phieu_gia_han";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new PhieuGiaHanDTO(
                        rs.getString("ma_pgh"),
                        rs.getString("loai_gh"),
                        rs.getDouble("phi_gh"),
                        rs.getString("nhan_vien_tao"),
                        rs.getBoolean("da_thanh_toan"),
                        rs.getString("ma_ctpdk")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean deletePhieuGiaHan(String maPGH) {
        String query = "DELETE FROM phieu_gia_han WHERE ma_pgh = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maPGH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateThanhToan(String maPGH, boolean daThanhToan) {
        String query = "UPDATE phieu_gia_han SET da_thanh_toan = ? WHERE ma_pgh = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setBoolean(1, daThanhToan);
            ps.setString(2, maPGH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
