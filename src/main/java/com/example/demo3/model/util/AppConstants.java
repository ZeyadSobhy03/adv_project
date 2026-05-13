package com.example.demo3.model.util;

public final class AppConstants {
	private AppConstants() {
	}

	// Session keys
	public static final String SESSION_LOGGED_IN_USER = "loggedInUser";

	// JWT
	public static final String JWT_COOKIE_NAME = "AUTH_TOKEN";
	public static final String JWT_SECRET = "secret-change-me";
	public static final long JWT_TTL_SECONDS = 60L * 60L; // 1 hour


}

