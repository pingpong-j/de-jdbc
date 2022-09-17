package org.de.jdbc.statement.batch;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try {
            //here de-jdbc is database name, root is username and password is null. Fix them for your database settings.
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/de-jdbc", "root", null);
            Statement stmt = con.createStatement();
            stmt.addBatch(
                    "select `id`, `name`, `updated_at`, `contents`, `price` from product where id between 1 and 5;");
            stmt.addBatch("UPDATE product SET `price` = `price`+10000 where `id` = 1;");
            try {
                int[] results = stmt.executeBatch();
            } catch(BatchUpdateException batchUpdateException) {
                System.out.println(batchUpdateException.getMessage());
            }

            stmt.addBatch("UPDATE product SET `price` = `price`+10000 where `id` = 1;");
            stmt.addBatch("UPDATE product SET `price` = `price`+10000 where `id` = 2;");
            stmt.addBatch("UPDATE product SET `price` = `price`+10000 where `id` between 3 and 5;");
            int[] results = stmt.executeBatch();

            for (int result : results) {
                if (result >= 0) {
                    System.out.println("result of update: " + result);
                }

            }

            con.close();
        } catch (Exception e) {System.out.println(e);}
    }

    private static void printRs(ResultSet rs) throws SQLException {
        System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  "
                           + rs.getDate(3) + "  " + rs.getString(4)
                           + "  " + rs.getInt(5));
    }
}
