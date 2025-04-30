package com.hcmus.exammanagement.bus;

import com.hcmus.exammanagement.dao.ChungChiDAO;
import com.hcmus.exammanagement.dto.ChungChiDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic class for ChungChi operations
 */
public class ChungChiBUS {

    public static List<ChungChiDTO> layDSChungChi() throws SQLException {
        return ChungChiDAO.findAll();
    }
}
