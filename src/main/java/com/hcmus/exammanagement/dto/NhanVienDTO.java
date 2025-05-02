package com.hcmus.exammanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NhanVienDTO {
    private String maNV;
    private String hoTen;
    private String cccd;
    private String loaiNV;
    private String trangThai;

    public NhanVienDTO() {}

    public NhanVienDTO(String maNV, String hoTen, String cccd, String loaiNV, String trangThai) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.cccd = cccd;
        this.loaiNV = loaiNV;
        this.trangThai = trangThai;
    }

}
