package com.example.demo3.model.service;

import com.example.demo3.model.dao.UserDAO;
import com.example.demo3.model.util.ValidationUtil;

import java.sql.SQLException;

public class AuthService {
	private final UserDAO userDAO;

	public AuthService() {
		this(new UserDAO());
	}

	public AuthService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public boolean register(String username, String password) throws SQLException {
		if (!ValidationUtil.isValidUsername(username) || !ValidationUtil.isValidPassword(password)) {
			return false;
		}
		try {
			if (userDAO.existsByUsername(username)) {
				return false;
			}
			userDAO.save(username, password);
			return true;
		} catch (SQLException e) {
			throw e;
		}
	}

	public boolean login(String username, String password) throws SQLException {
		if (ValidationUtil.isBlank(username) || ValidationUtil.isBlank(password)) {
			return false;
		}
		try {
			return userDAO.validateCredentials(username, password);
		} catch (SQLException e) {
			throw e;
		}
	}
}
