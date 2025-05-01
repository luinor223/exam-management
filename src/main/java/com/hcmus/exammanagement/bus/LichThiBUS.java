package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.LichThiDAO;
import com.hcmus.exammanagement.dto.LichThiDTO;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Business logic class for LichThi (Exam Schedule) operations
 */
public class LichThiBUS {

    /**
     * Lấy danh sách tất cả lịch thi
     * @return Danh sách lịch thi
     * @throws SQLException Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public static List<LichThiDTO> layDSLichThi() throws SQLException {
        return LichThiDAO.findAll();
    }

    /**
     * Lấy danh sách lịch thi trong tương lai
     * @return Danh sách lịch thi trong tương lai
     * @throws SQLException Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public static List<LichThiDTO> layDSLichThiMoi() throws SQLException {
        return LichThiDAO.findAllNew();
    }

    /**
     * Tạo lịch thi mới
     * @param lichThi Thông tin lịch thi cần tạo
     * @throws IllegalArgumentException Nếu thông tin lịch thi không hợp lệ
     * @throws SQLException Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public static void taoLichThi(LichThiDTO lichThi) throws IllegalArgumentException, SQLException {
        if (lichThi == null) {
            throw new IllegalArgumentException("Thông tin lịch thi không được để trống");
        }

        // Validate exam schedule data
        if (lichThi.getNgayGioThi() == null) {
            throw new IllegalArgumentException("Ngày giờ thi không được để trống");
        }

        // Check if the exam date is in the past
        if (lichThi.getNgayGioThi().before(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("Ngày giờ thi không thể là thời điểm trong quá khứ");
        }

        if (lichThi.getThoiLuongThi() == null || lichThi.getThoiLuongThi() <= 0) {
            throw new IllegalArgumentException("Thời lượng thi phải lớn hơn 0");
        }

        if (lichThi.getChungChi() == null || lichThi.getChungChi().getMaChungChi() == null || 
            lichThi.getChungChi().getMaChungChi().trim().isEmpty()) {
            throw new IllegalArgumentException("Chứng chỉ không được để trống");
        }

        LichThiDAO.insert(lichThi);
    }

    /**
     * Cập nhật thông tin lịch thi
     *
     * @param lichThi Thông tin lịch thi cần cập nhật
     * @throws IllegalArgumentException Nếu thông tin lịch thi không hợp lệ
     * @throws SQLException             Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public static void capNhatLichThi(LichThiDTO lichThi) throws IllegalArgumentException, SQLException {
        if (lichThi == null) {
            throw new IllegalArgumentException("Thông tin lịch thi không được để trống");
        }

        if (lichThi.getMaLichThi() == null || lichThi.getMaLichThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        // Check if exam schedule exists
        LichThiDTO existingLichThi = LichThiDAO.findById(lichThi.getMaLichThi());
        if (existingLichThi == null) {
            throw new IllegalArgumentException("Lịch thi với mã " + lichThi.getMaLichThi() + " không tồn tại");
        }

        // Validate exam schedule data
        if (lichThi.getNgayGioThi() == null) {
            throw new IllegalArgumentException("Ngày giờ thi không được để trống");
        }

        // Check if the exam date is in the past
        if (lichThi.getNgayGioThi().before(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("Ngày giờ thi không thể là thời điểm trong quá khứ");
        }

        if (lichThi.getThoiLuongThi() == null || lichThi.getThoiLuongThi() <= 0) {
            throw new IllegalArgumentException("Thời lượng thi phải lớn hơn 0");
        }

        if (lichThi.getChungChi() == null || lichThi.getChungChi().getMaChungChi() == null || 
            lichThi.getChungChi().getMaChungChi().trim().isEmpty()) {
            throw new IllegalArgumentException("Chứng chỉ không được để trống");
        }

        LichThiDAO.update(lichThi);
    }

    /**
     * Xóa lịch thi
     *
     * @param maLichThi Mã lịch thi cần xóa
     * @throws IllegalArgumentException Nếu mã lịch thi không hợp lệ
     * @throws SQLException             Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public static void xoaLichThi(String maLichThi) throws IllegalArgumentException, SQLException {
        if (maLichThi == null || maLichThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        // Check if the exam schedule exists
        LichThiDTO existingLichThi = LichThiDAO.findById(maLichThi);
        if (existingLichThi == null) {
            throw new IllegalArgumentException("Lịch thi với mã " + maLichThi + " không tồn tại");
        }

        // Check if the exam is in the past
        if (existingLichThi.getNgayGioThi().before(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("Không thể xóa lịch thi đã diễn ra");
        }

        // Check if there are any registered candidates
        if (existingLichThi.getSoLuongHienTai() != null && existingLichThi.getSoLuongHienTai() > 0) {
            throw new IllegalArgumentException("Không thể xóa lịch thi đã có thí sinh đăng ký");
        }

        LichThiDAO.delete(maLichThi);
    }
}
