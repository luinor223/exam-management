package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PhieuDangKyDAO {
    public List<PhieuDangKyDTO> findAll() {
        List<PhieuDangKyDTO> listPDK = new ArrayList<>();
        String sql = "SELECT * FROM phieu_dang_ky";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                listPDK.add(PhieuDangKyDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return listPDK;
    }

    public PhieuDangKyDTO findById(String maPhieuDangKy) {
        String sql = "SELECT * FROM phieu_dang_ky WHERE ma_pdk = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPhieuDangKy);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return PhieuDangKyDTO.fromResultSet(rs);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    public void insert(PhieuDangKyDTO phieuDangKy) {
        String sql = "INSERT INTO phieu_dang_ky (trang_thai, dia_chi_giao, ma_kh, nhan_vien_tao) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phieuDangKy.getTrangThai());
            stmt.setString(2, phieuDangKy.getDiaChiGiao());
            stmt.setString(3, phieuDangKy.getMaKH());
            stmt.setString(4, phieuDangKy.getMaNVTao());

            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
