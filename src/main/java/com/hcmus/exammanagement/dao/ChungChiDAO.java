package com.hcmus.exammanagement.dao;

import com.hcmus.exammanagement.dto.ChungChiDTO;
import com.hcmus.exammanagement.dto.Database;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ChungChiDAO {

    public static List<ChungChiDTO> findAll() throws SQLException {
        List<ChungChiDTO> chungChiList = new ArrayList<>();
        String sql = "SELECT * FROM chung_chi";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ChungChiDTO chungChi = ChungChiDTO.fromResultSet(rs);
                chungChiList.add(chungChi);
            }
        } catch (Exception e) {
            log.error("Error finding all chung chi: {}", e.getMessage());
            throw e;
        }

        return chungChiList;
    }
}
