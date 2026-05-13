package com.example.demo3.model.controller;

import com.example.demo3.model.dao.ProductDAO;
import com.example.demo3.model.model.Product;
import com.example.demo3.model.service.RateLimiter;
import com.example.demo3.model.util.ValidationUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/products")
public class AdminProductsServlet extends HttpServlet {
	private final ProductDAO productDAO = new ProductDAO();
	private static final RateLimiter RATE_LIMITER = new RateLimiter();
	private static final int MAX_REQUESTS = 5;
	private static final long WINDOW_MILLIS = 10_000L;
// get products
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ip = req.getRemoteAddr();
		String key = "admin:products:get:" + (ip == null ? "unknown" : ip); // that make key for ip
		if (!RATE_LIMITER.allow(key, MAX_REQUESTS, WINDOW_MILLIS)) {
			resp.sendError(429, "Too many requests. Please wait a few seconds and try again."); // that show message error when user make many request
			return;
		}
		try {
			List<Product> products = productDAO.findAll(); // get product list of admin
			req.setAttribute("products", products);
		} catch (Exception e) {
			req.setAttribute("products", java.util.Collections.emptyList()); // if error show empty list
			req.setAttribute("error", "Failed to load products"); // error when failed to load products
		}

		RequestDispatcher dispatcher = req.getRequestDispatcher("/adminProducts.jsp"); //after get data forward to adminProducts.jsp
		dispatcher.forward(req, resp); // that  use to forward request and response to adminProducts.jsp  that url not change
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String ip = req.getRemoteAddr(); // get ip of user
		String key = "admin:products:post:" + (ip == null ? "unknown" : ip);
		if (!RATE_LIMITER.allow(key, MAX_REQUESTS, WINDOW_MILLIS)) {
			resp.sendError(429, "Too many requests. Please wait a few seconds and try again.");
			return;
		}
		String action = req.getParameter("action"); // that get action from form that user submit like add or delete
		// that check the field not empty  when is empty that go ti admin product
		if (ValidationUtil.isBlank(action)) {
			resp.sendRedirect(req.getContextPath() + "/admin/products");
			return;
		}

		switch (action) {
			case "add":
				handleAdd(req, resp);  // when admin add product
				return;
			case "delete":
				handleDelete(req, resp); // when admin delete product
				return;
			default:  // error occur
				resp.sendRedirect(req.getContextPath() + "/admin/products?error="
						+ java.net.URLEncoder.encode("Unknown action", java.nio.charset.StandardCharsets.UTF_8));
		}
	}
// add product
	private void handleAdd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String name = req.getParameter("name");
		String priceParam = req.getParameter("price");

		// check name and price is not empty

		if (ValidationUtil.isBlank(name) || ValidationUtil.isBlank(priceParam)) {
			resp.sendRedirect(req.getContextPath() + "/admin/products?error="
					+ java.net.URLEncoder.encode("Name and price are required", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}

		float price;
		try {
			price = Float.parseFloat(priceParam); // convert float to string
		} catch (NumberFormatException e) {
			resp.sendRedirect(req.getContextPath() + "/admin/products?error="
					+ java.net.URLEncoder.encode("Invalid price", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}
// check price not negative
		if (price < 0) {
			resp.sendRedirect(req.getContextPath() + "/admin/products?error="
					+ java.net.URLEncoder.encode("Price must be >= 0", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}

		String safeName = name.trim(); // remove space from name of product
		// check the name of product length between 2 and 100 character
		if (safeName.length() < 2 || safeName.length() > 100) {
			resp.sendRedirect(req.getContextPath() + "/admin/products?error="
					+ java.net.URLEncoder.encode("Name length must be 2-100", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}

		try {
			productDAO.add(safeName, price); // add product to list
			resp.sendRedirect(req.getContextPath() + "/admin/products?message="
					+ java.net.URLEncoder.encode("Product added", java.nio.charset.StandardCharsets.UTF_8));
		} catch (SQLException e) {
			resp.sendRedirect(req.getContextPath() + "/admin/products?error="
					+ java.net.URLEncoder.encode("Failed to add product", java.nio.charset.StandardCharsets.UTF_8));
		}
	}
// delete product
	private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String idParam = req.getParameter("id"); // get id of product
		int id;
		try {
			id = Integer.parseInt(idParam); // convert string to int
		} catch (Exception e) {
			resp.sendRedirect(req.getContextPath() + "/admin/products?error="
					+ java.net.URLEncoder.encode("Invalid product id", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}

		try {
			boolean deleted = productDAO.deleteById(id);
			resp.sendRedirect(req.getContextPath() + "/admin/products?message="
					+ java.net.URLEncoder.encode(deleted ? "Product deleted" : "Product not found", java.nio.charset.StandardCharsets.UTF_8));
		} catch (SQLException e) {
			resp.sendRedirect(req.getContextPath() + "/admin/products?error="
					+ java.net.URLEncoder.encode("Failed to delete product", java.nio.charset.StandardCharsets.UTF_8));
		}
	}
}

