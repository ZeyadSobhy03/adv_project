package com.example.demo3.model.controller;

import com.example.demo3.model.dao.ReviewDAO;
import com.example.demo3.model.service.RateLimiter;
import com.example.demo3.model.util.AppConstants;
import com.example.demo3.model.util.ValidationUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet("/review/add")
public class ReviewServlet extends HttpServlet {
	private final ReviewDAO reviewDAO = new ReviewDAO();
	private static final RateLimiter RATE_LIMITER = new RateLimiter();
	private static final int MAX_REQUESTS = 5;
	private static final long WINDOW_MILLIS = 10_000L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String ip = req.getRemoteAddr();
		String key = "review:add:" + (ip == null ? "unknown" : ip);
		if (!RATE_LIMITER.allow(key, MAX_REQUESTS, WINDOW_MILLIS)) {
			resp.sendError(429, "Too many requests. Please wait a few seconds and try again.");
			return;
		}
		Object userObj = req.getSession(false) == null ? null : req.getSession(false).getAttribute(AppConstants.SESSION_LOGGED_IN_USER);
		if (userObj == null) {
			resp.sendRedirect(req.getContextPath() + "/login.jsp?message="
					+ java.net.URLEncoder.encode("Please login to add a review", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}
		String username = String.valueOf(userObj);

		String productIdParam = req.getParameter("productId");
		String ratingParam = req.getParameter("rating");
		String comment = req.getParameter("comment");

		int productId;
		int rating;
		try {
			productId = Integer.parseInt(productIdParam);
			rating = Integer.parseInt(ratingParam);
		} catch (Exception e) {
			resp.sendRedirect(req.getContextPath() + "/product?id=" + productIdParam + "&error="
					+ java.net.URLEncoder.encode("Invalid review data", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}

		if (rating < 1 || rating > 5) {
			resp.sendRedirect(req.getContextPath() + "/product?id=" + productId + "&error="
					+ java.net.URLEncoder.encode("Rating must be between 1 and 5", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}

		if (ValidationUtil.isBlank(comment) || comment.trim().length() < 2) {
			resp.sendRedirect(req.getContextPath() + "/product?id=" + productId + "&error="
					+ java.net.URLEncoder.encode("Comment is required", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}

		try {
			reviewDAO.save(productId, username, comment.trim(), rating);
			resp.sendRedirect(req.getContextPath() + "/product?id=" + productId + "&message="
					+ java.net.URLEncoder.encode("Review added", java.nio.charset.StandardCharsets.UTF_8));
		} catch (Exception e) {
			resp.sendRedirect(req.getContextPath() + "/product?id=" + productId + "&error="
					+ java.net.URLEncoder.encode("Failed to save review", java.nio.charset.StandardCharsets.UTF_8));
		}
	}
}

