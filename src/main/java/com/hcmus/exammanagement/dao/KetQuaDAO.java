package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.HoaDonDTO;
import com.hcmus.exammanagement.dto.KetQuaDTO;
import com.hcmus.exammanagement.dto.KetQuaDayDuDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class KetQuaDAO {

    public static List<KetQuaDTO> findAll() {
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

    public static KetQuaDTO findById(String maLT, String sbd) {
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

    public static boolean insert(KetQuaDTO ketQua) {
        String sql = "INSERT INTO ket_qua (ma_lt, sbd, diem, xep_loai, nhan_xet, " +
                "ngay_cap_chung_chi, ngay_het_han, trang_thai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ketQua.getMaLT());
            stmt.setString(2, ketQua.getSbd());
            stmt.setInt(3, ketQua.getDiem());
            stmt.setString(4, ketQua.getXepLoai());
            stmt.setString(5, ketQua.getNhanXet());
            stmt.setDate(6, Date.valueOf(ketQua.getNgayCapChungChi()));

            if (ketQua.getNgayHetHan() != null) {
                stmt.setDate(7, Date.valueOf(ketQua.getNgayHetHan()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            stmt.setString(8, "Chưa cấp");
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public static boolean update(KetQuaDTO ketQua) {
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

    public static List<KetQuaDayDuDTO> findAllWithExtra() {
        List<KetQuaDayDuDTO> listKetQua = new ArrayList<>();

        String sql = "SELECT kq.*, ts.ma_ts, ts.ho_ten, ts.cccd, " +
                "cc.ma_cchi, cc.ten_chung_chi, cc.thoi_gian_hieu_luc, " +
                "lt.ngay_gio_thi " +
                "FROM ket_qua kq " +
                "JOIN phieu_du_thi pdt ON kq.ma_lt = pdt.ma_lt AND kq.sbd = pdt.sbd " +
                "JOIN chi_tiet_phieu_dk ctpdk ON pdt.ma_ctpdk = ctpdk.ma_ctpdk " +
                "JOIN thi_sinh ts ON ctpdk.ma_ts = ts.ma_ts " +
                "JOIN lich_thi lt ON kq.ma_lt = lt.ma_lt " +
                "JOIN chung_chi cc ON lt.ma_cchi = cc.ma_cchi ";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                listKetQua.add(KetQuaDayDuDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return listKetQua;
    }
}