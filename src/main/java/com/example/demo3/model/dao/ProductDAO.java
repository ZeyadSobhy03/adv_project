package com.example.demo3.model.dao;

import com.example.demo3.model.config.DatabaseConfig;
import com.example.demo3.model.model.Product;

import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

	public List<Product> findAll() throws SQLException, ClassNotFoundException {
		String sql = "SELECT id, item, price FROM product_cards";
		List<Product> products = new ArrayList<>();
		try (Connection conn = DatabaseConfig.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql);
		     ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				products.add(new Product(
						rs.getInt("id"),
						rs.getString("item"),
						rs.getFloat("price")
				));
			}
		}
		return products;
	}

	public Product add(String name, float price) throws SQLException {
		String sql = "INSERT INTO product_cards (item, price) VALUES (?, ?)";
		try (Connection conn = DatabaseConfig.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, name);
			stmt.setFloat(2, price);
			stmt.executeUpdate();
			try (ResultSet keys = stmt.getGeneratedKeys()) {
				if (keys.next()) {
					int id = keys.getInt(1);
					return new Product(id, name, price);
				}
			}
		}
		throw new SQLException("Failed to add product");
	}

	public boolean deleteById(int id) throws SQLException {
		String sql = "DELETE FROM product_cards WHERE id = ?";
		try (Connection conn = DatabaseConfig.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			boolean deleted = stmt.executeUpdate() > 0;
			return deleted;
		}
	}
}
