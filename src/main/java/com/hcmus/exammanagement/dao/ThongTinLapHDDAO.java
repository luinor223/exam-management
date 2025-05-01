package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.ThongTinLapHDDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ThongTinLapHDDAO {

    private static final String QUERY = """
        SELECT
            pdk.ma_pdk,
            kh.ma_kh,
            kh.loai_kh,
            cc.ten_chung_chi,
            lt.ngay_gio_thi,
            cc.le_phi,
            COUNT(ct.ma_ctpdk) AS so_luong_thi_sinh,
            cc.le_phi * COUNT(ct.ma_ctpdk) AS tong_tien
        FROM phieu_dang_ky pdk
        JOIN chi_tiet_phieu_dk ct ON ct.ma_pdk = pdk.ma_pdk
        JOIN lich_thi lt ON lt.ma_lt = ct.ma_lt
        JOIN chung_chi cc ON cc.ma_cchi = lt.ma_cchi
        JOIN khach_hang kh ON kh.ma_kh = pdk.ma_kh
        GROUP BY pdk.ma_pdk, kh.ma_kh, cc.ten_chung_chi, lt.ngay_gio_thi, cc.le_phi, kh.loai_kh
    """;

    private static final String QUERY1 = """
        SELECT
            pdk.ma_pdk,
            kh.ma_kh,
            kh.loai_kh,
            cc.ten_chung_chi,
            lt.ngay_gio_thi,
            cc.le_phi,
            COUNT(ct.ma_ctpdk) AS so_luong_thi_sinh,
            cc.le_phi * COUNT(ct.ma_ctpdk) AS tong_tien
        FROM phieu_dang_ky pdk
        JOIN chi_tiet_phieu_dk ct ON ct.ma_pdk = pdk.ma_pdk
        JOIN lich_thi lt ON lt.ma_lt = ct.ma_lt
        JOIN chung_chi cc ON cc.ma_cchi = lt.ma_cchi
        JOIN khach_hang kh ON kh.ma_kh = pdk.ma_kh
        WHERE pdk.ma_pdk = ?
        GROUP BY pdk.ma_pdk, kh.ma_kh, cc.ten_chung_chi, lt.ngay_gio_thi, cc.le_phi, kh.loai_kh
    """;

    public static List<ThongTinLapHDDTO> getAllThongTinLapHD() {
        List<ThongTinLapHDDTO> result = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ThongTinLapHDDTO dto = ThongTinLapHDDTO.fromResultSet(rs);
                result.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<ThongTinLapHDDTO> getAllThongTinLapHDbyMapdk(String maPdk) {
        List<ThongTinLapHDDTO> result = new ArrayList<>();

        String queryWithFilter = QUERY1;
        System.out.println("Query SQL = " + queryWithFilter);

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryWithFilter)) {

            stmt.setString(1, maPdk);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ThongTinLapHDDTO dto = ThongTinLapHDDTO.fromResultSet(rs);
                    result.add(dto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
