package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.KetQuaDAO;
import com.hcmus.exammanagement.dto.KetQuaDTO;
import com.hcmus.exammanagement.dto.KetQuaDayDuDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for KetQua (Result) operations
 */
@Slf4j
public class KetQuaBUS {

    public List<KetQuaDayDuDTO> layTatCaKetQuaChiTiet() {
        return KetQuaDAO.findAllWithExtra();
    }

    public KetQuaDTO layKetQua(String maLT, String sbd) throws IllegalArgumentException, SQLException {
        if (maLT == null || maLT.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        if (sbd == null || sbd.trim().isEmpty()) {
            throw new IllegalArgumentException("Số báo danh không được để trống");
        }

        return KetQuaDAO.findById(maLT, sbd);
    }

    public boolean luuKetQua(KetQuaDTO ketQua) throws IllegalArgumentException {
        if (ketQua == null) {
            throw new IllegalArgumentException("Kết quả không được để trống");
        }

        KetQuaDTO existingKetQua = KetQuaDAO.findById(ketQua.getMaLT(), ketQua.getSbd());
        log.info("KetQuaDAO.findById() = {}", existingKetQua);

        if (existingKetQua == null) {
            return KetQuaDAO.insert(ketQua);
        } else {
            return KetQuaDAO.update(ketQua);
        }
    }

    public boolean capChungChi(String maLT, String sbd) throws IllegalArgumentException {
        if (maLT == null || maLT.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        if (sbd == null || sbd.trim().isEmpty()) {
            throw new IllegalArgumentException("Số báo danh không được để trống");
        }

        KetQuaDTO ketQua = KetQuaDAO.findById(maLT, sbd);

        if (ketQua == null) {
            throw new IllegalArgumentException("Không tìm thấy kết quả cho mã lịch thi " +
                    maLT + " và SBD " + sbd);
        }

        ketQua.setTrangThai("Đã cấp");
        return KetQuaDAO.update(ketQua);
    }
}