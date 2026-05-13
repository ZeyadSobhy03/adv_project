package com.example.demo3.model.controller;

import com.example.demo3.model.model.Product;
import com.example.demo3.model.service.CartService;
import com.example.demo3.model.service.ProductService;
import com.example.demo3.model.service.RateLimiter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cart/add")
public class CartAddServlet extends HttpServlet {
	private final ProductService productService = new ProductService();
	private static final RateLimiter RATE_LIMITER = new RateLimiter();
	private static final int MAX_REQUESTS = 5;
	private static final long WINDOW_MILLIS = 10_000L;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String ip = req.getRemoteAddr(); // get ip of user
		String key = "cart:add:" + (ip == null ? "unknown" : ip);
		if (!RATE_LIMITER.allow(key, MAX_REQUESTS, WINDOW_MILLIS)) {
			resp.sendError(429, "Too many requests. Please wait a few seconds and try again.");
			return;
		}
		String idParam = req.getParameter("id");
		if (idParam == null || idParam.trim().isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/products");
			return;
		}
		int id;
		try {
			id = Integer.parseInt(idParam);
		} catch (NumberFormatException e) {
			resp.sendRedirect(req.getContextPath() + "/products");
			return;
		}

		try {
			List<Product> products = productService.listProducts();
			Product found = null;
			for (Product p : products) {
				if (p != null && p.getId() == id) {
					found = p;
					break;
				}
			}
			if (found != null) {
				CartService.addToCart(req.getSession(true), found);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// ignore and redirect
		}

		resp.sendRedirect(req.getContextPath() + "/cart");
	}
}

