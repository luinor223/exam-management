package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.GiamThiDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GiamThiDAO {

    public List<GiamThiDTO> findAll() throws SQLException {
        List<GiamThiDTO> giamThiList = new ArrayList<>();
        String sql = "SELECT * FROM giam_thi";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                giamThiList.add(GiamThiDTO.fromResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("Error finding all giam thi: {}", e.getMessage());
            throw e;
        }

        return giamThiList;
    }

    public GiamThiDTO findById(String maGiamThi) throws SQLException {
        String sql = "SELECT * FROM giam_thi WHERE ma_gt = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maGiamThi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return GiamThiDTO.fromResultSet(rs);
            }

        } catch (SQLException e) {
            log.error("Error finding giam thi by ID {}: {}", maGiamThi, e.getMessage());
            throw e;
        }

        return null;
    }

    public int insert(GiamThiDTO giamThi) throws SQLException {
        String sql = "INSERT INTO giam_thi (ho_ten, sdt, email) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, giamThi.getHoTen());
                stmt.setString(2, giamThi.getSdt());
                stmt.setString(3, giamThi.getEmail());

                return stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Error inserting giam thi: {}", e.getMessage());
            throw e;
        }
    }

    public int update(GiamThiDTO giamThi) throws SQLException {
        String sql = "UPDATE giam_thi SET ho_ten = ?, sdt = ?, email = ? WHERE ma_gt = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, giamThi.getHoTen());
            stmt.setString(2, giamThi.getSdt());
            stmt.setString(3, giamThi.getEmail());
            stmt.setString(4, giamThi.getMaGiamThi());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating giam thi with ID {}: {}", giamThi.getMaGiamThi(), e.getMessage());
            throw e;
        }
    }

    public int delete(String maGiamThi) throws SQLException {
        String sql = "DELETE FROM giam_thi WHERE ma_gt = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maGiamThi);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting giam thi with ID {}: {}", maGiamThi, e.getMessage());
            throw e;
        }
    }
}
