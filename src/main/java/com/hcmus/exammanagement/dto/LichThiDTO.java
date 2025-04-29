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
    private Integer soLuongTS;
    private ChungChiDTO chungChi;

    public static LichThiDTO fromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        String maLichThi = rs.getString("ma_lt");
        Timestamp ngayGioThi = rs.getTimestamp("ngay_gio_thi");
        Integer thoiLuongThi = rs.getInt("thoi_luong_thi");
        Integer soLuongTS = rs.getInt("so_luong_thi_sinh");
        ChungChiDTO chungChi = ChungChiDTO.fromResultSet(rs);

        return new LichThiDTO(maLichThi, ngayGioThi, thoiLuongThi, soLuongTS, chungChi);
    }
}
