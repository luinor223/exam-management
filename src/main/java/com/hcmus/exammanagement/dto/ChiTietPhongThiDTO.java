package com.hcmus.exammanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChiTietPhongThiDTO {
    private String maLichThi;
    private String maPhong;
    private String maGiamThi;
    private Integer soLuongHienTai;
    private Integer soLuongToiDa;

    public static ChiTietPhongThiDTO fromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        String maLichThi = rs.getString("ma_lt");
        String maPhong = rs.getString("ma_phong");
        String maGiamThi = rs.getString("ma_gt");
        Integer soLuongHienTai = rs.getInt("so_luong_hien_tai");
        Integer soLuongToiDa = rs.getInt("so_luong_toi_da");

        return new ChiTietPhongThiDTO(maLichThi, maPhong, maGiamThi, soLuongHienTai, soLuongToiDa);
    }
}
