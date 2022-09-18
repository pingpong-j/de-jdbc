package org.de.jdbc.transaction.isolation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.de.jdbc.mapper.ResultSetMapper;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", null);
        con.setAutoCommit(false);
        Statement stmt = con.createStatement();
        stmt.executeUpdate("UPDATE product SET `id` = 101 where `id` = 1;");

        ResultSet rs = stmt.executeQuery("select `id`, `name`, `updated_at`, `contents`, `price` from product where id = 101");
        while (rs.next()) {
            // id = 101
            ResultSetMapper.printRs(rs);
        }

        // no commit
        con.close();

        Connection con2 = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", null);
        Statement stmt2 = con2.createStatement();
        ResultSet rs2 = stmt2.executeQuery("select `id`, `name`, `updated_at`, `contents`, `price` from product where id = 1");
        while (rs2.next()) {
            // id = 1
            ResultSetMapper.printRs(rs2);
        }

    }
}