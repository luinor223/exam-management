package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.PhongDAO;
import com.hcmus.exammanagement.dto.PhongDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for Phong (Room) operations
 */
public class PhongBUS {
    private final PhongDAO phongDAO;
    
    public PhongBUS() {
        this.phongDAO = new PhongDAO();
    }

    public List<PhongDTO> layDSPhong() throws SQLException {
        return phongDAO.findAll();
    }

    public PhongDTO layPhongTheoMa(String maPhong) throws IllegalArgumentException, SQLException {
        if (maPhong == null || maPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        return phongDAO.findById(maPhong);
    }

    public boolean themPhong(PhongDTO phong) throws IllegalArgumentException, SQLException {
        if (phong == null) {
            throw new IllegalArgumentException("Phòng không được để trống");
        }
        
        // Validate room data
        if (phong.getTenPhong() == null || phong.getTenPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được để trống");
        }
        
        if (phong.getSoGhe() == null || phong.getSoGhe().trim().isEmpty()) {
            throw new IllegalArgumentException("Số ghế không được để trống");
        }
        
        return phongDAO.insert(phong) > 0;
    }

    public boolean capNhatPhong(PhongDTO phong) throws IllegalArgumentException, SQLException {
        if (phong == null) {
            throw new IllegalArgumentException("Phòng không được để trống");
        }
        
        if (phong.getMaPhong() == null || phong.getMaPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        
        // Check if room exists
        PhongDTO existingPhong = phongDAO.findById(phong.getMaPhong());
        if (existingPhong == null) {
            throw new IllegalArgumentException("Phòng với mã " + phong.getMaPhong() + " không tồn tại");
        }
        
        // Validate room data
        if (phong.getTenPhong() == null || phong.getTenPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được để trống");
        }
        
        if (phong.getSoGhe() == null || phong.getSoGhe().trim().isEmpty()) {
            throw new IllegalArgumentException("Số ghế không được để trống");
        }
        
        return phongDAO.update(phong) > 0;
    }

    public boolean xoaPhong(String maPhong) throws IllegalArgumentException, SQLException {
        if (maPhong == null || maPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        
        // Check if the room exists
        PhongDTO existingPhong = phongDAO.findById(maPhong);
        if (existingPhong == null) {
            throw new IllegalArgumentException("Phòng với mã " + maPhong + " không tồn tại");
        }
        
        return phongDAO.delete(maPhong) > 0;
    }
}