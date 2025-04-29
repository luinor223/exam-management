package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.ThiSinhDAO;
import com.hcmus.exammanagement.dto.ThiSinhDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * Business logic class for ThiSinh (Candidate) operations
 */
@Slf4j
public class ThiSinhBUS {
    private final ThiSinhDAO thiSinhDAO;

    public ThiSinhBUS() {
        this.thiSinhDAO = new ThiSinhDAO();
    }

    public List<ThiSinhDTO> layDSThiSinh() {
        return thiSinhDAO.findAll();
    }

    public ThiSinhDTO layThiSinh(String maThiSinh) throws IllegalArgumentException {
        if (maThiSinh == null || maThiSinh.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thí sinh không được để trống");
        }
        return thiSinhDAO.findById(maThiSinh);
    }

    public List<ThiSinhDTO> layThiSinhByPhieuDangKy(String maPhieuDangKy) throws IllegalArgumentException {
        if (maPhieuDangKy == null || maPhieuDangKy.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu đăng ký không được để trống");
        }
        return thiSinhDAO.findByPhieuDangKy(maPhieuDangKy);
    }

    public boolean createThiSinh(ThiSinhDTO thiSinh) throws IllegalArgumentException {
        if (thiSinh == null) {
            throw new IllegalArgumentException("Thí sinh không được để trống");
        }

        // Validate candidate data
        if (thiSinh.getHoTen() == null || thiSinh.getHoTen().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thí sinh không được để trống");
        }

        if (thiSinh.getCccd() == null || thiSinh.getCccd().trim().isEmpty()) {
            throw new IllegalArgumentException("CCCD của thí sinh không được để trống");
        }

        if (thiSinh.getNgaySinh() == null) {
            throw new IllegalArgumentException("Ngày sinh của thí sinh không được để trống");
        }

        // Check if the date of birth is in the future
        if (thiSinh.getNgaySinh().after(new Date())) {
            throw new IllegalArgumentException("Ngày sinh của thí sinh không thể là ngày trong tương lai");
        }

        return thiSinhDAO.insert(thiSinh);
    }

    public boolean updateThiSinh(ThiSinhDTO thiSinh) throws IllegalArgumentException {
        if (thiSinh == null) {
            throw new IllegalArgumentException("Thí sinh không được để trống");
        }

        if (thiSinh.getMaThiSinh() == null || thiSinh.getMaThiSinh().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thí sinh không được để trống");
        }

        // Check if candidate exists
        ThiSinhDTO existingThiSinh = thiSinhDAO.findById(thiSinh.getMaThiSinh());
        if (existingThiSinh == null) {
            throw new IllegalArgumentException("Thí sinh với mã " + thiSinh.getMaThiSinh() + " không tồn tại");
        }

        // Validate candidate data
        if (thiSinh.getHoTen() == null || thiSinh.getHoTen().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thí sinh không được để trống");
        }

        if (thiSinh.getCccd() == null || thiSinh.getCccd().trim().isEmpty()) {
            throw new IllegalArgumentException("CCCD của thí sinh không được để trống");
        }

        if (thiSinh.getNgaySinh() == null) {
            throw new IllegalArgumentException("Ngày sinh của thí sinh không được để trống");
        }

        // Check if the date of birth is in the future
        if (thiSinh.getNgaySinh().after(new Date())) {
            throw new IllegalArgumentException("Ngày sinh của thí sinh không thể là ngày trong tương lai");
        }

        return thiSinhDAO.update(thiSinh);
    }

    public boolean deleteThiSinh(String maThiSinh) throws IllegalArgumentException {
        if (maThiSinh == null || maThiSinh.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thí sinh không được để trống");
        }

        // Check if the candidate exists
        ThiSinhDTO existingThiSinh = thiSinhDAO.findById(maThiSinh);
        if (existingThiSinh == null) {
            throw new IllegalArgumentException("Thí sinh với mã " + maThiSinh + " không tồn tại");
        }

        return thiSinhDAO.delete(maThiSinh);
    }
}
