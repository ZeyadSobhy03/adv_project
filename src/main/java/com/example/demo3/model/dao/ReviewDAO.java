package com.example.demo3.model.dao;

import com.example.demo3.model.config.DatabaseConfig;
import com.example.demo3.model.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

	public List<Review> findByProductId(int productId) throws SQLException {
		List<Review> reviews = new ArrayList<>();
		String sql = "SELECT id, product_id, username, comment, rating FROM reviews WHERE product_id = ? ORDER BY created_at DESC";

		try (Connection conn = DatabaseConfig.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, productId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Review review = new Review(
							rs.getInt("id"),
							rs.getInt("product_id"),
							rs.getString("username"),
							rs.getString("comment"),
							rs.getInt("rating")
					);
					reviews.add(review);
				}
			}
		}
		return reviews;
	}

	public Review save(int productId, String username, String comment, int rating) throws SQLException {
		String sql = "INSERT INTO reviews (product_id, username, comment, rating) VALUES (?, ?, ?, ?)";

		try (Connection conn = DatabaseConfig.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, productId);
			stmt.setString(2, username);
			stmt.setString(3, comment);
			stmt.setInt(4, Math.max(1, Math.min(5, rating))); // Clamp rating between 1-5

			stmt.executeUpdate();

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int id = generatedKeys.getInt(1);
					return new Review(id, productId, username, comment, rating);
				}
			}
		}
		throw new SQLException("Failed to create review");
	}

	public double getAverageRating(int productId) throws SQLException {
		String sql = "SELECT AVG(rating) as avg_rating FROM reviews WHERE product_id = ?";

		try (Connection conn = DatabaseConfig.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, productId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					double avg = rs.getDouble("avg_rating");
					return Double.isNaN(avg) ? 0.0 : avg;
				}
			}
		}
		return 0.0;
	}

	public int getReviewCount(int productId) throws SQLException {
		String sql = "SELECT COUNT(*) as count FROM reviews WHERE product_id = ?";

		try (Connection conn = DatabaseConfig.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, productId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("count");
				}
			}
		}
		return 0;
	}
}
