package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.KetQuaDAO;
import com.hcmus.exammanagement.dao.PhieuDuThiDAO;
import com.hcmus.exammanagement.dto.KetQuaDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for KetQua (Result) operations
 */
public class KetQuaBUS {
    private final KetQuaDAO ketQuaDAO;
    private final PhieuDuThiDAO phieuDuThiDAO;

    public KetQuaBUS() {
        this.ketQuaDAO = new KetQuaDAO();
        this.phieuDuThiDAO = new PhieuDuThiDAO();
    }

    public List<KetQuaDTO> layTatCaKetQua() throws Exception {
        return ketQuaDAO.findAll();
    }

    public KetQuaDTO getKetQuaByMaLTAndSBD(String maLT, String sbd) throws IllegalArgumentException, SQLException {
        if (maLT == null || maLT.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        if (sbd == null || sbd.trim().isEmpty()) {
            throw new IllegalArgumentException("Số báo danh không được để trống");
        }

        return ketQuaDAO.findById(maLT, sbd);
    }

    public boolean saveKetQua(KetQuaDTO ketQua) throws IllegalArgumentException, SQLException {
        if (ketQua == null) {
            throw new IllegalArgumentException("Kết quả không được để trống");
        }

        if (ketQua.getMaLT() == null || ketQua.getMaLT().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        if (ketQua.getSbd() == null || ketQua.getSbd().trim().isEmpty()) {
            throw new IllegalArgumentException("Số báo danh không được để trống");
        }

        if (phieuDuThiDAO.findById(ketQua.getMaLT(), ketQua.getSbd()) == null) {
            throw new IllegalArgumentException("Phiếu dự thi không tồn tại với mã lịch thi " +
                    ketQua.getMaLT() + " và SBD " + ketQua.getSbd());
        }

        if (ketQua.getDiem() < 0) {
            throw new IllegalArgumentException("Điểm không được âm");
        }

        if (ketQua.getXepLoai() == null || ketQua.getXepLoai().trim().isEmpty()) {
            throw new IllegalArgumentException("Xếp loại không được để trống");
        }

        if (ketQua.getNgayCapChungChi() == null) {
            throw new IllegalArgumentException("Ngày cấp chứng chỉ không được để trống");
        }

        if (ketQua.getNgayHetHan() == null) {
            throw new IllegalArgumentException("Ngày hết hạn không được để trống");
        }

        if (ketQua.getNgayHetHan().isBefore(ketQua.getNgayCapChungChi())) {
            throw new IllegalArgumentException("Ngày hết hạn phải sau ngày cấp chứng chỉ");
        }

        if (ketQua.getTrangThai() == null || ketQua.getTrangThai().trim().isEmpty()) {
            ketQua.setTrangThai("Chưa cấp");
        }

        KetQuaDTO existingKetQua = ketQuaDAO.findById(ketQua.getMaLT(), ketQua.getSbd());

        if (existingKetQua == null) {
            return ketQuaDAO.insert(ketQua);
        } else {
            return ketQuaDAO.update(ketQua);
        }
    }

    public boolean capChungChi(String maLT, String sbd) throws IllegalArgumentException, SQLException {
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