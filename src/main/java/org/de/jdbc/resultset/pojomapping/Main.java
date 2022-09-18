package org.de.jdbc.resultset.pojomapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.de.jdbc.mapper.ResultSetMapper;

public class Main {
    public static void main(String[] args) {
        try {
            //here de-jdbc is database name, root is username and password is null. Fix them for your database settings.
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/de-jdbc", "root", null);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select `id`, `name`, `updated_at`, `contents`, `price` from product");
            while (rs.next()) {
                System.out.println(ResultSetMapper.create(rs));
            }
            con.close();
        } catch (Exception e) {System.out.println(e);}
    }
}
