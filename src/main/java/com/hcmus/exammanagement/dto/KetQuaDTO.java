package com.hcmus.exammanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaDTO {
    private String maLT;
    private String sbd;
    private int diem;
    private String xepLoai;
    private String nhanXet;
    private LocalDate ngayCapChungChi;
    private LocalDate ngayHetHan;
    private String trangThai = "Chưa cấp";  // Mặc định là chưa cấp

    public static KetQuaDTO fromResultSet(ResultSet rs) throws SQLException {
        KetQuaDTO dto = new KetQuaDTO();
        dto.setMaLT(rs.getString("ma_lt"));
        dto.setSbd(rs.getString("sbd"));
        dto.setDiem(rs.getInt("diem"));
        dto.setXepLoai(rs.getString("xep_loai"));
        dto.setNhanXet(rs.getString("nhan_xet"));

        java.sql.Date ngayCapCC = rs.getDate("ngay_cap_chung_chi");
        if (ngayCapCC != null) {
            dto.setNgayCapChungChi(ngayCapCC.toLocalDate());
        }

        java.sql.Date ngayHetHan = rs.getDate("ngay_het_han");
        if (ngayHetHan != null) {
            dto.setNgayHetHan(ngayHetHan.toLocalDate());
        }

        dto.setTrangThai(rs.getString("trang_thai"));

        return dto;
    }
}