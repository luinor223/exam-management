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
    private final LichThiDAO lichThiDAO;

    public LichThiBUS() {
        this.lichThiDAO = new LichThiDAO();
    }

    /**
     * Lấy danh sách tất cả lịch thi
     * @return Danh sách lịch thi
     * @throws SQLException Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public List<LichThiDTO> layDSLichThi() throws SQLException {
        return lichThiDAO.findAll();
    }

    /**
     * Lấy danh sách lịch thi trong tương lai
     * @return Danh sách lịch thi trong tương lai
     * @throws SQLException Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public List<LichThiDTO> layDSLichThiMoi() throws SQLException {
        return lichThiDAO.findAllNew();
    }

    /**
     * Lấy lịch thi theo mã lịch thi
     * @param maLichThi Mã lịch thi cần tìm
     * @return Lịch thi tìm thấy hoặc null nếu không tìm thấy
     * @throws IllegalArgumentException Nếu mã lịch thi không hợp lệ
     * @throws SQLException Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public LichThiDTO layLichThi(String maLichThi) throws IllegalArgumentException, SQLException {
        if (maLichThi == null || maLichThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }
        return lichThiDAO.findById(maLichThi);
    }

    /**
     * Lấy danh sách lịch thi theo mã chứng chỉ
     * @param maChungChi Mã chứng chỉ cần tìm
     * @return Danh sách lịch thi của chứng chỉ
     * @throws IllegalArgumentException Nếu mã chứng chỉ không hợp lệ
     * @throws SQLException Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public List<LichThiDTO> layLichThiTheoChungChi(String maChungChi) throws IllegalArgumentException, SQLException {
        if (maChungChi == null || maChungChi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã chứng chỉ không được để trống");
        }
        return lichThiDAO.findByChungChi(maChungChi);
    }

    /**
     * Tạo lịch thi mới
     * @param lichThi Thông tin lịch thi cần tạo
     * @throws IllegalArgumentException Nếu thông tin lịch thi không hợp lệ
     * @throws SQLException Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public void taoLichThi(LichThiDTO lichThi) throws IllegalArgumentException, SQLException {
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

        lichThiDAO.insert(lichThi);
    }

    /**
     * Cập nhật thông tin lịch thi
     * @param lichThi Thông tin lịch thi cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không
     * @throws IllegalArgumentException Nếu thông tin lịch thi không hợp lệ
     * @throws SQLException Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public boolean capNhatLichThi(LichThiDTO lichThi) throws IllegalArgumentException, SQLException {
        if (lichThi == null) {
            throw new IllegalArgumentException("Thông tin lịch thi không được để trống");
        }

        if (lichThi.getMaLichThi() == null || lichThi.getMaLichThi().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        // Check if exam schedule exists
        LichThiDTO existingLichThi = lichThiDAO.findById(lichThi.getMaLichThi());
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

        return lichThiDAO.update(lichThi) > 0;
    }

    /**
     * Xóa lịch thi
     * @param maLichThi Mã lịch thi cần xóa
     * @return true nếu xóa thành công, false nếu không
     * @throws IllegalArgumentException Nếu mã lịch thi không hợp lệ
     * @throws SQLException Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public boolean xoaLichThi(String maLichThi) throws IllegalArgumentException, SQLException {
        if (maLichThi == null || maLichThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        // Check if the exam schedule exists
        LichThiDTO existingLichThi = lichThiDAO.findById(maLichThi);
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

        return lichThiDAO.delete(maLichThi) > 0;
    }

    /**
     * Kiểm tra xem lịch thi có còn chỗ không
     * @param maLichThi Mã lịch thi cần kiểm tra
     * @return true nếu còn chỗ, false nếu đã đầy
     * @throws IllegalArgumentException Nếu mã lịch thi không hợp lệ
     * @throws SQLException Nếu có lỗi truy vấn cơ sở dữ liệu
     */
    public boolean kiemTraConCho(String maLichThi) throws IllegalArgumentException, SQLException {
        if (maLichThi == null || maLichThi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch thi không được để trống");
        }

        LichThiDTO lichThi = lichThiDAO.findById(maLichThi);
        if (lichThi == null) {
            throw new IllegalArgumentException("Lịch thi với mã " + maLichThi + " không tồn tại");
        }

        return lichThi.getSoLuongHienTai() < lichThi.getSoLuongToiDa();
    }
}
