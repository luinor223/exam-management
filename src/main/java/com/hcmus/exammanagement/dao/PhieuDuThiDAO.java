package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.PhieuDuThiDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhieuDuThiDAO {
    public List<PhieuDuThiDTO> findAll() {
        List<PhieuDuThiDTO> result = new ArrayList<>();

        String sql = "SELECT pdt.ma_lt, pdt.sbd, pdt.ma_phong, pdt.ngay_cap, pdt.ma_ctpdk, " +
                "lt.ngay_gio_thi::date, " +
                "(CASE WHEN EXISTS (SELECT 1 FROM ket_qua kq WHERE kq.ma_lt = pdt.ma_lt AND kq.sbd = pdt.sbd) " +
                "THEN 'Đã có kết quả' ELSE 'Chưa có kết quả' END) as trang_thai " +
                "FROM phieu_du_thi pdt " +
                "JOIN lich_thi lt ON pdt.ma_lt = lt.ma_lt ";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(PhieuDuThiDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public PhieuDuThiDTO findById(String maLT, String sbd) {
        String sql = "SELECT pdt.ma_lt, pdt.sbd, pdt.ma_phong, pdt.ngay_cap, pdt.ma_ctpdk, " +
                "lt.ngay_gio_thi::date, " +
                "(CASE WHEN EXISTS (SELECT 1 FROM ket_qua kq WHERE kq.ma_lt = pdt.ma_lt AND kq.sbd = pdt.sbd) " +
                "THEN 'Đã có kết quả' ELSE 'Chưa có kết quả' END) as trang_thai " +
                "FROM phieu_du_thi pdt " +
                "JOIN lich_thi lt ON pdt.ma_lt = lt.ma_lt " +
                "WHERE pdt.ma_lt = ? AND pdt.sbd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maLT);
            stmt.setString(2, sbd);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return PhieuDuThiDTO.fromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public int insertPhieuDuThi(PhieuDuThiDTO pdt) throws SQLException {
        String sql = "INSERT INTO phieu_du_thi (ma_lt, sbd, ma_phong, ma_ctpdk) VALUES (?, ?, ?, ?);";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int paramId = 1;
            stmt.setString(paramId++, pdt.getMaLT());
            stmt.setString(paramId++, pdt.getSbd());
            stmt.setString(paramId++, pdt.getMaPhong());
            stmt.setString(paramId, pdt.getMaCtpdk());

            return stmt.executeUpdate();
        }
    }

    public int countByLichThi(String maLT) throws SQLException {
        String sql = "SELECT COUNT(*) FROM phieu_du_thi WHERE ma_lt = ?";
        
        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, maLT);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}