package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.KhachHangDAO;
import com.hcmus.exammanagement.dto.KhachHangDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for KhachHang (Customer) operations
 */
public class KhachHangBUS {

    public static List<KhachHangDTO> layDSKhachHangDonVi() throws SQLException {
        return KhachHangDAO.findByLoaiKh("Đơn vị");
    }

    public static List<KhachHangDTO> layDSKhachHangCaNhan() throws SQLException {
        return KhachHangDAO.findByLoaiKh("Cá nhân");
    }

    public static KhachHangDTO layKhachHangBangCCCD(String cccd) throws IllegalArgumentException, SQLException {
        if (cccd == null || cccd.trim().isEmpty()) {
            throw new IllegalArgumentException("CCCD không được để trống");
        }
        return KhachHangDAO.findByCCCD(cccd);
    }

    public static KhachHangDTO taoKhachHang(KhachHangDTO khachHang) throws IllegalArgumentException, SQLException {
        if (khachHang == null) {
            throw new IllegalArgumentException("Khách hàng không được để trống");
        }

        // Validate customer data
        if (khachHang.getHoTen() == null || khachHang.getHoTen().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống");
        }

        return KhachHangDAO.insert(khachHang);
    }
}
