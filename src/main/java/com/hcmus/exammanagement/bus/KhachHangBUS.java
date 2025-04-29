package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.KhachHangDAO;
import com.hcmus.exammanagement.dto.KhachHangDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Business logic class for KhachHang (Customer) operations
 */
@Slf4j
public class KhachHangBUS {
    private final KhachHangDAO khachHangDAO;

    public KhachHangBUS() {
        this.khachHangDAO = new KhachHangDAO();
    }

    public List<KhachHangDTO> getAllKhachHang() {
        return khachHangDAO.findAll();
    }

    public List<KhachHangDTO> getKhachHangDonVi() {
        return khachHangDAO.findByLoaiKh("Đơn vị");
    }

    public List<KhachHangDTO> getKhachHangCaNhan() {
        return khachHangDAO.findByLoaiKh("Cá nhân");
    }

    public KhachHangDTO getKhachHangById(String maKH) throws IllegalArgumentException {
        if (maKH == null || maKH.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng không được để trống");
        }
        return khachHangDAO.findById(maKH);
    }

    public boolean createKhachHang(KhachHangDTO khachHang) throws IllegalArgumentException {
        if (khachHang == null) {
            throw new IllegalArgumentException("Khách hàng không được để trống");
        }

        // Validate customer data
        if (khachHang.getHoTen() == null || khachHang.getHoTen().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống");
        }

        try {
            khachHangDAO.insert(khachHang);
            return true;
        } catch (Exception e) {
            log.error("Error creating customer: {}", e.getMessage());
            return false;
        }
    }
}
