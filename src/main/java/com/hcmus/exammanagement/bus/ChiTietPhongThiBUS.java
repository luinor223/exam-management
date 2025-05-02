package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.ChiTietPhongThiDAO;
import com.hcmus.exammanagement.dto.ChiTietPhongThiDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for ChiTietPhongThi (Exam Room Detail) operations
 */
public class ChiTietPhongThiBUS {

    public static List<ChiTietPhongThiDTO> layDSTheoLichThi(String maLichThi) throws IllegalArgumentException, SQLException {
        if (maLichThi == null || maLichThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        return ChiTietPhongThiDAO.findByLichThi(maLichThi);
    }

    public static void themChiTietPhongThi(ChiTietPhongThiDTO chiTietPhongThi) throws IllegalArgumentException, SQLException {
        if (chiTietPhongThi == null) {
            throw new IllegalArgumentException("Chi tiết phòng thi không được để trống");
        }
        
        // Validate exam room detail data
        if (chiTietPhongThi.getMaLichThi() == null || chiTietPhongThi.getMaLichThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        if (chiTietPhongThi.getPhong() == null || chiTietPhongThi.getPhong().getMaPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        
        if (chiTietPhongThi.getGiamThi() == null || chiTietPhongThi.getGiamThi().getMaGiamThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giám thị không được để trống");
        }
        
        if (chiTietPhongThi.getSoLuongToiDa() == null || chiTietPhongThi.getSoLuongToiDa() <= 0) {
            throw new IllegalArgumentException("Số lượng tối đa phải lớn hơn 0");
        }

        ChiTietPhongThiDAO.insert(chiTietPhongThi);
    }

    public static boolean xoaChiTietPhongThi(String maLichThi, String maPhong) throws IllegalArgumentException, SQLException {
        if (maLichThi == null || maLichThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        if (maPhong == null || maPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        
        return ChiTietPhongThiDAO.delete(maLichThi, maPhong) > 0;
    }
}