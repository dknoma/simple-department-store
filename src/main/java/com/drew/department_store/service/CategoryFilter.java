package com.drew.department_store.service;

import com.drew.department_store.models.Product;

public class CategoryFilter implements ProductFilter {

	private String category;

	public static CategoryFilter of(String category) {
		CategoryFilter filter = new CategoryFilter();
		filter.category = category;
		return filter;
	}

	@Override
	public boolean isValid(Product product) {
		return product.category().equalsIgnoreCase(category);
	}

	public String category() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
