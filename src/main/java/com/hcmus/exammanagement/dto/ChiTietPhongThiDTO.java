package com.hcmus.exammanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;

@Getter
@Setter
@AllArgsConstructor
public class ChiTietPhongThiDTO {
    private String maLichThi;
    private PhongDTO phong;
    private GiamThiDTO giamThi;
    private Integer soLuongHienTai;
    private Integer soLuongToiDa;

    public static ChiTietPhongThiDTO fromResultSet(java.sql.ResultSet rs) throws SQLException {
        String maLichThi = rs.getString("ma_lt");
        PhongDTO phongDTO = PhongDTO.fromResultSet(rs);
        GiamThiDTO giamThiDTO = GiamThiDTO.fromResultSet(rs);
        Integer soLuongHienTai = rs.getInt("so_luong_hien_tai");
        Integer soLuongToiDa = rs.getInt("so_luong_toi_da");

        return new ChiTietPhongThiDTO(maLichThi, phongDTO, giamThiDTO, soLuongHienTai, soLuongToiDa);
    }
}
