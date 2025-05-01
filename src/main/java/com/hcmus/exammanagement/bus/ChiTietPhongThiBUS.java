package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.ChiTietPhongThiDAO;
import com.hcmus.exammanagement.dto.ChiTietPhongThiDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for ChiTietPhongThi (Exam Room Detail) operations
 */
public class ChiTietPhongThiBUS {

    public static List<ChiTietPhongThiDTO> layDSChiTietPhongThiTheoLichThi(String maLichThi) throws IllegalArgumentException, SQLException {
        if (maLichThi == null || maLichThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        return ChiTietPhongThiDAO.findByLichThi(maLichThi);
    }

    public boolean themChiTietPhongThi(ChiTietPhongThiDTO chiTietPhongThi) throws IllegalArgumentException, SQLException {
        if (chiTietPhongThi == null) {
            throw new IllegalArgumentException("Chi tiết phòng thi không được để trống");
        }
        
        // Validate exam room detail data
        if (chiTietPhongThi.getMaLichThi() == null || chiTietPhongThi.getMaLichThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        if (chiTietPhongThi.getPhongDTO() == null || chiTietPhongThi.getPhongDTO().getMaPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        
        if (chiTietPhongThi.getGiamThiDTO() == null || chiTietPhongThi.getGiamThiDTO().getMaGiamThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giám thị không được để trống");
        }
        
        if (chiTietPhongThi.getSoLuongToiDa() == null || chiTietPhongThi.getSoLuongToiDa() <= 0) {
            throw new IllegalArgumentException("Số lượng tối đa phải lớn hơn 0");
        }

        boolean tonTaiPhongThi = ChiTietPhongThiDAO.checkExist(chiTietPhongThi.getMaLichThi(), chiTietPhongThi.getPhongDTO().getMaPhong());
        if (tonTaiPhongThi) {
            throw new IllegalArgumentException("Chi tiết phòng thi đã tồn tại");
        }
        
        return ChiTietPhongThiDAO.insert(chiTietPhongThi) > 0;
    }

    public static boolean xoaChiTietPhongThi(String maLichThi, String maPhong) throws IllegalArgumentException, SQLException {
        if (maLichThi == null || maLichThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        if (maPhong == null || maPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        
        // Check if the exam room detail exists
        boolean tonTaiPhongThi = ChiTietPhongThiDAO.checkExist(maLichThi, maPhong);
        if (!tonTaiPhongThi) {
            throw new IllegalArgumentException("Chi tiết phòng thi không tồn tại");
        }
        
        return ChiTietPhongThiDAO.delete(maLichThi, maPhong) > 0;
    }

    public static void capNhatSoLuongHienTai(String maLT, String maPhong, int soLuong) throws IllegalArgumentException, SQLException {

        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng hiện tại không hợp lệ");
        }

        ChiTietPhongThiDAO.updateSoLuongHienTai(maLT, maPhong, soLuong);
    }
}