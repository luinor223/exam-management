package com.hcmus.exammanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GiamThiDTO {
    private String maGiamThi;
    private String hoTen;
    private String sdt;
    private String email;

    public static GiamThiDTO fromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        String maGiamThi = rs.getString("ma_gt");
        String hoTen = rs.getString("ho_ten");
        String sdt = rs.getString("sdt");
        String email = rs.getString("email");

        return new GiamThiDTO(maGiamThi, hoTen, sdt, email);
    }
}
