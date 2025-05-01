package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.ThongTinLapHDDAO;
import com.hcmus.exammanagement.dto.ThongTinLapHDDTO;

import java.util.List;

public class ThongTinLapHDBUS {
    public static List<ThongTinLapHDDTO> getAllThongTinLapHD() {
        return ThongTinLapHDDAO.getAllThongTinLapHD();
    }

    public static List<ThongTinLapHDDTO> LayThongTinLapHDbyMapdk(String maPdk) {
        return ThongTinLapHDDAO.getAllThongTinLapHDbyMapdk(maPdk);
    }
}

