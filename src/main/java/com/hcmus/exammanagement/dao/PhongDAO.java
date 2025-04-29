package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.Database;
import com.hcmus.exammanagement.dto.PhongDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PhongDAO {

    public List<PhongDTO> findAll() throws SQLException {
        List<PhongDTO> phongList = new ArrayList<>();
        String sql = "SELECT * FROM phong";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                phongList.add(PhongDTO.fromResultSet(rs));
            }

        } catch (SQLException e) {
            log.error("Error finding all phong: {}", e.getMessage());
            throw e;
        }

        return phongList;
    }

    public PhongDTO findById(String maPhong) throws SQLException {
        String sql = "SELECT * FROM phong WHERE ma_phong = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhong);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return PhongDTO.fromResultSet(rs);
            }

        } catch (SQLException e) {
            log.error("Error finding phong by ID {}: {}", maPhong, e.getMessage());
            throw e;
        }

        return null;
    }

    public int insert(PhongDTO phong) throws SQLException {
        String sql = "INSERT INTO phong (ten_phong, so_ghe) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phong.getTenPhong());
            stmt.setString(2, phong.getSoGhe());

            return stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Error inserting phong: {}", e.getMessage());
            throw e;
        }
    }

    public int update(PhongDTO phong) throws SQLException {
        String sql = "UPDATE phong SET ten_phong = ?, so_ghe = ? WHERE ma_phong = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phong.getTenPhong());
            stmt.setString(2, phong.getSoGhe());
            stmt.setString(3, phong.getMaPhong());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating phong with ID {}: {}", phong.getMaPhong(), e.getMessage());
            throw e;
        }
    }

    public int delete(String maPhong) throws SQLException {
        String sql = "DELETE FROM phong WHERE ma_phong = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhong);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Error deleting phong with ID {}: {}", maPhong, e.getMessage());
            throw e;
        }
    }
}
