package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhieuDangKyDAO {
    public List<PhieuDangKyDTO> findAll() throws SQLException {
        List<PhieuDangKyDTO> listPDK = new ArrayList<>();
        String sql = """
            SELECT pdk.*, kh.*
            FROM phieu_dang_ky pdk
            JOIN khach_hang kh ON pdk.ma_kh = kh.ma_kh
        """;

        Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            listPDK.add(PhieuDangKyDTO.fromResultSet(rs));
        }

        return listPDK;
    }

    public PhieuDangKyDTO findById(String maPhieuDangKy) throws SQLException {
        String sql = """
            SELECT pdk.*, kh.*
            FROM phieu_dang_ky pdk
            JOIN khach_hang kh ON pdk.ma_kh = kh.ma_kh
            WHERE pdk.ma_pdk = ?
        """;

        Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, maPhieuDangKy);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return PhieuDangKyDTO.fromResultSet(rs);
        }

        return null;
    }

    public void insert(PhieuDangKyDTO phieuDangKy) throws SQLException {
        String sql = "INSERT INTO phieu_dang_ky (trang_thai, dia_chi_giao, ma_kh, nhan_vien_tao) VALUES (?, ?, ?, ?)";

        Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, phieuDangKy.getTrangThai());
        stmt.setString(2, phieuDangKy.getDiaChiGiao());
        stmt.setString(3, phieuDangKy.getKhachHang().getMaKH());
        stmt.setString(4, phieuDangKy.getMaNVTao());
        stmt.executeUpdate();
    }
}
