package com.example.demo3.model.service;

import com.example.demo3.model.config.RedisConfig;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;
import java.time.Duration;


public final class RedisCacheService {
	public static final String KEY_PREFIX_REVOKED_JWT = "revoked:jwt:";

	public boolean isJwtRevoked(String token) {
		if (token == null || token.trim().isEmpty()) {
			return false;
		}
		try (Jedis jedis = RedisConfig.createClient()) {
			return jedis.exists((KEY_PREFIX_REVOKED_JWT + token).getBytes(StandardCharsets.UTF_8));
		} catch (Exception ignored) {
			return false;
		}
	}

	public void revokeJwt(String token, Duration ttlUntilExpiry) {
		if (token == null || token.trim().isEmpty()) {
			return;
		}
		long seconds = ttlUntilExpiry == null ? 0 : ttlUntilExpiry.getSeconds();
		if (seconds <= 0) {
			return;
		}
		try (Jedis jedis = RedisConfig.createClient()) {
			byte[] key = (KEY_PREFIX_REVOKED_JWT + token).getBytes(StandardCharsets.UTF_8);
			jedis.setex(key, (int) Math.min(Integer.MAX_VALUE, seconds), "1".getBytes(StandardCharsets.UTF_8));
		} catch (Exception ignored) {
			// Fail open
		}
	}
}

