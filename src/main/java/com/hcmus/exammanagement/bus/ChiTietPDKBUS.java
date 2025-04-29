package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.ChiTietPDKDAO;
import com.hcmus.exammanagement.dao.LichThiDAO;
import com.hcmus.exammanagement.dao.PhieuDangKyDAO;
import com.hcmus.exammanagement.dao.ThiSinhDAO;
import com.hcmus.exammanagement.dto.ChiTietPDKDTO;
import com.hcmus.exammanagement.dto.LichThiDTO;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;
import com.hcmus.exammanagement.dto.ThiSinhDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for ChiTietPDK (Registration Form Detail) operations
 */
public class ChiTietPDKBUS {
    private final ChiTietPDKDAO chiTietPDKDAO;
    private final PhieuDangKyDAO phieuDangKyDAO;
    private final ThiSinhDAO thiSinhDAO;
    private final LichThiDAO lichThiDAO;
    
    public ChiTietPDKBUS() {
        this.chiTietPDKDAO = new ChiTietPDKDAO();
        this.phieuDangKyDAO = new PhieuDangKyDAO();
        this.thiSinhDAO = new ThiSinhDAO();
        this.lichThiDAO = new LichThiDAO();
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

    public boolean themChiTietPDK(ChiTietPDKDTO chiTietPDK) throws IllegalArgumentException, SQLException {
        if (chiTietPDK == null) {
            throw new IllegalArgumentException("Chi tiết phiếu đăng ký không được để trống");
        }
        
        // Validate registration form detail data
        if (chiTietPDK.getMaPDK() == null || chiTietPDK.getMaPDK().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu đăng ký không được để trống");
        }
        
        if (chiTietPDK.getMaThiSinh() == null || chiTietPDK.getMaThiSinh().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thí sinh không được để trống");
        }
        
        if (chiTietPDK.getMaLichThi() == null || chiTietPDK.getMaLichThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        // Check if the registration form exists
        PhieuDangKyDTO phieuDangKy = phieuDangKyDAO.findById(chiTietPDK.getMaPDK());
        if (phieuDangKy == null) {
            throw new IllegalArgumentException("Phiếu đăng ký với mã " + chiTietPDK.getMaPDK() + " không tồn tại");
        }
        
        // Check if the candidate exists
        ThiSinhDTO thiSinh = thiSinhDAO.findById(chiTietPDK.getMaThiSinh());
        if (thiSinh == null) {
            throw new IllegalArgumentException("Thí sinh với mã " + chiTietPDK.getMaThiSinh() + " không tồn tại");
        }
        
        // Check if the exam schedule exists
        LichThiDTO lichThi = lichThiDAO.findById(chiTietPDK.getMaLichThi());
        if (lichThi == null) {
            throw new IllegalArgumentException("Lịch thi với mã " + chiTietPDK.getMaLichThi() + " không tồn tại");
        }
        
        return chiTietPDKDAO.insert(chiTietPDK) > 0;
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
        
        if (chiTietPDK.getMaThiSinh() == null || chiTietPDK.getMaThiSinh().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thí sinh không được để trống");
        }
        
        if (chiTietPDK.getMaLichThi() == null || chiTietPDK.getMaLichThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        // Check if the registration form exists
        PhieuDangKyDTO phieuDangKy = phieuDangKyDAO.findById(chiTietPDK.getMaPDK());
        if (phieuDangKy == null) {
            throw new IllegalArgumentException("Phiếu đăng ký với mã " + chiTietPDK.getMaPDK() + " không tồn tại");
        }
        
        // Check if the candidate exists
        ThiSinhDTO thiSinh = thiSinhDAO.findById(chiTietPDK.getMaThiSinh());
        if (thiSinh == null) {
            throw new IllegalArgumentException("Thí sinh với mã " + chiTietPDK.getMaThiSinh() + " không tồn tại");
        }
        
        // Check if the exam schedule exists
        LichThiDTO lichThi = lichThiDAO.findById(chiTietPDK.getMaLichThi());
        if (lichThi == null) {
            throw new IllegalArgumentException("Lịch thi với mã " + chiTietPDK.getMaLichThi() + " không tồn tại");
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