package com.example.demo3.model.dao;

import com.example.demo3.model.config.DatabaseConfig;
import com.example.demo3.model.model.User;

import java.sql.*;
import java.util.Optional;

public class UserDAO {

	public Optional<User> findByUsername(String username) throws SQLException {
		if (username == null || username.trim().isEmpty()) {
			return Optional.empty();
		}

		String sql = "SELECT id, username, password FROM users WHERE username = ?";

		try (Connection conn = DatabaseConfig.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username.trim());
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					User user = new User(
							rs.getInt("id"),
							rs.getString("username"),
							rs.getString("password")
					);
					return Optional.of(user);
				}
			}
		}
		return Optional.empty();
	}

	public boolean existsByUsername(String username) throws SQLException {
		return findByUsername(username).isPresent();
	}

	public User save(String username, String rawPassword) throws SQLException {
		if (username == null || username.trim().isEmpty()) {
			throw new SQLException("Username cannot be empty");
		}

		String normalized = username.trim();

		// Check if user already exists
		if (existsByUsername(normalized)) {
			throw new SQLException("Username already exists");
		}

		String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

		try (Connection conn = DatabaseConfig.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, normalized);
			stmt.setString(2, rawPassword);

			stmt.executeUpdate();

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int id = generatedKeys.getInt(1);
					return new User(id, normalized, rawPassword);
				}
			}
		}
		throw new SQLException("Failed to create user");
	}

	public boolean validateCredentials(String username, String rawPassword) throws SQLException {
		Optional<User> user = findByUsername(username);
		return user.isPresent() && user.get().getPassword().equals(rawPassword);
	}

	public boolean deleteByUsername(String username) throws SQLException {
		if (username == null || username.trim().isEmpty()) {
			return false;
		}
		String sql = "DELETE FROM users WHERE username = ?";
		try (Connection conn = DatabaseConfig.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username.trim());
			return stmt.executeUpdate() > 0;
		}
	}

	public boolean isAdmin(String username) throws SQLException {
		if (username == null || username.trim().isEmpty()) {
			return false;
		}
		// expects: users.is_admin TINYINT(1) default 0
		String sql = "SELECT is_admin FROM users WHERE username = ?";
		try (Connection conn = DatabaseConfig.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username.trim());
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					try {
						return rs.getBoolean("is_admin");
					} catch (SQLException ignored) {
						// Column missing in DB
						return false;
					}
				}
			}
		}
		return false;
	}
}
