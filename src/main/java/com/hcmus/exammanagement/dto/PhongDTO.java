package com.hcmus.exammanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PhongDTO {
    private String maPhong;
    private String tenPhong;
    private Integer soGhe;

    public static PhongDTO fromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        String maPhong = rs.getString("ma_phong");
        String tenPhong = rs.getString("ten_phong");
        Integer soGhe = rs.getInt("so_ghe");

        return new PhongDTO(maPhong, tenPhong, soGhe);
    }
}
