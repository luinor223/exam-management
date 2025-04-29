package com.hcmus.exammanagement.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@AllArgsConstructor
public class HoaDonDTO {
    private String maHd;
    private float tongTien;
    private String ptThanhToan;
    private float soTienGiam;
    private String ngayLap;
    private String nhanVienTao;
    private PhieuDangKyDTO phieuDangKy;
    private String maTt;


    public static HoaDonDTO fromResultSet(ResultSet rs) throws SQLException {
        String maHd = rs.getString("ma_hd");
        float tongTien = rs.getFloat("tong_tien");
        String ptThanhToan = rs.getString("pt_thanh_toan");
        float soTienGiam = rs.getFloat("so_tien_giam");
        String ngayLap = rs.getString("ngay_lap");
        String nhanVienTao = rs.getString("nhan_vien_tao");
        String maTt = rs.getString("ma_thanh_toan");

        PhieuDangKyDTO phieuDangKy = PhieuDangKyDTO.fromResultSet(rs);

        return new HoaDonDTO(maHd, tongTien, ptThanhToan, soTienGiam, ngayLap, nhanVienTao, phieuDangKy, maTt);
    }
}