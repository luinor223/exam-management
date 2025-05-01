package com.hcmus.exammanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaDayDuDTO {
    // Kết quả fields
    private String maLT;
    private String sbd;
    private int diem;
    private String xepLoai;
    private String nhanXet;
    private LocalDate ngayCapChungChi;
    private LocalDate ngayHetHan;
    private String trangThai;

    // Thí sinh fields
    private String maThiSinh;
    private String hoTenThiSinh;
    private String cccd;

    // Chứng chỉ fields
    private String maChungChi;
    private String tenChungChi;
    private Integer thoiGianHieuLuc;

    // Lịch thi fields
    private LocalDateTime ngayGioThi;

    /**
     * Factory method to create a KetQuaDayDuDTO from a ResultSet with joined data
     */
    public static KetQuaDayDuDTO fromResultSet(ResultSet rs) throws SQLException {
        KetQuaDayDuDTO dto = new KetQuaDayDuDTO();

        // Kết quả fields
        dto.setMaLT(rs.getString("ma_lt"));
        dto.setSbd(rs.getString("sbd"));
        dto.setDiem(rs.getInt("diem"));
        dto.setXepLoai(rs.getString("xep_loai"));
        dto.setNhanXet(rs.getString("nhan_xet"));

        java.sql.Date ngayCapCC = rs.getDate("ngay_cap_chung_chi");
        if (ngayCapCC != null) {
            dto.setNgayCapChungChi(ngayCapCC.toLocalDate());
        }

        java.sql.Date ngayHetHan = rs.getDate("ngay_het_han");
        if (ngayHetHan != null) {
            dto.setNgayHetHan(ngayHetHan.toLocalDate());
        }

        dto.setTrangThai(rs.getString("trang_thai"));

        // Thí sinh fields
        dto.setMaThiSinh(rs.getString("ma_ts"));
        dto.setHoTenThiSinh(rs.getString("ho_ten"));
        dto.setCccd(rs.getString("cccd"));

        // Chứng chỉ fields
        dto.setMaChungChi(rs.getString("ma_cchi"));
        dto.setTenChungChi(rs.getString("ten_chung_chi"));
        dto.setThoiGianHieuLuc(rs.getInt("thoi_gian_hieu_luc"));

        // Lịch thi fields
        java.sql.Timestamp ngayGioThi = rs.getTimestamp("ngay_gio_thi");
        if (ngayGioThi != null) {
            dto.setNgayGioThi(ngayGioThi.toLocalDateTime());
        }

        return dto;
    }
}