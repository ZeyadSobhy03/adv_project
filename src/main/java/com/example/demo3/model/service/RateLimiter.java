package com.example.demo3.model.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {
	private final Map<String, WindowState> windows = new ConcurrentHashMap<>();

	public boolean allow(String key, int maxRequests, long windowMillis) {
		if (key == null || key.trim().isEmpty()) {
			return false;
		}
		long now = System.currentTimeMillis();
		WindowState state = windows.computeIfAbsent(key, ignored -> new WindowState(now));
		synchronized (state) {
			if (now - state.windowStartMillis >= windowMillis) {
				state.windowStartMillis = now;
				state.count = 0;
			}
			if (state.count >= maxRequests) {
				return false;
			}
			state.count++;
			return true;
		}
	}

	private static final class WindowState {
		private long windowStartMillis;
		private int count;

		private WindowState(long windowStartMillis) {
			this.windowStartMillis = windowStartMillis;
			this.count = 0;
		}
	}
}
