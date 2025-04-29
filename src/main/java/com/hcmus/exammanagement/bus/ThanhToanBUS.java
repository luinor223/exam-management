package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.HoaDonDAO;
import com.hcmus.exammanagement.dto.HoaDonDTO;


import java.util.List;

/**
 * Business logic class for HoaDon (Invoice) operations
 */

public class ThanhToanBUS {
    private final HoaDonDAO hoaDonDAO;

    public ThanhToanBUS() {
        this.hoaDonDAO = new HoaDonDAO();
    }

    public List<HoaDonDTO> LayDsHoaDon() {
        return hoaDonDAO.findAll();
    }

    public HoaDonDTO LayHoaDonTheoId(String maHd) {
        return  hoaDonDAO.findById(maHd);
    }

    public void taoHoaDon(HoaDonDTO hoaDonDTO) {
        hoaDonDAO.insert(hoaDonDTO);
    }

    // Kiểm tra hạn thanh toán (3 ngày)
    public boolean KiemTraHanThanhToan(HoaDonDTO hoaDon) {
        java.time.LocalDate ngayDangKy = java.time.LocalDate.parse(hoaDon.getNgayLap());
        java.time.LocalDate hanThanhToan = ngayDangKy.plusDays(3);
        return java.time.LocalDate.now().isBefore(hanThanhToan);
    }

    public boolean xoaHoaDon(String maHd) {
        hoaDonDAO.delete(maHd);
        return false;
    }

}
