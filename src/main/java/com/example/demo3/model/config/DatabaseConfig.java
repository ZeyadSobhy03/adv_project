package com.example.demo3.model.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConfig {
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException ignored) {
		}
	}

	private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/products?useSSL=false";
	private static final String DEFAULT_USER = "root";
	private static final String DEFAULT_PASSWORD = "root";

	private DatabaseConfig() {
	}

	public static String getUrl() {
		return getEnvOrDefault("APP_DB_URL", DEFAULT_URL);
	}

	public static String getUser() {
		return getEnvOrDefault("APP_DB_USER", DEFAULT_USER);
	}

	public static String getPassword() {
		return getEnvOrDefault("APP_DB_PASSWORD", DEFAULT_PASSWORD);
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(getUrl(), getUser(), getPassword());
	}
// that get port number
	private static String getEnvOrDefault(String key, String fallback) {
		String value = System.getenv(key);
		if (value == null || value.trim().isEmpty()) {
			return fallback;
		}
		return value;
	}
}
