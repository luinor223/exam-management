package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.PhongDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PhongDAO {

    public static List<PhongDTO> findAllFree(Timestamp ngayGioThi, int thoiLuongThi) throws SQLException {
        List<PhongDTO> phongList = new ArrayList<>();

        Timestamp thoiGianKetThuc = new Timestamp(ngayGioThi.getTime() + ((long) thoiLuongThi * 60 * 1000));

        String sql = "SELECT * FROM phong WHERE ma_phong NOT IN (" +
                        "SELECT ct.ma_phong " +
                        "FROM chi_tiet_phong_thi ct JOIN lich_thi lt ON ct.ma_lt = lt.ma_lt " +
                        "WHERE (lt.ngay_gio_thi < ? " +
                        "AND lt.ngay_gio_thi + (lt.thoi_luong_thi * INTERVAL '1 minute') > ?))";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, thoiGianKetThuc);
            stmt.setTimestamp(2, ngayGioThi);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                phongList.add(PhongDTO.fromResultSet(rs));
            }

        } catch (SQLException e) {
            log.error("Error finding all free phong: {}", e.getMessage());
            throw e;
        }

        return phongList;
    }
}
