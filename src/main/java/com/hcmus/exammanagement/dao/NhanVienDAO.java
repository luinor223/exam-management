package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.NhanVienDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    public static List<NhanVienDTO> getAllNhanVien() {
        List<NhanVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM nhan_vien";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO(
                        rs.getString("ma_nv"),
                        rs.getString("ho_ten"),
                        rs.getString("cccd"),
                        rs.getString("loai_nv"),
                        rs.getString("trang_thai")
                );
                list.add(nv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static NhanVienDTO getNhanVienByMa(String maNV) {
        String sql = "SELECT * FROM nhan_vien WHERE ma_nv = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNV);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new NhanVienDTO(
                        rs.getString("ma_nv"),
                        rs.getString("ho_ten"),
                        rs.getString("cccd"),
                        rs.getString("loai_nv"),
                        rs.getString("trang_thai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertNhanVien(NhanVienDTO nv) {
        String sql = "INSERT INTO nhan_vien (ho_ten, cccd, loai_nv, trang_thai) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nv.getHoTen());
            pstmt.setString(2, nv.getCccd());
            pstmt.setString(3, nv.getLoaiNV());
            pstmt.setString(4, nv.getTrangThai());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateNhanVien(NhanVienDTO nv) {
        String sql = "UPDATE nhan_vien SET ho_ten = ?, cccd = ?, loai_nv = ?, trang_thai = ? WHERE ma_nv = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nv.getHoTen());
            pstmt.setString(2, nv.getCccd());
            pstmt.setString(3, nv.getLoaiNV());
            pstmt.setString(4, nv.getTrangThai());
            pstmt.setString(5, nv.getMaNV());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteNhanVien(String maNV) {
        String sql = "DELETE FROM nhan_vien WHERE ma_nv = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNV);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getLoaiNV(String maNV) {
        String sql = "SELECT loai_nv FROM nhan_vien WHERE ma_nv = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNV);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("loai_nv");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
