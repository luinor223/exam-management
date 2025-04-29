package com.hcmus.exammanagement.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThongTinLapHDDTO {
    private String maPhieuDangKy;
    private String maKhachHang;
    private String loaiKhachHang;
    private String tenChungChi;
    private String ngayGioThi;
    private Float lePhi;
    private Integer soLuongThiSinh;
    private Float tongTien;

    public static ThongTinLapHDDTO fromResultSet(ResultSet rs) throws SQLException {
        ThongTinLapHDDTO dto = new ThongTinLapHDDTO();

        dto.setMaPhieuDangKy(rs.getString("ma_pdk"));
        dto.setMaKhachHang(rs.getString("ma_kh"));
        dto.setLoaiKhachHang(rs.getString("loai_kh"));
        dto.setTenChungChi(rs.getString("ten_chung_chi"));
        dto.setNgayGioThi(rs.getString("ngay_gio_thi"));
        dto.setLePhi(rs.getFloat("le_phi"));
        dto.setSoLuongThiSinh(rs.getInt("so_luong_thi_sinh"));
        dto.setTongTien(rs.getFloat("tong_tien"));

        return dto;
    }
}
