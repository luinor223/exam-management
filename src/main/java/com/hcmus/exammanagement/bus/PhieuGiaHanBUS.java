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
}