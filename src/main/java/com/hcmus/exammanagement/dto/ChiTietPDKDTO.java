package com.hcmus.exammanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;

@Getter
@Setter
@AllArgsConstructor
public class ChiTietPDKDTO {
    private String maCTPDK;
    private String maPDK;
    private String maThiSinh;
    private String maLichThi;

    public static ChiTietPDKDTO fromResultSet(java.sql.ResultSet rs) throws SQLException {
        String maCTPDK = rs.getString("ma_ctpdk");
        String maPDK = rs.getString("ma_pdk");
        String maThiSinh = rs.getString("ma_ts");
        String maLichThi = rs.getString("ma_lt");

        return new ChiTietPDKDTO(maCTPDK, maPDK, maThiSinh, maLichThi);
    }
}
