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
    private final KetQuaDAO ketQuaDAO;

    public KetQuaBUS() {
        this.ketQuaDAO = new KetQuaDAO();
    }

    public List<KetQuaDTO> layTatCaKetQua() {
        return ketQuaDAO.findAll();
    }

    public List<KetQuaDayDuDTO> layTatCaKetQuaChiTiet() {
        return ketQuaDAO.findAllWithExtra();
    }

    public KetQuaDTO layKetQua(String maLT, String sbd) throws IllegalArgumentException, SQLException {
        if (maLT == null || maLT.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        if (sbd == null || sbd.trim().isEmpty()) {
            throw new IllegalArgumentException("Số báo danh không được để trống");
        }

        return ketQuaDAO.findById(maLT, sbd);
    }

    public boolean luuKetQua(KetQuaDTO ketQua) throws IllegalArgumentException {
        if (ketQua == null) {
            throw new IllegalArgumentException("Kết quả không được để trống");
        }

        KetQuaDTO existingKetQua = ketQuaDAO.findById(ketQua.getMaLT(), ketQua.getSbd());
        log.info("KetQuaDAO.findById() = {}", existingKetQua);

        if (existingKetQua == null) {
            return ketQuaDAO.insert(ketQua);
        } else {
            return ketQuaDAO.update(ketQua);
        }
    }

    public boolean capChungChi(String maLT, String sbd) throws IllegalArgumentException {
        if (maLT == null || maLT.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        if (sbd == null || sbd.trim().isEmpty()) {
            throw new IllegalArgumentException("Số báo danh không được để trống");
        }

        KetQuaDTO ketQua = ketQuaDAO.findById(maLT, sbd);

        if (ketQua == null) {
            throw new IllegalArgumentException("Không tìm thấy kết quả cho mã lịch thi " +
                    maLT + " và SBD " + sbd);
        }

        ketQua.setTrangThai("Đã cấp");
        return ketQuaDAO.update(ketQua);
    }
}