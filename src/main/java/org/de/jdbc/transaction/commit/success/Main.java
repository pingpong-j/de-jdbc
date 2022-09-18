package org.de.jdbc.transaction.commit.success;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", null);
        con.setAutoCommit(false);
        Statement stmt = con.createStatement();
        stmt.executeUpdate("UPDATE product SET `price` = `price`+10000 where `id` = 1");
        stmt.executeUpdate("UPDATE product SET `price` = `price`+10000 where `id` = 2");
        stmt.executeUpdate("UPDATE product SET `price` = `price`+10000 where `id` = 3");
        stmt.executeUpdate("DELETE from review where id = 1");
        stmt.executeUpdate("DELETE from product where id = 1");

        con.commit();
        con.close();

        Connection con2 = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", null);
        Statement stmt2 = con2.createStatement();
        ResultSet rs2 = stmt2.executeQuery("select `id`, `name`, `updated_at`, `contents`, `price` from product where id = 1");
        System.out.println("exist result?: " + rs2.next());
    }
}