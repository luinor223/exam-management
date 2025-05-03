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

    public static List<PhieuDangKyDTO> layDSPDKTheoTrangThai(String trangThai) throws SQLException {
        return PhieuDangKyDAO.findByTrangThai(trangThai);
    }

    public static PhieuDangKyDTO taoPhieuDangKy(PhieuDangKyDTO phieuDangKy) throws IllegalArgumentException, SQLException {
        if (phieuDangKy == null) {
            throw new IllegalArgumentException("Phiếu đăng ký không được để trống");
        }

        // Validate registration form data
        if (phieuDangKy.getKhachHang().getMaKH() == null || phieuDangKy.getKhachHang().getMaKH().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng trống");
        }

        // Check if the customer exists
        if (KhachHangDAO.findById(phieuDangKy.getKhachHang().getMaKH()) == null) {
            throw new IllegalArgumentException("Khách hàng không tồn tại: " + phieuDangKy.getKhachHang().getMaKH());
        }

        if (phieuDangKy.getTrangThai() == null || phieuDangKy.getTrangThai().trim().isEmpty()) {
            phieuDangKy.setTrangThai("Chờ xử lý");
        }

        return PhieuDangKyDAO.insert(phieuDangKy);
    }

    public static List<PhieuDangKyDTO> layDSPhieuDangKyChoThanhToan() throws Exception {
        try {
            return PhieuDangKyDAO.findAllByTinhTrang("Chờ xử lý");
        } catch (SQLException e) {
            throw new Exception("Lỗi khi lấy danh sách phiếu đăng ký: " + e.getMessage());
        }
    }

    public static void capNhatTrangThai(String maPhieu, String trangThai) {
        PhieuDangKyDAO.capNhatTrangThai(maPhieu, trangThai);
    }
}
