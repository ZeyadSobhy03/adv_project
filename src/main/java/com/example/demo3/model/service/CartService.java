package com.example.demo3.model.service;

import com.example.demo3.model.model.Product;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CartService {
	public static final String CART_SESSION_KEY = "cartItems";

	private CartService() {
	}

	@SuppressWarnings("unchecked")
	public static List<Product> getCart(HttpSession session) {
		Object value = session.getAttribute(CART_SESSION_KEY);
		if (value instanceof List) {
			return (List<Product>) value;
		}
		List<Product> cart = new ArrayList<>();
		session.setAttribute(CART_SESSION_KEY, cart);
		return cart;
	}

	public static void addToCart(HttpSession session, Product p) {
		if (p == null) {
			return;
		}
		getCart(session).add(p);
	}

	public static List<Product> viewCart(HttpSession session) {
		List<Product> cart = getCart(session);
		return Collections.unmodifiableList(cart);
	}
}

