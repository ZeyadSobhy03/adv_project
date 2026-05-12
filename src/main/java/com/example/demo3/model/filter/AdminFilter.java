package com.example.demo3.model.filter;

import com.example.demo3.model.util.AppConstants;
import com.example.demo3.model.dao.UserDAO;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {
	private final UserDAO userDAO = new UserDAO();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			chain.doFilter(request, response);
			return;
		}
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		Object userObj = req.getSession(false) == null ? null : req.getSession(false).getAttribute(AppConstants.SESSION_LOGGED_IN_USER);
		if (userObj == null) {
			resp.sendRedirect(req.getContextPath() + "/login.jsp?message="
					+ java.net.URLEncoder.encode("Admin access required", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}
		String username = String.valueOf(userObj);
		try {
			if (!userDAO.isAdmin(username)) {
				resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin only");
				return;
			}
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin only");
			return;
		}

		chain.doFilter(request, response);
	}
}

