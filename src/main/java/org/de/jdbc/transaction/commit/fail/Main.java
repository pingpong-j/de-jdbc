package org.de.jdbc.transaction.commit.fail;

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
        try {
            con.setAutoCommit(false);
            // default is REPEATABLE READ
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "select `id`, `name`, `updated_at`, `contents`, `price` from product where id = 1");
            while (rs.next()) {
                System.out.println("==== Before start update ======");
                ResultSetMapper.printRs(rs);
            }
            stmt.executeUpdate("UPDATE product SET `price` = `price`+10000 where `id` = 1");
            stmt.executeUpdate("UPDATE product SET `price` = `price`+10000 where `id` = 2");
            stmt.executeUpdate("UPDATE product SET `price` = `price`+10000 where `id` = 3");
            stmt.executeUpdate("DELETE from product where id = 1");

            con.commit();
            // no commit
            con.close();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getErrorCode() + ", " + sqlException.getMessage());
        }

        System.out.println("==== After commit failed ==== ");
        Connection con2 = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", null);
        Statement stmt2 = con2.createStatement();
        ResultSet rs2 = stmt2.executeQuery(
                "select `id`, `name`, `updated_at`, `contents`, `price` from product where id = 1");
        while (rs2.next()) {
            ResultSetMapper.printRs(rs2);
        }
        con2.close();
    }
}