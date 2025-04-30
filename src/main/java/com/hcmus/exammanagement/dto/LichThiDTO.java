package com.hcmus.exammanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class LichThiDTO {
    private String maLichThi;
    private Timestamp ngayGioThi;
    private Integer thoiLuongThi;
    private ChungChiDTO chungChi;
    private Integer soLuongHienTai;
    private Integer soLuongToiDa;

    public static LichThiDTO fromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        String maLichThi = rs.getString("ma_lt");
        Timestamp ngayGioThi = rs.getTimestamp("ngay_gio_thi");
        Integer thoiLuongThi = rs.getInt("thoi_luong_thi");
        ChungChiDTO chungChi = ChungChiDTO.fromResultSet(rs);
        Integer soLuongHienTai = rs.getInt("so_luong_hien_tai");
        Integer soLuongToiDa = rs.getInt("so_luong_toi_da");

        return new LichThiDTO(maLichThi, ngayGioThi, thoiLuongThi, chungChi, soLuongHienTai, soLuongToiDa);
    }
}
