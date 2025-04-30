package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.HoaDonDAO;
import com.hcmus.exammanagement.dao.PhieuDangKyDAO;
import com.hcmus.exammanagement.dto.HoaDonDTO;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;


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
            return true;
        }

        PhieuDangKyDTO phieuDangKy = hoaDon.getPhieuDangKy();

        // Kiểm tra trạng thái phiếu đăng ký (chỉ thực hiện nếu phiếu không phải là "đã xác nhận")
        if (phieuDangKy.getTrangThai() != null && phieuDangKy.getTrangThai().equals("Đã xác nhận")) {
            return true;
        }

        java.sql.Date ngayDangKy = (java.sql.Date) phieuDangKy.getNgayLap();
        java.time.LocalDate localNgayDangKy = ngayDangKy.toLocalDate();

        java.time.LocalDate hanThanhToan = localNgayDangKy.plusDays(3);

        System.out.println("Han thanh toan cua ma pdk " + phieuDangKy.getMaPhieuDangKy() + " : " + hanThanhToan);

        return java.time.LocalDate.now().isBefore(hanThanhToan);
    }


    public void xuLyHoaDonQuaHan() {
        List<HoaDonDTO> dsHoaDon = hoaDonDAO.findAll();
        PhieuDangKyDAO phieuDangKyDAO = new PhieuDangKyDAO();

        for (HoaDonDTO hoaDon : dsHoaDon) {
            if (!KiemTraHanThanhToan(hoaDon) && hoaDon.getMaTt() == null) {
                String maHd = hoaDon.getMaHd();
                String maPhieuDangKy = hoaDon.getPhieuDangKy().getMaPhieuDangKy();

                boolean daXoaHoaDon = hoaDonDAO.delete(maHd);

                if (daXoaHoaDon) {
                    System.out.println("Đã xóa hóa đơn quá hạn: " + maPhieuDangKy);
                    boolean daHuyPDK = phieuDangKyDAO.huyPhieuDangKy(maPhieuDangKy);
                    if (daHuyPDK) {
                        System.out.println("Đã hủy phiếu đăng ký quá hạn: " + maPhieuDangKy);
                    }
                }
            }
        }
    }



    public boolean xoaHoaDon(String maHd) {
        return hoaDonDAO.delete(maHd);
    }

    public boolean duyetThanhToan(String maHd, String maThanhToan) {
        if (maThanhToan == null || maThanhToan.isBlank()) {
            return false;
        }
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        hoaDonDAO.update_matt(maHd, maThanhToan);
        return true;
    }

}
