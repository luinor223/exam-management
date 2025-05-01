package com.hcmus.exammanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhieuDuThiDTO {
    private String maLT;
    private String sbd;
    private String maPhong;
    private LocalDate ngayCap;
    private String maCtpdk;

    // Thông tin bổ sung từ các bảng liên quan
    private String trangThai;  // "Đã có kết quả" hoặc "Chưa có kết quả"
    private LocalDate ngayThi;

    // Phương thức factory để tạo đối tượng từ ResultSet
    public static PhieuDuThiDTO fromResultSet(ResultSet rs) throws SQLException {
        PhieuDuThiDTO dto = new PhieuDuThiDTO();
        dto.setMaLT(rs.getString("ma_lt"));
        dto.setSbd(rs.getString("sbd"));
        dto.setMaPhong(rs.getString("ma_phong"));

        java.sql.Date ngayCap = rs.getDate("ngay_cap");
        if (ngayCap != null) {
            dto.setNgayCap(ngayCap.toLocalDate());
        }

        dto.setMaCtpdk(rs.getString("ma_ctpdk"));

        java.sql.Date ngayThi = rs.getDate("ngay_gio_thi");
        if (ngayThi != null) {
            dto.setNgayThi(ngayThi.toLocalDate());
        }

        dto.setTrangThai(rs.getString("trang_thai"));

        return dto;
    }
}