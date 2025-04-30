package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.PhongDAO;
import com.hcmus.exammanagement.dto.PhongDTO;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Business logic class for Phong (Room) operations
 */
public class PhongBUS {

    public static List<PhongDTO> layDSPhongTrong(Timestamp ngayGioThi, int thoiLuongThi) throws SQLException {
        return PhongDAO.findAllFree(ngayGioThi, thoiLuongThi);
    }
}