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
    public List<KhachHangDTO> findAll() {
        List<KhachHangDTO> khList = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                khList.add(KhachHangDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return khList;
    }

    public List<KhachHangDTO> findByLoaiKh(String loaiKh) {
        List<KhachHangDTO> khList = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang WHERE loai_kh = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, loaiKh);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("loai_kh"));
                khList.add(KhachHangDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        System.out.println(sql + loaiKh);
        System.out.println(khList);
        return khList;
    }

    public KhachHangDTO findById(String maKH) {
        String sql = "SELECT * FROM khach_hang WHERE ma_kh = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return KhachHangDTO.fromResultSet(rs);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    public void insert(KhachHangDTO khachHang) {
        String sql = "INSERT INTO khach_hang (ho_ten, cccd, email, sdt, dia_chi, loai_kh) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, khachHang.getHoTen());
            stmt.setString(2, khachHang.getCccd());
            stmt.setString(3, khachHang.getEmail());
            stmt.setString(4, khachHang.getSdt());
            stmt.setString(5, khachHang.getDiaChi());
            stmt.setString(6, khachHang.getLoai_kh());

            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
