package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.PhieuDangKyDAO;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * Business logic class for PhieuDangKy (Registration Form) operations
 */
@Slf4j
public class PhieuDangKyBUS {
    private final PhieuDangKyDAO phieuDangKyDAO;
    private final KhachHangBUS khachHangBUS;

    public PhieuDangKyBUS() {
        this.phieuDangKyDAO = new PhieuDangKyDAO();
        this.khachHangBUS = new KhachHangBUS();
    }

    public List<PhieuDangKyDTO> layDSPhieuDangKy() {
        return phieuDangKyDAO.findAll();
    }

    public PhieuDangKyDTO layPhieuDangKy(String maPhieuDangKy) throws IllegalArgumentException {
        if (maPhieuDangKy == null || maPhieuDangKy.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu đăng ký không được để trống");
        }
        return phieuDangKyDAO.findById(maPhieuDangKy);
    }

    public void taoPhieuDangKy(PhieuDangKyDTO phieuDangKy) throws IllegalArgumentException {
        if (phieuDangKy == null) {
            throw new IllegalArgumentException("Phiếu đăng ký không được để trống");
        }

        // Validate registration form data
        if (phieuDangKy.getMaKH() == null || phieuDangKy.getMaKH().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng trống");
        }
        
        // Check if the customer exists
        if (khachHangBUS.getKhachHangById(phieuDangKy.getMaKH()) == null) {
            throw new IllegalArgumentException("Khách hàng không tồn tại: " + phieuDangKy.getMaKH());
        }
        
        if (phieuDangKy.getTrangThai() == null || phieuDangKy.getTrangThai().trim().isEmpty()) {
            phieuDangKy.setTrangThai("Chờ xử lý");
        }
        
        try {
            phieuDangKyDAO.insert(phieuDangKy);
        } catch (Exception e) {
            log.error("Error creating registration form: {}", e.getMessage());
        }
    }
}