package com.example.demo3.model.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

import com.example.demo3.model.service.RateLimiter;
import com.example.demo3.model.service.RedisCacheService;
import com.example.demo3.model.util.AppConstants;
import com.example.demo3.model.util.JwtUtil;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
  private static final RateLimiter RATE_LIMITER = new RateLimiter();
	private final RedisCacheService redisCacheService = new RedisCacheService();
  private static final int MAX_REQUESTS = 5;
  private static final long WINDOW_MILLIS = 10_000L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String ip = req.getRemoteAddr();
		String key = "logout:" + (ip == null ? "unknown" : ip);
		if (!RATE_LIMITER.allow(key, MAX_REQUESTS, WINDOW_MILLIS)) {
			resp.sendError(429, "Too many requests. Please wait a few seconds and try again.");
			return;
		}
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

    // Revoke JWT in Redis until it expires, then clear cookie
    String token = null;
    Cookie[] cookies = req.getCookies();
    if (cookies != null) {
      for (Cookie c : cookies) {
        if (AppConstants.JWT_COOKIE_NAME.equals(c.getName())) {
          token = c.getValue();
          break;
        }
      }
    }
    if (token != null && !token.trim().isEmpty()) {
      Optional<Long> exp = JwtUtil.extractExpiresAtMillis(token);
      if (exp.isPresent()) {
        long ttlMillis = exp.get() - System.currentTimeMillis();
        if (ttlMillis > 0) {
          redisCacheService.revokeJwt(token, Duration.ofMillis(ttlMillis));
        }
      }

      Cookie jwtCookie = new Cookie(AppConstants.JWT_COOKIE_NAME, "");
      jwtCookie.setHttpOnly(true);
      jwtCookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
      jwtCookie.setMaxAge(0);
      resp.addCookie(jwtCookie);
    }


        resp.sendRedirect(req.getContextPath() + "/login.jsp?message="
                + java.net.URLEncoder.encode("Logged out successfully", java.nio.charset.StandardCharsets.UTF_8));
    }
}
