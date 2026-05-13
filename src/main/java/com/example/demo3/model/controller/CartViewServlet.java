package com.example.demo3.model.controller;

import com.example.demo3.model.model.Product;
import com.example.demo3.model.service.CartService;
import com.example.demo3.model.service.RateLimiter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
// cart view
@WebServlet("/cart")
public class CartViewServlet extends HttpServlet {
	private static final RateLimiter RATE_LIMITER = new RateLimiter();
	private static final int MAX_REQUESTS = 5;
	private static final long WINDOW_MILLIS = 10_000L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ip = req.getRemoteAddr();
		String key = "cart:view:" + (ip == null ? "unknown" : ip);
		if (!RATE_LIMITER.allow(key, MAX_REQUESTS, WINDOW_MILLIS)) {
			resp.sendError(429, "Too many requests. Please wait a few seconds and try again.");
			return;
		}
		List<Product> cart = CartService.viewCart(req.getSession(true));
		req.setAttribute("cart", cart);
		RequestDispatcher dispatcher = req.getRequestDispatcher("/cart.jsp");
		dispatcher.forward(req, resp);
	}
}

