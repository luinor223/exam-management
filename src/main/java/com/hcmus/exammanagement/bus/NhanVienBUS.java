package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.NhanVienDAO;

public class NhanVienBUS {
    public static String layLoaiNV(String maNV) {
        return NhanVienDAO.getLoaiNV(maNV);
    }
}
