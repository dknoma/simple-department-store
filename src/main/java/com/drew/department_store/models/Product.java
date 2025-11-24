package com.drew.department_store.models;

public class Product {
	private long id;
	private String name;
	// Grocery, Stationary, Toiletry, Vegetables
	private String category;
	private double cost; // price to buy stock of product
	private int quantity;

	public Product() {}

	public Product(
		long id,
		String name,
		String category,
		double cost,
		int quantity
	) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.cost = cost;
		this.quantity = quantity;
	}

	public long id() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String name() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String category() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double cost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double salePrice() {
		return (cost * 0.5) + cost;
	}

	public int quantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return String.format("{ \"id\": %d, \"name\": \"%s\", \"category\": \"%s\", \"quantity\": %d, \"cost\": %s, " +
								 "\"salePrice\": %s" +
								 " }",
							 id,
							 name,
							 category,
							 quantity,
							 cost,
							 salePrice()
		);
	}
}
