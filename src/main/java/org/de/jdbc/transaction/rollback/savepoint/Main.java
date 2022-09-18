package org.de.jdbc.transaction.rollback.savepoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.de.jdbc.mapper.ResultSetMapper;

public class Main {

    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", null);
        try {
            con.setAutoCommit(false);
            // default is REPEATABLE READ
            PreparedStatement updateStmt = con.prepareStatement("UPDATE product SET `price` = `price`+10000 where `id` = ?");
            PreparedStatement selectStmt = con.prepareStatement(
                    "select `id`, `name`, `updated_at`, `contents`, `price` from product where id = ?");

            System.out.println("==== Before Start Update ====");
            selectAndPrintRow(selectStmt, 1);
            selectAndPrintRow(selectStmt, 2);
            selectAndPrintRow(selectStmt, 3);

            updateStmt.setInt(1, 1);
            updateStmt.executeUpdate();
            Savepoint sp1 = con.setSavepoint();
            updateStmt.setInt(1, 2);
            updateStmt.executeUpdate();
            Savepoint sp2 = con.setSavepoint();
            updateStmt.setInt(1, 3);
            updateStmt.executeUpdate();

            System.out.println();
            System.out.println("==== After Update in Transaction ====");
            selectAndPrintRow(selectStmt, 1);
            System.out.println("save point 1 here");
            selectAndPrintRow(selectStmt, 2);
            System.out.println("save point 2 here");
            selectAndPrintRow(selectStmt, 3);

            con.rollback(sp2);

            System.out.println();
            System.out.println("==== After Rollback to savepoint2 in Transaction ====");
            selectAndPrintRow(selectStmt, 1);
            selectAndPrintRow(selectStmt, 2);
            selectAndPrintRow(selectStmt, 3);


            con.rollback(sp1); // error

            System.out.println();
            System.out.println("==== After Rollback to savepoint1 in Transaction ====");
            selectAndPrintRow(selectStmt, 1);
            selectAndPrintRow(selectStmt, 2);
            selectAndPrintRow(selectStmt, 3);

            con.close();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getErrorCode() + ", " + sqlException.getMessage());
        }
    }

    private static void selectAndPrintRow(PreparedStatement stmt, int id) throws SQLException {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            ResultSetMapper.printRs(rs);
        }
    }
}