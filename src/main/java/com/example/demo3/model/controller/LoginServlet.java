package com.example.demo3.model.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import com.example.demo3.model.dao.UserDAO;
import com.example.demo3.model.service.AuthService;
import com.example.demo3.model.service.RateLimiter;
import com.example.demo3.model.util.AppConstants;
import com.example.demo3.model.util.JwtUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  private final UserDAO userDAO = new UserDAO();
  private final AuthService authService = new AuthService(userDAO);
  private static final RateLimiter RATE_LIMITER = new RateLimiter();
  private static final int MAX_REQUESTS = 5;
  private static final long WINDOW_MILLIS = 10_000L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String ip = request.getRemoteAddr();
    String key = "login:" + (ip == null ? "unknown" : ip);
    if (!RATE_LIMITER.allow(key, MAX_REQUESTS, WINDOW_MILLIS)) {
      response.sendError(429, "Too many requests. Please wait a few seconds and try again.");
      return;
    }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            boolean valid = authService.login(username, password);

            if (valid) {
                request.getSession(true).setAttribute("loggedInUser", username);

        // Determine admin role from DB
        boolean isAdmin = false;
        try {
          isAdmin = userDAO.isAdmin(username);
        } catch (Exception ignored) {
        }
        request.getSession(true).setAttribute("isAdmin", isAdmin);

                // Also issue a JWT (stateless) and store it in an HttpOnly cookie
                String token = JwtUtil.issueToken(username, AppConstants.JWT_TTL_SECONDS, AppConstants.JWT_SECRET);
                Cookie jwtCookie = new Cookie(AppConstants.JWT_COOKIE_NAME, token);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
                // 1 hour
                jwtCookie.setMaxAge((int) AppConstants.JWT_TTL_SECONDS);
                response.addCookie(jwtCookie);

                // redirect to product listing servlet
                response.sendRedirect(request.getContextPath() + "/products");
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
