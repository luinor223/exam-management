package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.ChiTietPhongThiDAO;
import com.hcmus.exammanagement.dao.GiamThiDAO;
import com.hcmus.exammanagement.dao.LichThiDAO;
import com.hcmus.exammanagement.dao.PhongDAO;
import com.hcmus.exammanagement.dto.ChiTietPhongThiDTO;
import com.hcmus.exammanagement.dto.GiamThiDTO;
import com.hcmus.exammanagement.dto.LichThiDTO;
import com.hcmus.exammanagement.dto.PhongDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for ChiTietPhongThi (Exam Room Detail) operations
 */
public class ChiTietPhongThiBUS {
    private final ChiTietPhongThiDAO chiTietPhongThiDAO;
    private final LichThiDAO lichThiDAO;
    private final PhongDAO phongDAO;
    private final GiamThiDAO giamThiDAO;
    
    public ChiTietPhongThiBUS() {
        this.chiTietPhongThiDAO = new ChiTietPhongThiDAO();
        this.lichThiDAO = new LichThiDAO();
        this.phongDAO = new PhongDAO();
        this.giamThiDAO = new GiamThiDAO();
    }

    public List<ChiTietPhongThiDTO> layDSChiTietPhongThi() throws SQLException {
        return chiTietPhongThiDAO.findAll();
    }

    public ChiTietPhongThiDTO layChiTietPhongThi(String maLichThi, String maPhong) throws IllegalArgumentException, SQLException {
        if (maLichThi == null || maLichThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        if (maPhong == null || maPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        
        return chiTietPhongThiDAO.findById(maLichThi, maPhong);
    }

    public List<ChiTietPhongThiDTO> layDSChiTietPhongThiTheoLichThi(String maLichThi) throws IllegalArgumentException, SQLException {
        if (maLichThi == null || maLichThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        return chiTietPhongThiDAO.findByLichThi(maLichThi);
    }

    public List<ChiTietPhongThiDTO> layDSChiTietPhongThiTheoPhong(String maPhong) throws IllegalArgumentException, SQLException {
        if (maPhong == null || maPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        
        return chiTietPhongThiDAO.findByPhong(maPhong);
    }

    public List<ChiTietPhongThiDTO> layDSChiTietPhongThiTheoGiamThi(String maGiamThi) throws IllegalArgumentException, SQLException {
        if (maGiamThi == null || maGiamThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giám thị không được để trống");
        }
        
        return chiTietPhongThiDAO.findByGiamThi(maGiamThi);
    }

    public boolean themChiTietPhongThi(ChiTietPhongThiDTO chiTietPhongThi) throws IllegalArgumentException, SQLException {
        if (chiTietPhongThi == null) {
            throw new IllegalArgumentException("Chi tiết phòng thi không được để trống");
        }
        
        // Validate exam room detail data
        if (chiTietPhongThi.getMaLichThi() == null || chiTietPhongThi.getMaLichThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        if (chiTietPhongThi.getMaPhong() == null || chiTietPhongThi.getMaPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        
        if (chiTietPhongThi.getMaGiamThi() == null || chiTietPhongThi.getMaGiamThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giám thị không được để trống");
        }
        
        if (chiTietPhongThi.getSoLuongToiDa() == null || chiTietPhongThi.getSoLuongToiDa() <= 0) {
            throw new IllegalArgumentException("Số lượng tối đa phải lớn hơn 0");
        }
        
        // Check if the exam schedule exists
        LichThiDTO lichThi = lichThiDAO.findById(chiTietPhongThi.getMaLichThi());
        if (lichThi == null) {
            throw new IllegalArgumentException("Lịch thi với mã " + chiTietPhongThi.getMaLichThi() + " không tồn tại");
        }
        
        // Check if the room exists
        PhongDTO phong = phongDAO.findById(chiTietPhongThi.getMaPhong());
        if (phong == null) {
            throw new IllegalArgumentException("Phòng với mã " + chiTietPhongThi.getMaPhong() + " không tồn tại");
        }
        
        // Check if the supervisor exists
        GiamThiDTO giamThi = giamThiDAO.findById(chiTietPhongThi.getMaGiamThi());
        if (giamThi == null) {
            throw new IllegalArgumentException("Giám thị với mã " + chiTietPhongThi.getMaGiamThi() + " không tồn tại");
        }
        
        // Check if the exam room detail already exists
        ChiTietPhongThiDTO existingChiTietPhongThi = chiTietPhongThiDAO.findById(chiTietPhongThi.getMaLichThi(), chiTietPhongThi.getMaPhong());
        if (existingChiTietPhongThi != null) {
            throw new IllegalArgumentException("Chi tiết phòng thi đã tồn tại");
        }
        
        return chiTietPhongThiDAO.insert(chiTietPhongThi) > 0;
    }

    public boolean capNhatChiTietPhongThi(ChiTietPhongThiDTO chiTietPhongThi) throws IllegalArgumentException, SQLException {
        if (chiTietPhongThi == null) {
            throw new IllegalArgumentException("Chi tiết phòng thi không được để trống");
        }
        
        if (chiTietPhongThi.getMaLichThi() == null || chiTietPhongThi.getMaLichThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        if (chiTietPhongThi.getMaPhong() == null || chiTietPhongThi.getMaPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        
        // Check if exam room detail exists
        ChiTietPhongThiDTO existingChiTietPhongThi = chiTietPhongThiDAO.findById(chiTietPhongThi.getMaLichThi(), chiTietPhongThi.getMaPhong());
        if (existingChiTietPhongThi == null) {
            throw new IllegalArgumentException("Chi tiết phòng thi không tồn tại");
        }
        
        // Validate exam room detail data
        if (chiTietPhongThi.getMaGiamThi() == null || chiTietPhongThi.getMaGiamThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giám thị không được để trống");
        }
        
        if (chiTietPhongThi.getSoLuongToiDa() == null || chiTietPhongThi.getSoLuongToiDa() <= 0) {
            throw new IllegalArgumentException("Số lượng tối đa phải lớn hơn 0");
        }
        
        // Check if the supervisor exists
        GiamThiDTO giamThi = giamThiDAO.findById(chiTietPhongThi.getMaGiamThi());
        if (giamThi == null) {
            throw new IllegalArgumentException("Giám thị với mã " + chiTietPhongThi.getMaGiamThi() + " không tồn tại");
        }
        
        // Check if the current number of candidates is less than or equal to the maximum number
        if (chiTietPhongThi.getSoLuongHienTai() != null && 
            chiTietPhongThi.getSoLuongToiDa() != null && 
            chiTietPhongThi.getSoLuongHienTai() > chiTietPhongThi.getSoLuongToiDa()) {
            throw new IllegalArgumentException("Số lượng hiện tại không thể lớn hơn số lượng tối đa");
        }
        
        return chiTietPhongThiDAO.update(chiTietPhongThi) > 0;
    }

    public boolean xoaChiTietPhongThi(String maLichThi, String maPhong) throws IllegalArgumentException, SQLException {
        if (maLichThi == null || maLichThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        
        if (maPhong == null || maPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        
        // Check if the exam room detail exists
        ChiTietPhongThiDTO existingChiTietPhongThi = chiTietPhongThiDAO.findById(maLichThi, maPhong);
        if (existingChiTietPhongThi == null) {
            throw new IllegalArgumentException("Chi tiết phòng thi không tồn tại");
        }
        
        return chiTietPhongThiDAO.delete(maLichThi, maPhong) > 0;
    }
}