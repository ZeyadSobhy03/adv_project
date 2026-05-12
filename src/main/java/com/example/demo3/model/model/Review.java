package com.example.demo3.model.model;

public class Review {
	private int id;
	private int productId;
	private String username;
	private String comment;
	private int rating;

	public Review() {
	}

	public Review(int id, int productId, String username, String comment, int rating) {
		this.id = id;
		this.productId = productId;
		this.username = username;
		this.comment = comment;
		this.rating = rating;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
}
