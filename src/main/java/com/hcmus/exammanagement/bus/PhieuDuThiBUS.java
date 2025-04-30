package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.PhieuDuThiDAO;
import com.hcmus.exammanagement.dto.PhieuDuThiDTO;

import java.util.List;

/**
 * Business logic class for PhieuDuThi (Exam Ticket) operations
 */
public class PhieuDuThiBUS {
    private final PhieuDuThiDAO phieuDuThiDAO;

    public PhieuDuThiBUS() {
        this.phieuDuThiDAO = new PhieuDuThiDAO();
    }

    public List<PhieuDuThiDTO> getAllPhieuDuThi() throws Exception {
        return phieuDuThiDAO.findAll();
    }

    public PhieuDuThiDTO getPhieuDuThi(String maLT, String sbd) throws IllegalArgumentException {
        if (maLT == null || maLT.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        if (sbd == null || sbd.trim().isEmpty()) {
            throw new IllegalArgumentException("Số báo danh không được để trống");
        }

        return phieuDuThiDAO.findById(maLT, sbd);
    }
}