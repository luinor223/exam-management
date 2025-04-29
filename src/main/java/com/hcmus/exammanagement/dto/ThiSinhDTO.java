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
public class ThiSinhDTO {
    private String maThiSinh;
    private String hoTen;
    private String cccd;
    private Date ngaySinh;
    private String gioiTinh;

    public static ThiSinhDTO fromResultSet(ResultSet rs) throws SQLException {
        String maThiSinh = rs.getString("ma_thi_sinh");
        String hoTen = rs.getString("ho_ten");
        String cccd = rs.getString("cccd");
        Date ngaySinh = rs.getDate("ngay_sinh");
        String gioiTinh = rs.getString("gioi_tinh");

        return new ThiSinhDTO(maThiSinh, hoTen, cccd, ngaySinh, gioiTinh);
    }
}
