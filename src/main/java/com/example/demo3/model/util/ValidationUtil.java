package com.example.demo3.model.util;

public final class ValidationUtil {
	private static final int MIN_PASSWORD_LENGTH = 6;

	private ValidationUtil() {
	}

	public static boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	public static boolean isValidUsername(String username) {
		if (isBlank(username)) {
			return false;
		}
		return username.trim().matches("[A-Za-z0-9_]{3,30}");
	}

	public static boolean isValidPassword(String password) {
		if (isBlank(password)) {
			return false;
		}
		return password.length() >= MIN_PASSWORD_LENGTH;
	}
}
