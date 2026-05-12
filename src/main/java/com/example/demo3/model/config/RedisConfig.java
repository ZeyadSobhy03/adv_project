package com.example.demo3.model.config;

import redis.clients.jedis.Jedis;

public final class RedisConfig {

	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 6379;

	private RedisConfig() {
	}

	public static String getHost() {
		String host = System.getenv("APP_REDIS_HOST");
		if (host == null || host.trim().isEmpty()) {
			return DEFAULT_HOST;
		}
		return host;
	}

	public static int getPort() {
		String value = System.getenv("APP_REDIS_PORT");
		if (value == null || value.trim().isEmpty()) {
			return DEFAULT_PORT;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ignored) {
			return DEFAULT_PORT;
		}
	}

	public static Jedis createClient() {
		return new Jedis(getHost(), getPort());
	}
}
