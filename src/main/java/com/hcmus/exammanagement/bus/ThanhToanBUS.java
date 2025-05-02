package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.HoaDonDAO;
import com.hcmus.exammanagement.dao.PhieuDangKyDAO;
import com.hcmus.exammanagement.dto.HoaDonDTO;
import com.hcmus.exammanagement.dto.PhieuDangKyDTO;
import com.hcmus.exammanagement.dto.ThongTinLapHDDTO;


import java.util.List;

/**
 * Business logic class for HoaDon (Invoice) operations
 */

public class ThanhToanBUS {
    public static List<HoaDonDTO> LayDsHoaDon() {
        return HoaDonDAO.findAll();
    }

    public static void taoHoaDon(HoaDonDTO hoaDonDTO) {
        HoaDonDAO.insert(hoaDonDTO);
    }

    public static float KiemTraTroGia(List<ThongTinLapHDDTO> danhSachLapHD) {
        float tongTamTinhThucTe = 0;
        int tongSoLuongThiSinh = 0;
        boolean laDonVi = false;

        for (ThongTinLapHDDTO item : danhSachLapHD) {
            tongTamTinhThucTe += item.getTongTien();
            tongSoLuongThiSinh += item.getSoLuongThiSinh();
            if ("Đơn vị".equalsIgnoreCase(item.getLoaiKhachHang())) {
                laDonVi = true;
            }
        }

        if (!laDonVi) return 0;

        float troGia = 0.10f * tongTamTinhThucTe;
        if (tongSoLuongThiSinh > 20) {
            troGia = Math.min(troGia, 0.15f * tongTamTinhThucTe);
        }

        return Math.max(troGia, 0);
    }

    public static String getMoTaTroGia(List<ThongTinLapHDDTO> danhSachLapHD) {
        int tongSoLuongThiSinh = 0;
        boolean laDonVi = false;

        for (ThongTinLapHDDTO item : danhSachLapHD) {
            tongSoLuongThiSinh += item.getSoLuongThiSinh();
            if ("Đơn vị".equalsIgnoreCase(item.getLoaiKhachHang())) {
                laDonVi = true;
            }
        }

        if (!laDonVi) return "";

        return (tongSoLuongThiSinh > 20) ? "Trợ giá 15%" : "Trợ giá 10%";
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

        java.sql.Date ngayDangKy = (java.sql.Date) hoaDon.getPhieuDangKy().getNgayLap();
        java.time.LocalDate localNgayDangKy = ngayDangKy.toLocalDate();

        java.time.LocalDate hanThanhToan = localNgayDangKy.plusDays(3);

        System.out.println("Han thanh toan cua ma pdk " + phieuDangKy.getMaPhieuDangKy() + " : " + hanThanhToan);
        System.out.println("ngay lap phieu " + hoaDon.getPhieuDangKy().getNgayLap());

        return java.time.LocalDate.now().isBefore(hanThanhToan);
    }


    public void xuLyHoaDonQuaHan() {
        List<HoaDonDTO> dsHoaDon = HoaDonDAO.findAll();
        PhieuDangKyDAO phieuDangKyDAO = new PhieuDangKyDAO();

        for (HoaDonDTO hoaDon : dsHoaDon) {
            if (!KiemTraHanThanhToan(hoaDon) && hoaDon.getMaTt() == null) {
                String maHd = hoaDon.getMaHd();
                String maPhieuDangKy = hoaDon.getPhieuDangKy().getMaPhieuDangKy();

                boolean daXoaHoaDon = HoaDonDAO.delete(maHd);

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

    public static boolean xoaHoaDon(String maHd) {
        return HoaDonDAO.delete(maHd);
    }

    public static boolean duyetThanhToan(String maHd, String maThanhToan) {
        if (maThanhToan == null || maThanhToan.isBlank()) {
            return false;
        }
        HoaDonDAO HoaDonDAO = new HoaDonDAO();
        HoaDonDAO.update_matt(maHd, maThanhToan);
        return true;
    }

}
