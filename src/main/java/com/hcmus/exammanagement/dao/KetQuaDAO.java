package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.HoaDonDTO;
import com.hcmus.exammanagement.dto.KetQuaDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class KetQuaDAO {

    public List<KetQuaDTO> findAll() {
        List<KetQuaDTO> listKetQua = new ArrayList<>();
        String sql = "SELECT * from ket_qua";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery();) {
            while (rs.next()) {
                listKetQua.add(KetQuaDTO.fromResultSet(rs));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listKetQua;
    }

    public KetQuaDTO findById(String maLT, String sbd) {
        String sql = "SELECT * FROM ket_qua WHERE ma_lt = ? AND sbd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maLT);
            stmt.setString(2, sbd);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return KetQuaDTO.fromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean insert(KetQuaDTO ketQua) {
        String sql = "INSERT INTO ket_qua (ma_lt, sbd, diem, xep_loai, nhan_xet, " +
                "ngay_cap_chung_chi, ngay_het_han, trang_thai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ketQua.getMaLT());
            stmt.setString(2, ketQua.getSbd());
            stmt.setInt(4, ketQua.getDiem());
            stmt.setString(5, ketQua.getXepLoai());
            stmt.setString(6, ketQua.getNhanXet());

            if (ketQua.getNgayHetHan() != null) {
                stmt.setDate(7, Date.valueOf(ketQua.getNgayHetHan()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            stmt.setString(8, ketQua.getTrangThai());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean update(KetQuaDTO ketQua) {
        String sql = "UPDATE ket_qua SET diem = ?, xep_loai = ?, nhan_xet = ?, " +
                "ngay_cap_chung_chi = ?, ngay_het_han = ?, trang_thai = ? " +
                "WHERE ma_lt = ? AND sbd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ketQua.getDiem());
            stmt.setString(2, ketQua.getXepLoai());
            stmt.setString(3, ketQua.getNhanXet());

            if (ketQua.getNgayCapChungChi() != null) {
                stmt.setDate(4, Date.valueOf(ketQua.getNgayCapChungChi()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            if (ketQua.getNgayHetHan() != null) {
                stmt.setDate(5, Date.valueOf(ketQua.getNgayHetHan()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            stmt.setString(6, ketQua.getTrangThai());
            stmt.setString(7, ketQua.getMaLT());
            stmt.setString(8, ketQua.getSbd());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}