package com.hcmus.exammanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@AllArgsConstructor
public class ChungChiDTO {
    private String maChungChi;
    private String tenChungChi;
    private Integer thoiGianHieuLuc;
    private String moTa;
    private Double lePhi;

    public static ChungChiDTO fromResultSet(ResultSet rs) throws SQLException {
        String maChungChi = rs.getString("ma_cchi");
        String tenChungChi = rs.getString("ten_chung_chi");
        Integer thoiGianHieuLuc = rs.getInt("thoi_gian_hieu_luc");
        String moTa = rs.getString("mo_ta");
        Double lePhi = rs.getDouble("le_phi");

        return new ChungChiDTO(maChungChi, tenChungChi, thoiGianHieuLuc, moTa, lePhi);
    }
}
