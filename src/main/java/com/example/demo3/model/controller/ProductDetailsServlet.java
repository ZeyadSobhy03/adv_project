package com.example.demo3.model.controller;

import com.example.demo3.model.dao.ReviewDAO;
import com.example.demo3.model.model.Product;
import com.example.demo3.model.model.Review;
import com.example.demo3.model.service.ProductService;
import com.example.demo3.model.service.RateLimiter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet("/product")
public class ProductDetailsServlet extends HttpServlet {
	private final ReviewDAO reviewDAO = new ReviewDAO();
	private final ProductService productService = new ProductService();
	private static final RateLimiter RATE_LIMITER = new RateLimiter();
	private static final int MAX_REQUESTS = 5;
	private static final long WINDOW_MILLIS = 10_000L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ip = req.getRemoteAddr();
		String key = "product:" + (ip == null ? "unknown" : ip);
		if (!RATE_LIMITER.allow(key, MAX_REQUESTS, WINDOW_MILLIS)) {
			resp.sendError(429, "Too many requests. Please wait a few seconds and try again.");
			return;
		}
		String idParam = req.getParameter("id");
		int id;
		try {
			id = Integer.parseInt(idParam);
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid product id");
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
			if (found == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
				return;
			}

			req.setAttribute("product", found);

			// Reviews section
			try {
				List<Review> reviews = reviewDAO.findByProductId(id);
				req.setAttribute("reviews", reviews);
				req.setAttribute("reviewCount", reviewDAO.getReviewCount(id));
				req.setAttribute("avgRating", reviewDAO.getAverageRating(id));
			} catch (Exception ex) {
				req.setAttribute("reviews", Collections.emptyList());
				req.setAttribute("reviewCount", 0);
				req.setAttribute("avgRating", 0.0);
			}

			RequestDispatcher dispatcher = req.getRequestDispatcher("/productDetails.jsp");
			dispatcher.forward(req, resp);
		} catch (ClassNotFoundException | SQLException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load product");
		}
	}
}

