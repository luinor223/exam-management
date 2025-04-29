package com.hcmus.exammanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@AllArgsConstructor
public class KhachHangDTO {
    private String maKH;
    private String hoTen;
    private String email;
    private String cccd;
    private String sdt;
    private String diaChi;
    private String loai_kh;

    public static KhachHangDTO fromResultSet(ResultSet rs) throws SQLException {
        String maKH = rs.getString("ma_kh");
        String hoTen = rs.getString("ho_ten");
        String email = rs.getString("email");
        String cccd = rs.getString("cccd");
        String sdt = rs.getString("sdt");
        String diaChi = rs.getString("dia_chi");
        String loai_kh = rs.getString("loai_kh");

        return new KhachHangDTO(maKH, hoTen, email, cccd, sdt, diaChi, loai_kh);
    }
}
