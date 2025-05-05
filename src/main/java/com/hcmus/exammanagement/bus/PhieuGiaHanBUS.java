package com.hcmus.exammanagement.bus;
import com.hcmus.exammanagement.dao.PhieuGiaHanDAO;
import com.hcmus.exammanagement.dto.PhieuGiaHanDTO;

import java.util.List;

public class PhieuGiaHanBUS {
    public static boolean taoPhieuGiaHan(PhieuGiaHanDTO dto) {
        return PhieuGiaHanDAO.insertPhieuGiaHan(dto);
    }

    public static List<PhieuGiaHanDTO> layTatCaPhieuGiaHan() {
        return PhieuGiaHanDAO.getAllPhieuGiaHan();
    }

    public static boolean xoaPhieuGiaHan(String maPGH) {
        return PhieuGiaHanDAO.deletePhieuGiaHan(maPGH);
    }
    public static boolean capNhatThanhToan(String maPGH, boolean daThanhToan) {
        return PhieuGiaHanDAO.updateThanhToan(maPGH, daThanhToan);
    }
}