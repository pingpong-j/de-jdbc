package org.de.jdbc.resultset.methods;

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
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(
                    "select `id`, `name`, `updated_at`, `contents`, `price` from product where id between 1 and 5 order by id asc");
            // cursor init
            if (rs.next()) {
                // cursor 1
                printRs(rs);
            }
            if (rs.last()) {
                // cursor 5
                printRs(rs);
            }
            if (!rs.next()) {
                // end of result set
                try {
                    printRs(rs);
                } catch (SQLException sqlException) {
                    System.out.println(sqlException.getErrorCode() + ", " + sqlException.getMessage());
                }
            }
            rs.last();
            if (rs.previous()) {
                // cursor 4
                printRs(rs);
            }
            if (rs.absolute(2)) {
                // cursor 2
                printRs(rs);
            }
            if (rs.relative(2)) {
                // cursor 4
                printRs(rs);
            }
            if (rs.relative(-2)) {
                // cursor 2
                printRs(rs);
            }
            if (rs.first()) {
                // cursor 1
                printRs(rs);
            }
            if (!rs.previous()) {
                // Before start of result set
                try {
                    printRs(rs);
                } catch (SQLException sqlException) {
                    System.out.println(sqlException.getErrorCode() + ", " + sqlException.getMessage());
                }
            }
            if (!rs.previous()) {
                // Before start of result set
                try {
                    printRs(rs);
                } catch (SQLException sqlException) {
                    System.out.println(sqlException.getErrorCode() + ", " + sqlException.getMessage());
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
