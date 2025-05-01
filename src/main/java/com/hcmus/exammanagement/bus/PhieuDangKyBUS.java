package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.KhachHangDAO;
import com.hcmus.exammanagement.dao.PhieuDangKyDAO;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for PhieuDangKy (Registration Form) operations
 */
public class PhieuDangKyBUS {
    private final PhieuDangKyDAO phieuDangKyDAO;
    private final KhachHangDAO khachHangDAO;

    public PhieuDangKyBUS() {
        this.phieuDangKyDAO = new PhieuDangKyDAO();
        this.khachHangDAO = new KhachHangDAO();
    }

    public List<PhieuDangKyDTO> layDSPhieuDangKy() throws Exception {
        try {
            return phieuDangKyDAO.findAll();
        } catch (SQLException e) {
            throw new Exception("Lỗi khi lấy danh sách phiếu đăng ký: " + e.getMessage());
        }
    }

    public PhieuDangKyDTO layPhieuDangKy(String maPhieuDangKy) throws IllegalArgumentException, SQLException {
        if (maPhieuDangKy == null || maPhieuDangKy.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu đăng ký không được để trống");
        }
        return PhieuDangKyDAO.findById(maPhieuDangKy);
    }

    public List<PhieuDangKyDTO> layDSPhieuDangKyTheoTrangThai(String trangThai) throws SQLException {
        return PhieuDangKyDAO.findByTrangThai(trangThai);
    }

    public void taoPhieuDangKy(PhieuDangKyDTO phieuDangKy) throws IllegalArgumentException, SQLException {
        if (phieuDangKy == null) {
            throw new IllegalArgumentException("Phiếu đăng ký không được để trống");
        }

        // Validate registration form data
        if (phieuDangKy.getKhachHang().getMaKH() == null || phieuDangKy.getKhachHang().getMaKH().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng trống");
        }

        // Check if the customer exists
        if (khachHangDAO.findById(phieuDangKy.getKhachHang().getMaKH()) == null) {
            throw new IllegalArgumentException("Khách hàng không tồn tại: " + phieuDangKy.getKhachHang().getMaKH());
        }

        if (phieuDangKy.getTrangThai() == null || phieuDangKy.getTrangThai().trim().isEmpty()) {
            phieuDangKy.setTrangThai("Chờ xử lý");
        }

        phieuDangKyDAO.insert(phieuDangKy);
    }

    public List<PhieuDangKyDTO> layDSPhieuDangKyChoThanhToan() throws Exception {
        try {
            return phieuDangKyDAO.findAllChoThanhToan();
        } catch (SQLException e) {
            throw new Exception("Lỗi khi lấy danh sách phiếu đăng ký: " + e.getMessage());
        }
    }

    public void capNhatTrangThai(String maPhieu, String trangThai) {
        phieuDangKyDAO.capNhatTrangThai(maPhieu, trangThai);
    }
}
