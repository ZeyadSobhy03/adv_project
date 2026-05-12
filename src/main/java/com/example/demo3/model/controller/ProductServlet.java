package com.example.demo3.model.controller;

import com.example.demo3.model.model.Product;
import com.example.demo3.model.service.RateLimiter;
import com.example.demo3.model.service.ProductService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
  private final ProductService productService = new ProductService();
  private static final RateLimiter RATE_LIMITER = new RateLimiter();
  private static final int MAX_REQUESTS = 5;
  private static final long WINDOW_MILLIS = 10_000L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ip = req.getRemoteAddr();
        String key = "products:" + (ip == null ? "unknown" : ip);
        if (!RATE_LIMITER.allow(key, MAX_REQUESTS, WINDOW_MILLIS)) {
            resp.sendError(429, "Too many requests. Please wait a few seconds and try again.");
            return;
        }
        try {
            List<Product> data = productService.listProducts();
            req.setAttribute("data", data);

            RequestDispatcher dispatcher = req.getRequestDispatcher("/productList.jsp");
            dispatcher.forward(req, resp);
        } catch (ClassNotFoundException | SQLException ex) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load products");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
