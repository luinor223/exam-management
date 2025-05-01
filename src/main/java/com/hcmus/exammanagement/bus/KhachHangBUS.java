package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.KhachHangDAO;
import com.hcmus.exammanagement.dto.KhachHangDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for KhachHang (Customer) operations
 */
public class KhachHangBUS {
    private final KhachHangDAO khachHangDAO;

    public KhachHangBUS() {
        this.khachHangDAO = new KhachHangDAO();
    }

    public List<KhachHangDTO> layDSKhachHang() throws SQLException {
        return khachHangDAO.findAll();
    }

    public List<KhachHangDTO> layDSKhachHangDonVi() throws SQLException {
        return khachHangDAO.findByLoaiKh("Đơn vị");
    }

    public List<KhachHangDTO> layDSKhachHangCaNhan() throws SQLException {
        return khachHangDAO.findByLoaiKh("Cá nhân");
    }

    public KhachHangDTO layKhachHangBangCCCD(String cccd) throws IllegalArgumentException, SQLException {
        if (cccd == null || cccd.trim().isEmpty()) {
            throw new IllegalArgumentException("CCCD không được để trống");
        }
        return khachHangDAO.findByCCCD(cccd);
    }

    public void taoKhachHang(KhachHangDTO khachHang) throws IllegalArgumentException, SQLException {
        if (khachHang == null) {
            throw new IllegalArgumentException("Khách hàng không được để trống");
        }

        // Validate customer data
        if (khachHang.getHoTen() == null || khachHang.getHoTen().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống");
        }

        khachHangDAO.insert(khachHang);
    }
}
