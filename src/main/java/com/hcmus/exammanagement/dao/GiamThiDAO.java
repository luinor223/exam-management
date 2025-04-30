package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.GiamThiDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GiamThiDAO {

    public static List<GiamThiDTO> findAllFree(Timestamp ngayGioThi, int thoiLuongThi) throws SQLException {
        List<GiamThiDTO> giamThiList = new ArrayList<>();

        Timestamp thoiGianKetThuc = new Timestamp(ngayGioThi.getTime() + ((long) thoiLuongThi * 60 * 1000));

        String sql = "SELECT * FROM giam_thi WHERE ma_gt NOT IN " +
                        "(SELECT ct.ma_gt " +
                        "FROM chi_tiet_phong_thi ct JOIN lich_thi lt ON ct.ma_lt = lt.ma_lt " +
                        "WHERE (lt.ngay_gio_thi < ? " +
                        "AND lt.ngay_gio_thi + (lt.thoi_luong_thi * INTERVAL '1 minute') > ?) )";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, thoiGianKetThuc);
            stmt.setTimestamp(2, ngayGioThi);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                giamThiList.add(GiamThiDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all free giam thi: {}", e.getMessage());
            throw e;
        }

        return giamThiList;
    }

}
