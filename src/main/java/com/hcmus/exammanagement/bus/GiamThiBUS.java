package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.GiamThiDAO;
import com.hcmus.exammanagement.dto.GiamThiDTO;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Business logic class for GiamThi (Supervisor) operations
 */
public class GiamThiBUS {

    public static List<GiamThiDTO> layDSGiamThiRanh(Timestamp ngayGioThi, int thoiLuongThi) throws SQLException {
        return GiamThiDAO.findAllFree(ngayGioThi, thoiLuongThi);
    }
}