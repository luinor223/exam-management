package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.ThongTinLapHDDAO;
import com.hcmus.exammanagement.dto.ThongTinLapHDDTO;

import java.util.List;

public class ThongTinLapHDBUS {

    private final ThongTinLapHDDAO thongTinLapHDDAO;

    public ThongTinLapHDBUS() {
        thongTinLapHDDAO = new ThongTinLapHDDAO();
    }

    public List<ThongTinLapHDDTO> getAllThongTinLapHD() {
        return thongTinLapHDDAO.getAllThongTinLapHD();
    }

    public List<ThongTinLapHDDTO> getAllThongTinLapHDbyMapdk(String maPdk) {
        return thongTinLapHDDAO.getAllThongTinLapHDbyMapdk(maPdk);
    }
}

