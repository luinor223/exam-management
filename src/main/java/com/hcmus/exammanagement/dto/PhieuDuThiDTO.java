package com.hcmus.exammanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhieuDuThiDTO {
    private String maPhieuDuThi;
    private String soBaoDanh;
    private String tenThiSinh;
    private LocalDate ngayCap;
    private String maChiTietPhieuDK;
    private String trangThai; // Trạng thái: "Đã nhập" hoặc "Chưa nhập"
}