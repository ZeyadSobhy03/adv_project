package com.example.demo3.model.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

import java.io.IOException;
import java.util.Optional;

import com.example.demo3.model.service.RedisCacheService;
import com.example.demo3.model.util.AppConstants;
import com.example.demo3.model.util.JwtUtil;

@WebFilter(urlPatterns = {"/products"})
public class AuthFilter implements Filter {
	private final RedisCacheService redisCacheService = new RedisCacheService();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			chain.doFilter(request, response);
			return;
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);
		Object loggedInUser = session == null ? null : session.getAttribute(AppConstants.SESSION_LOGGED_IN_USER);

		// If no session user, try stateless JWT cookie (and ensure it is NOT revoked in Redis)
		if (loggedInUser == null) {
			String token = null;
			Cookie[] cookies = httpRequest.getCookies();
			if (cookies != null) {
				for (Cookie c : cookies) {
					if (AppConstants.JWT_COOKIE_NAME.equals(c.getName())) {
						token = c.getValue();
						break;
					}
				}
			}

			if (token != null && !token.trim().isEmpty() && !redisCacheService.isJwtRevoked(token)) {
				Optional<String> username = JwtUtil.verifyAndGetUsername(token, AppConstants.JWT_SECRET);
				if (username.isPresent()) {
					HttpSession newSession = httpRequest.getSession(true);
					newSession.setAttribute(AppConstants.SESSION_LOGGED_IN_USER, username.get());
					loggedInUser = username.get();
				}
			}
		}


		if (loggedInUser == null) {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
			return;
		}

		chain.doFilter(request, response);
	}
}
