package com.example.demo3.model.controller;

import com.example.demo3.model.dao.UserDAO;
import com.example.demo3.model.service.AuthService;
import com.example.demo3.model.service.RateLimiter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.example.demo3.model.util.ValidationUtil.isBlank;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private final UserDAO userDAO = new UserDAO();
	private final AuthService authService = new AuthService(userDAO);
	private static final RateLimiter RATE_LIMITER = new RateLimiter();
	private static final int MAX_REQUESTS = 5;
	private static final long WINDOW_MILLIS = 10_000L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().println("Send username and password using POST /register");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String ip = req.getRemoteAddr();
		String key = "register:" + (ip == null ? "unknown" : ip);
		if (!RATE_LIMITER.allow(key, MAX_REQUESTS, WINDOW_MILLIS)) {
			resp.sendError(429, "Too many requests. Please wait a few seconds and try again.");
			return;
		}

		String username = req.getParameter("username");
		String password = req.getParameter("password");

		if (isBlank(username) || isBlank(password)) {
			resp.sendRedirect(req.getContextPath() + "/register.jsp?error=" + java.net.URLEncoder.encode("Username and password are required", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}

		try {
			boolean ok = authService.register(username, password);
			if (!ok) {
				resp.sendRedirect(req.getContextPath() + "/register.jsp?error=" + java.net.URLEncoder.encode("Registration failed", java.nio.charset.StandardCharsets.UTF_8));
				return;
			}

			req.getSession(true).setAttribute("loggedInUser", username);
			resp.sendRedirect(req.getContextPath() + "/products");

		} catch (Exception e) {
			resp.sendRedirect(req.getContextPath() + "/register.jsp?error=" + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8));
		}
	}
}
