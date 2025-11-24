package com.drew.department_store.service;

import com.drew.department_store.models.Product;

public class NameFilter implements ProductFilter {
	private String name;

	public static NameFilter of(String name) {
		NameFilter filter = new NameFilter();
		filter.name = name;
		return filter;
	}

	@Override
	public boolean isValid(Product product) {
		return product.name().equalsIgnoreCase(name);
	}

	public String name() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
