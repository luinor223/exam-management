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
        if (hoaDon == null || hoaDon.getPhieuDangKy() == null || hoaDon.getPhieuDangKy().getNgayLap() == null) {
            return false;
        }

        java.sql.Date ngayDangKy = (java.sql.Date) hoaDon.getPhieuDangKy().getNgayLap();
        java.time.LocalDate localNgayDangKy = ngayDangKy.toLocalDate();

        // Cộng thêm 3 ngày vào ngày đăng ký
        java.time.LocalDate hanThanhToan = localNgayDangKy.plusDays(3);
        return java.time.LocalDate.now().isBefore(hanThanhToan);
    }



    public boolean xoaHoaDon(String maHd) {
        hoaDonDAO.delete(maHd);
        return false;
    }

}
