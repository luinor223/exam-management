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
    private final ChiTietPDKDAO chiTietPDKDAO;
    
    public ChiTietPDKBUS() {
        this.chiTietPDKDAO = new ChiTietPDKDAO();
    }

    public List<ChiTietPDKDTO> layDSChiTietPDK() throws SQLException {
        return chiTietPDKDAO.findAll();
    }

    public ChiTietPDKDTO layChiTietPDK(String maCTPDK) throws IllegalArgumentException, SQLException {
        if (maCTPDK == null || maCTPDK.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã chi tiết phiếu đăng ký không được để trống");
        }
        return chiTietPDKDAO.findById(maCTPDK);
    }

    public List<ChiTietPDKDTO> layDSChiTietPDKTheoPhieuDangKy(String maPDK) throws IllegalArgumentException, SQLException {
        if (maPDK == null || maPDK.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu đăng ký không được để trống");
        }
        return chiTietPDKDAO.findByPhieuDangKy(maPDK);
    }

    public List<ChiTietPDKDTO> layDSChiTietPDKTheoThiSinh(String maThiSinh) throws IllegalArgumentException, SQLException {
        if (maThiSinh == null || maThiSinh.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thí sinh không được để trống");
        }
        return chiTietPDKDAO.findByThiSinh(maThiSinh);
    }

    public List<ChiTietPDKDTO> layDSChiTietPDKTheoLichThi(String maLichThi) throws IllegalArgumentException, SQLException {
        if (maLichThi == null || maLichThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        return chiTietPDKDAO.findByLichThi(maLichThi);
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

    public boolean capNhatChiTietPDK(ChiTietPDKDTO chiTietPDK) throws IllegalArgumentException, SQLException {
        if (chiTietPDK == null) {
            throw new IllegalArgumentException("Chi tiết phiếu đăng ký không được để trống");
        }
        
        if (chiTietPDK.getMaCTPDK() == null || chiTietPDK.getMaCTPDK().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã chi tiết phiếu đăng ký không được để trống");
        }
        
        // Check if registration form detail exists
        ChiTietPDKDTO existingChiTietPDK = chiTietPDKDAO.findById(chiTietPDK.getMaCTPDK());
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
        
        return chiTietPDKDAO.update(chiTietPDK) > 0;
    }

    public boolean xoaChiTietPDK(String maCTPDK) throws IllegalArgumentException, SQLException {
        if (maCTPDK == null || maCTPDK.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã chi tiết phiếu đăng ký không được để trống");
        }
        
        // Check if the registration form detail exists
        ChiTietPDKDTO existingChiTietPDK = chiTietPDKDAO.findById(maCTPDK);
        if (existingChiTietPDK == null) {
            throw new IllegalArgumentException("Chi tiết phiếu đăng ký với mã " + maCTPDK + " không tồn tại");
        }
        
        return chiTietPDKDAO.delete(maCTPDK) > 0;
    }
}