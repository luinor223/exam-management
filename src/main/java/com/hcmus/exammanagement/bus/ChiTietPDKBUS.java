package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.ChiTietPDKDAO;
import com.hcmus.exammanagement.dao.PhieuDangKyDAO;
import com.hcmus.exammanagement.dto.ChiTietPDKDTO;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for ChiTietPDK (Registration Form Detail) operations
 */
public class ChiTietPDKBUS {

    public static List<ChiTietPDKDTO> layDSChiTietPDKTheoPDK(String maPDK) throws IllegalArgumentException, SQLException {
        if (maPDK == null || maPDK.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu đăng ký không được để trống");
        }
        return ChiTietPDKDAO.findByPDK(maPDK);
    }

    public static void themChiTietPDK(ChiTietPDKDTO chiTietPDK) throws IllegalArgumentException, SQLException {
        if (chiTietPDK == null) {
            throw new IllegalArgumentException("Chi tiết phiếu đăng ký không được để trống");
        }
        
        // Validate registration form detail data
        if (chiTietPDK.getMaPDK() == null || chiTietPDK.getMaPDK().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu đăng ký không được để trống");
        }
        
        // Check if the registration form exists
        PhieuDangKyDTO phieuDangKy = PhieuDangKyDAO.findById(chiTietPDK.getMaPDK());
        if (phieuDangKy == null) {
            throw new IllegalArgumentException("Phiếu đăng ký với mã " + chiTietPDK.getMaPDK() + " không tồn tại");
        }

        ChiTietPDKDAO.insert(chiTietPDK);
    }

    public static void capNhatChiTietPDK(ChiTietPDKDTO chiTietPDK) throws IllegalArgumentException, SQLException {
        if (chiTietPDK == null) {
            throw new IllegalArgumentException("Chi tiết phiếu đăng ký không được để trống");
        }
        
        if (chiTietPDK.getMaCTPDK() == null || chiTietPDK.getMaCTPDK().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã chi tiết phiếu đăng ký không được để trống");
        }
        
        // Check if registration form detail exists
        ChiTietPDKDTO existingChiTietPDK = ChiTietPDKDAO.findById(chiTietPDK.getMaCTPDK());
        if (existingChiTietPDK == null) {
            throw new IllegalArgumentException("Chi tiết phiếu đăng ký với mã " + chiTietPDK.getMaCTPDK() + " không tồn tại");
        }
        
        // Validate registration form detail data
        if (chiTietPDK.getMaPDK() == null || chiTietPDK.getMaPDK().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu đăng ký không được để trống");
        }
        
        // Check if the registration form exists
        PhieuDangKyDTO phieuDangKy = PhieuDangKyDAO.findById(chiTietPDK.getMaPDK());
        if (phieuDangKy == null) {
            throw new IllegalArgumentException("Phiếu đăng ký với mã " + chiTietPDK.getMaPDK() + " không tồn tại");
        }

        ChiTietPDKDAO.update(chiTietPDK);
    }
}