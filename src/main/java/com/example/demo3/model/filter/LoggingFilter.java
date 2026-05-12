package com.example.demo3.model.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter("/*")
public class LoggingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long start = System.currentTimeMillis();
		try {
			chain.doFilter(request, response);
		} finally {
			long end = System.currentTimeMillis();
			if (request instanceof HttpServletRequest) {
				HttpServletRequest httpRequest = (HttpServletRequest) request;
				System.out.println("[REQ] " + httpRequest.getMethod() + " " + httpRequest.getRequestURI() +
						" took " + (end - start) + " ms");
			} else {
				System.out.println("[REQ] Non-HTTP request took " + (end - start) + " ms");
			}
		}
	}
}
