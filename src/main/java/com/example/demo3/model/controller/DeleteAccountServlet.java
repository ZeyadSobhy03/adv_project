package com.example.demo3.model.controller;

import com.example.demo3.model.dao.UserDAO;
import com.example.demo3.model.service.RateLimiter;
import com.example.demo3.model.util.AppConstants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/account/delete")
public class DeleteAccountServlet extends HttpServlet {
	private final UserDAO userDAO = new UserDAO();
	private static final RateLimiter RATE_LIMITER = new RateLimiter();
	private static final int MAX_REQUESTS = 5;
	private static final long WINDOW_MILLIS = 10_000L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String ip = req.getRemoteAddr();
		String key = "account:delete:" + (ip == null ? "unknown" : ip);
		if (!RATE_LIMITER.allow(key, MAX_REQUESTS, WINDOW_MILLIS)) {
			resp.sendError(429, "Too many requests. Please wait a few seconds and try again.");
			return;
		}
		Object userObj = req.getSession(false) == null ? null : req.getSession(false).getAttribute(AppConstants.SESSION_LOGGED_IN_USER);
		if (userObj == null) {
			resp.sendRedirect(req.getContextPath() + "/login.jsp?message="
					+ java.net.URLEncoder.encode("Please login first", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}
		String username = String.valueOf(userObj);

		try {
			boolean deleted = userDAO.deleteByUsername(username);
			// invalidate session either way
			if (req.getSession(false) != null) {
				req.getSession(false).invalidate();
			}
			// Clear JWT cookie
			Cookie jwtCookie = new Cookie(AppConstants.JWT_COOKIE_NAME, "");
			jwtCookie.setHttpOnly(true);
			jwtCookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
			jwtCookie.setMaxAge(0);
			resp.addCookie(jwtCookie);

			if (deleted) {
				resp.sendRedirect(req.getContextPath() + "/register.jsp?message="
						+ java.net.URLEncoder.encode("Account deleted", java.nio.charset.StandardCharsets.UTF_8));
			} else {
				resp.sendRedirect(req.getContextPath() + "/products?error="
						+ java.net.URLEncoder.encode("Account not found", java.nio.charset.StandardCharsets.UTF_8));
			}
		} catch (Exception e) {
			resp.sendRedirect(req.getContextPath() + "/products?error="
					+ java.net.URLEncoder.encode("Failed to delete account", java.nio.charset.StandardCharsets.UTF_8));
		}
	}
}

