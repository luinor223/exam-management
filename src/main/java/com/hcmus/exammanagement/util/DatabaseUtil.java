package com.hcmus.exammanagement.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtil {
    private static final String URL = ConfigUtil.get("db.url");
    private static final String USER = ConfigUtil.get("db.user");
    private static final String PASSWORD = ConfigUtil.get("db.password");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
