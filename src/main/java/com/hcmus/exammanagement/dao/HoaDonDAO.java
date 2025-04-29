package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.HoaDonDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class HoaDonDAO {

    public List<HoaDonDTO> findAll() {
        List<HoaDonDTO> listHoaDon = new ArrayList<>();
        String sql = "SELECT hd.*, pd.*, kh.* " +
                    "FROM hoa_don hd " +
                    "JOIN phieu_dang_ky pd ON hd.ma_pdk = pd.ma_pdk " +
                    "JOIN khach_hang kh ON pd.ma_kh = kh.ma_kh";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                listHoaDon.add(HoaDonDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listHoaDon;
    }

    public HoaDonDTO findById(String maHoaDon) {
        String sql = "SELECT hd.*, pd.*, kh.* " +
                "FROM hoa_don hd " +
                "JOIN phieu_dang_ky pd ON hd.ma_pdk = pd.ma_pdk " +
                "JOIN khach_hang kh ON pd.ma_kh = kh.ma_kh " +
                "WHERE hd.ma_hd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return HoaDonDTO.fromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void insert(HoaDonDTO hoaDon) {
        String sql = "INSERT INTO hoa_don (ma_hd, tong_tien, pt_thanh_toan, so_tien_giam, ngay_lap, nhan_vien_tao, ma_pdk, ma_thanh_toan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hoaDon.getMaHd());
            stmt.setDouble(2, hoaDon.getTongTien());
            stmt.setString(3, hoaDon.getPtThanhToan());
            stmt.setDouble(4, hoaDon.getSoTienGiam());
            stmt.setString(5, hoaDon.getNgayLap());
            stmt.setString(6, hoaDon.getNhanVienTao());
            stmt.setString(7, hoaDon.getMaTt());
            stmt.setString(8, hoaDon.getPhieuDangKy().getMaPhieuDangKy());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(HoaDonDTO hoaDon) {
        String sql = "UPDATE hoa_don SET tong_tien = ?, pt_thanh_toan = ?, so_tien_giam = ?, ngay_lap = ?, nhan_vien_tao = ?, ma_pdk = ?, ma_thanh_toan = ? WHERE ma_hd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, hoaDon.getTongTien());
            stmt.setString(2, hoaDon.getPtThanhToan());
            stmt.setDouble(3, hoaDon.getSoTienGiam());
            stmt.setString(4, hoaDon.getNgayLap());
            stmt.setString(5, hoaDon.getNhanVienTao());
            stmt.setString(6, hoaDon.getPhieuDangKy().getMaPhieuDangKy());
            stmt.setString(7, hoaDon.getMaTt());
            stmt.setString(8, hoaDon.getMaHd());


            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update_matt(String maHd, String maThanhToan) {
        String sql = "UPDATE hoa_don SET ma_thanh_toan = ? WHERE ma_hd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maThanhToan);
            stmt.setString(2, maHd);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete(String maHoaDon) {
        String sql = "DELETE FROM hoa_don WHERE ma_hd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}