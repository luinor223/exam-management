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

    public static List<HoaDonDTO> findAll() {
        List<HoaDonDTO> listHoaDon = new ArrayList<>();
        String sql = "SELECT hd.*, pd.*, kh.* " +
                    "FROM hoa_don hd " +
                    "JOIN phieu_dang_ky pd ON hd.ma_pdk = pd.ma_pdk " +
                    "JOIN khach_hang kh ON pd.ma_kh = kh.ma_kh";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("Query SQL = " + sql);
            while (rs.next()) {
                listHoaDon.add(HoaDonDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listHoaDon;
    }

    public static HoaDonDTO findById(String maHoaDon) {
        String sql = "SELECT hd.*, pd.*, kh.* " +
                "FROM hoa_don hd " +
                "JOIN phieu_dang_ky pd ON hd.ma_pdk = pd.ma_pdk " +
                "JOIN khach_hang kh ON pd.ma_kh = kh.ma_kh " +
                "WHERE hd.ma_hd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            System.out.println("Query SQL = " + sql);
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

    public static void insert(HoaDonDTO hoaDon) {
        String sql = "INSERT INTO hoa_don ( tong_tien, pt_thanh_toan, so_tien_giam, ngay_lap, ma_pdk, ma_thanh_toan) " +
                "VALUES ( ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("Query SQL = " + sql);
            int cnt = 1;
            stmt.setDouble(cnt++, hoaDon.getTongTien());
            stmt.setString(cnt++, hoaDon.getPtThanhToan());
            stmt.setDouble(cnt++, hoaDon.getSoTienGiam());
            stmt.setDate(cnt++, hoaDon.getNgayLap());
//            stmt.setString(cnt++, hoaDon.getNhanVienTao());
            stmt.setString(cnt++, hoaDon.getPhieuDangKy().getMaPhieuDangKy());
            stmt.setString(cnt, hoaDon.getMaTt());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void update(HoaDonDTO hoaDon) {
        String sql = "UPDATE hoa_don SET tong_tien = ?, pt_thanh_toan = ?, so_tien_giam = ?, ngay_lap = ?, ma_pdk = ?, ma_thanh_toan = ? WHERE ma_hd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("Query SQL = " + sql);
            int cnt = 1;
            stmt.setDouble(cnt++, hoaDon.getTongTien());
            stmt.setString(cnt++, hoaDon.getPtThanhToan());
            stmt.setDouble(cnt++, hoaDon.getSoTienGiam());
            stmt.setDate(cnt++, hoaDon.getNgayLap());
//            stmt.setString(cnt++, hoaDon.getNhanVienTao());
            stmt.setString(cnt++, hoaDon.getPhieuDangKy().getMaPhieuDangKy());
            stmt.setString(cnt++, hoaDon.getMaTt());
            stmt.setString(cnt, hoaDon.getMaHd());


            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean update_matt(String maHd, String maThanhToan) {
        String sql = "UPDATE hoa_don SET ma_thanh_toan = ? WHERE ma_hd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("Query SQL = " + sql);
            stmt.setString(1, maThanhToan);
            stmt.setString(2, maHd);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean delete(String maHoaDon) {
        String sql = "DELETE FROM hoa_don WHERE ma_hd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("Query SQL = " + sql);
            stmt.setString(1, maHoaDon);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}