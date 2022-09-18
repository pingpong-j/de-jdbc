package org.de.jdbc.callablestatement.procedurecall;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

public class Main {
    public static void main(String[] args) throws SQLException {

        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", null);
        CallableStatement stmtProcedureCall = con.prepareCall("call discount_price(?, ?, ?);");
        stmtProcedureCall.setInt(1, 1);
        stmtProcedureCall.setInt(2, 10);
        stmtProcedureCall.registerOutParameter(3, Types.INTEGER);
        boolean result = stmtProcedureCall.execute();
        System.out.println("result: " + result);
        System.out.println("param: " + stmtProcedureCall.getInt(3));
        if (!result) {
            System.out.println("update count: " + stmtProcedureCall.getUpdateCount());
        }
    }
}
