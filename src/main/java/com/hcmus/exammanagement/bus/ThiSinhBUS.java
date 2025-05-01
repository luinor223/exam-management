package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.ThiSinhDAO;
import com.hcmus.exammanagement.dto.ThiSinhDTO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Business logic class for ThiSinh (Candidate) operations
 */
public class ThiSinhBUS {

    public static List<ThiSinhDTO> layDSThiSinh() throws SQLException {
        return ThiSinhDAO.findAll();
    }

    public static ThiSinhDTO layThiSinhBangMaTS(String maThiSinh) throws IllegalArgumentException, SQLException {
        if (maThiSinh == null || maThiSinh.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thí sinh không được để trống");
        }
        return ThiSinhDAO.findById(maThiSinh);
    }

    public static ThiSinhDTO layThiSinhBangCCCD(String cccd) throws IllegalArgumentException, SQLException {
        if (cccd == null || cccd.trim().isEmpty()) {
            throw new IllegalArgumentException("CCCD không được để trống");
        }
        return ThiSinhDAO.findByCCCD(cccd);
    }

    public static ThiSinhDTO taoThiSinh(ThiSinhDTO thiSinh) throws IllegalArgumentException, SQLException {
        if (thiSinh == null) {
            throw new IllegalArgumentException("Thí sinh không được để trống");
        }

        // Validate candidate data
        if (thiSinh.getHoTen() == null || thiSinh.getHoTen().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thí sinh không được để trống");
        }

        if (thiSinh.getCccd() == null || thiSinh.getCccd().trim().isEmpty()) {
            throw new IllegalArgumentException("CCCD của thí sinh không được để trống");
        }

        if (thiSinh.getNgaySinh() == null) {
            throw new IllegalArgumentException("Ngày sinh của thí sinh không được để trống");
        }

        // Check if the date of birth is in the future
        if (thiSinh.getNgaySinh().after(new Date())) {
            throw new IllegalArgumentException("Ngày sinh của thí sinh không thể là ngày trong tương lai");
        }

        return ThiSinhDAO.insert(thiSinh);
    }
}
