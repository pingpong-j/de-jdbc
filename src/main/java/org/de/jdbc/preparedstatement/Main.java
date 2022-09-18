package org.de.jdbc.preparedstatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.de.jdbc.mapper.Product;
import org.de.jdbc.mapper.ResultSetMapper;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/de-jdbc", "root", null);
        PreparedStatement psUpdate = con.prepareStatement(
                "UPDATE product SET `price` = price+? where `id` = ?;");
        psUpdate.setInt(1, 10000); // price + a
        psUpdate.setInt(2, 1); // id

        int updatedResult = psUpdate.executeUpdate();
        System.out.println("result of update: " + updatedResult);

        PreparedStatement psSelect = con.prepareStatement(
                "select `id`, `name`, `updated_at`, `contents`, `price` from product where id between ? and ?;");
        psSelect.setInt(1, 1);
        psSelect.setInt(2, 5);
        ResultSet rs = psSelect.executeQuery();

        while (rs.next()) {
            ResultSetMapper.printRs(rs);
        }

        psSelect.setInt(1, 6);
        psSelect.setInt(2, 10);
        ResultSet rs2 = psSelect.executeQuery();

        List<Product> productList = new ArrayList<>();
        while (rs2.next()) {
            productList.add(ResultSetMapper.create(rs2));
        }

        PreparedStatement psUpdateProduct = con.prepareStatement(
                "UPDATE product SET `id` = ?, `name` = ?, `updated_at` = ?, `contents` = ?, price = ? where id = ?");
        for (Product product : productList) {

            product.setPrice(product.getPrice() - 1000);
            product.setUpdated_at(LocalDateTime.now());

            psUpdateProduct.setInt(1, product.getId());
            psUpdateProduct.setString(2, product.getName());
            psUpdateProduct.setTimestamp(3, Timestamp.valueOf(product.getUpdated_at()));
            psUpdateProduct.setString(4, product.getContents());
            psUpdateProduct.setInt(5, product.getPrice());
            psUpdateProduct.setInt(6, product.getId());

            psUpdateProduct.addBatch();
            psUpdateProduct.clearParameters();
        }

        int[] results = psUpdateProduct.executeBatch();
        for (int result : results) {
            System.out.println("result of update: " + result);
        }
    }
}
