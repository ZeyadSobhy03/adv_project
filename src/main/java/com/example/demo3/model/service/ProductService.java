package com.example.demo3.model.service;

import com.example.demo3.model.dao.ProductDAO;
import com.example.demo3.model.model.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
	private final ProductDAO productDAO;

	public ProductService() {
		this(new ProductDAO());
	}

	public ProductService(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public List<Product> listProducts() throws SQLException, ClassNotFoundException {
		return productDAO.findAll();
	}
}
