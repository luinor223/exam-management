package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.NhanVienDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    public static String getLoaiNV(String maNV) {
        String sql = "SELECT loai_nv FROM nhan_vien WHERE ma_nv = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNV);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("loai_nv");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
