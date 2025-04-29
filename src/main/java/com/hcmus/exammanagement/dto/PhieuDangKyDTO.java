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
    private KhachHangDTO khachHang;
    private String maNVTao;

    public static PhieuDangKyDTO fromResultSet(ResultSet rs) throws SQLException {
        String maPhieuDangKy = rs.getString("ma_pdk");
        Date hanNop = rs.getDate("han_nop");
        String trangThai = rs.getString("trang_thai");
        Date ngayLap = rs.getDate("ngay_lap");
        String diaChiGiao = rs.getString("dia_chi_giao");
        String maNVTao = rs.getString("nhan_vien_tao");

        KhachHangDTO khachHang = KhachHangDTO.fromResultSet(rs);

        return new PhieuDangKyDTO(maPhieuDangKy, hanNop, trangThai, ngayLap, diaChiGiao, khachHang, maNVTao);
    }
}
