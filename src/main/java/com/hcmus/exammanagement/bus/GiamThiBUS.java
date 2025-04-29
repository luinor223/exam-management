package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.GiamThiDAO;
import com.hcmus.exammanagement.dto.GiamThiDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for GiamThi (Supervisor) operations
 */
public class GiamThiBUS {
    private final GiamThiDAO giamThiDAO;
    
    public GiamThiBUS() {
        this.giamThiDAO = new GiamThiDAO();
    }

    public List<GiamThiDTO> layDSGiamThi() throws SQLException {
        return giamThiDAO.findAll();
    }

    public GiamThiDTO layGiamThiTheoMa(String maGiamThi) throws IllegalArgumentException, SQLException {
        if (maGiamThi == null || maGiamThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giám thị không được để trống");
        }
        return giamThiDAO.findById(maGiamThi);
    }

    public boolean themGiamThi(GiamThiDTO giamThi) throws IllegalArgumentException, SQLException {
        if (giamThi == null) {
            throw new IllegalArgumentException("Giám thị không được để trống");
        }
        
        // Validate supervisor data
        if (giamThi.getHoTen() == null || giamThi.getHoTen().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên giám thị không được để trống");
        }
        
        if (giamThi.getSdt() == null || giamThi.getSdt().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }
        
        return giamThiDAO.insert(giamThi) > 0;
    }

    public boolean capNhatGiamThi(GiamThiDTO giamThi) throws IllegalArgumentException, SQLException {
        if (giamThi == null) {
            throw new IllegalArgumentException("Giám thị không được để trống");
        }
        
        if (giamThi.getMaGiamThi() == null || giamThi.getMaGiamThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giám thị không được để trống");
        }
        
        // Check if supervisor exists
        GiamThiDTO existingGiamThi = giamThiDAO.findById(giamThi.getMaGiamThi());
        if (existingGiamThi == null) {
            throw new IllegalArgumentException("Giám thị với mã " + giamThi.getMaGiamThi() + " không tồn tại");
        }
        
        // Validate supervisor data
        if (giamThi.getHoTen() == null || giamThi.getHoTen().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên giám thị không được để trống");
        }
        
        if (giamThi.getSdt() == null || giamThi.getSdt().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }
        
        return giamThiDAO.update(giamThi) > 0;
    }

    public boolean xoaGiamThi(String maGiamThi) throws IllegalArgumentException, SQLException {
        if (maGiamThi == null || maGiamThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giám thị không được để trống");
        }
        
        // Check if the supervisor exists
        GiamThiDTO existingGiamThi = giamThiDAO.findById(maGiamThi);
        if (existingGiamThi == null) {
            throw new IllegalArgumentException("Giám thị với mã " + maGiamThi + " không tồn tại");
        }
        
        return giamThiDAO.delete(maGiamThi) > 0;
    }
}