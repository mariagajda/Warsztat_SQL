package pl.coderslab.entity;

import java.sql.*;

public class DbUtil {
    private static final String DB_URL_WORKSHOP2 = "jdbc:mysql://localhost:3306/workshop2?useSSL=false&characterEncoding=utf8&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "coderslab";

    public static Connection connectWorkshop2() throws SQLException {
        return DriverManager.getConnection(DB_URL_WORKSHOP2, DB_USER, DB_PASS);
    }


}


