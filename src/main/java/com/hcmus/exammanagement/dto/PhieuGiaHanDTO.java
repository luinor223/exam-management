package com.hcmus.exammanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PhieuGiaHanDTO {
    private String maPhieuGH;
    private String loaiGH;
    private double phiGH;
    private String nhanVienTao;
    private boolean daThanhToan;
    private String maCTPDK;

    public PhieuGiaHanDTO(String maPGH, String loaiGH, double phiGH, String nhanVienTao, boolean daThanhToan, String maCTPDK) {
        this.maPhieuGH = maPGH;
        this.loaiGH = loaiGH;
        this.phiGH = phiGH;
        this.nhanVienTao = nhanVienTao;
        this.daThanhToan = daThanhToan;
        this.maCTPDK = maCTPDK;
    }
}
