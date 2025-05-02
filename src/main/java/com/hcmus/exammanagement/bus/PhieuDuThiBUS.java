package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.ChiTietPDKDAO;
import com.hcmus.exammanagement.dao.ChiTietPhongThiDAO;
import com.hcmus.exammanagement.dao.PhieuDuThiDAO;
import com.hcmus.exammanagement.dto.ChiTietPDKDTO;
import com.hcmus.exammanagement.dto.ChiTietPhongThiDTO;
import com.hcmus.exammanagement.dto.PhieuDuThiDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for PhieuDuThi (Exam Ticket) operations
 */
public class PhieuDuThiBUS {
    private final PhieuDuThiDAO phieuDuThiDAO;
    private final ChiTietPDKDAO chiTietPDKDAO;

    public PhieuDuThiBUS() {
        this.phieuDuThiDAO = new PhieuDuThiDAO();
        this.chiTietPDKDAO = new ChiTietPDKDAO();
    }

    public List<PhieuDuThiDTO> layDSPhieuDuThi() {
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

    public int PhatPhieuDuThi(String maLT) throws SQLException {
        if (maLT == null || maLT.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        List<ChiTietPhongThiDTO> dsChiTietPhongThi = ChiTietPhongThiDAO.findByLichThi(maLT);
        List<ChiTietPDKDTO> dsChiTietPDK = chiTietPDKDAO.findByLichThi(maLT);

        int phieuDuThiCount = 0;
        // Voi moi phong, them phieu du thi cho den khi day thi chuyen sang phong khac
        for (var chiTietPhongThiDTO : dsChiTietPhongThi) {
            int soLuongToiDa = chiTietPhongThiDTO.getSoLuongToiDa();
            int soLuongHienTai = 0;

            while (soLuongHienTai < soLuongToiDa && phieuDuThiCount < dsChiTietPDK.size()) {
                var chiTietPDKDTO = dsChiTietPDK.get(phieuDuThiCount);

                var phieuDuThiDTO = new PhieuDuThiDTO();
                phieuDuThiDTO.setSbd(String.format("%06d", phieuDuThiCount));
                phieuDuThiDTO.setMaLT(maLT);
                phieuDuThiDTO.setMaPhong(chiTietPhongThiDTO.getPhong().getMaPhong());
                phieuDuThiDTO.setMaCtpdk(chiTietPDKDTO.getMaCTPDK());
                try {
                    phieuDuThiDAO.insert(phieuDuThiDTO);
                    phieuDuThiCount++;
                    soLuongHienTai++;
                }
                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return phieuDuThiCount;
    }
}