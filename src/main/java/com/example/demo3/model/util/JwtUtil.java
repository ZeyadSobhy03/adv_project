package com.example.demo3.model.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public final class JwtUtil {
	private JwtUtil() {
	}


	public static Optional<Long> extractExpiresAtMillis(String token) {
		if (ValidationUtil.isBlank(token)) {
			return Optional.empty();
		}
		String[] parts = token.split("\\.");
		if (parts.length != 2) {
			return Optional.empty();
		}
		try {
			String payload = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
			String[] payloadParts = payload.split(":", 2);
			if (payloadParts.length != 2) {
				return Optional.empty();
			}
			return Optional.of(Long.parseLong(payloadParts[1]));
		} catch (Exception ignored) {
			return Optional.empty();
		}
	}

	public static String issueToken(String username, long ttlSeconds, String secret) {
		long expiresAt = System.currentTimeMillis() + (ttlSeconds * 1000L);
		String payload = username + ":" + expiresAt;
		String payloadPart = Base64.getUrlEncoder().withoutPadding()
				.encodeToString(payload.getBytes(StandardCharsets.UTF_8));
		String signature = sign(payloadPart, secret);
		return payloadPart + "." + signature;
	}

	public static Optional<String> verifyAndGetUsername(String token, String secret) {
		if (ValidationUtil.isBlank(token) || ValidationUtil.isBlank(secret)) {
			return Optional.empty();
		}
		String[] parts = token.split("\\.");
		if (parts.length != 2) {
			return Optional.empty();
		}

		String payloadPart = parts[0];
		String actualSignature = parts[1];
		String expectedSignature = sign(payloadPart, secret);
		if (!expectedSignature.equals(actualSignature)) {
			return Optional.empty();
		}

		String payload = new String(Base64.getUrlDecoder().decode(payloadPart), StandardCharsets.UTF_8);
		String[] payloadParts = payload.split(":", 2);
		if (payloadParts.length != 2) {
			return Optional.empty();
		}

		String username = payloadParts[0];
		long expiresAt;
		try {
			expiresAt = Long.parseLong(payloadParts[1]);
		} catch (NumberFormatException ignored) {
			return Optional.empty();
		}

		if (System.currentTimeMillis() > expiresAt) {
			return Optional.empty();
		}
		return Optional.of(username);
	}

	private static String sign(String content, String secret) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			mac.init(keySpec);
			byte[] signatureBytes = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to sign token", ex);
		}
	}
}
