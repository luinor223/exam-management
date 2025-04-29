package com.hcmus.exammanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class PhieuDangKyDTO {
    private String maPhieuDangKy;
    private Date hanNop;
    private String trangThai;
    private Date ngayLap;
    private String diaChiGiao;
    private String maKH;
    private String maNVTao;

    public static PhieuDangKyDTO fromResultSet(ResultSet rs) throws SQLException {
        String maPhieuDangKy = rs.getString("ma_phieu_dang_ky");
        Date hanNop = rs.getDate("han_nop");
        String trangThai = rs.getString("trang_thai");
        Date ngayLap = rs.getDate("ngay_lap");
        String diaChiGiao = rs.getString("dia_chi_giao");
        String maKH = rs.getString("ma_kh");
        String maNVTao = rs.getString("ma_nv_tao");

        return new PhieuDangKyDTO(maPhieuDangKy, hanNop, trangThai, ngayLap, diaChiGiao, maKH, maNVTao);
    }
}
