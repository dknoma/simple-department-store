package com.drew.department_store.service;

import com.drew.department_store.models.Product;

public class IdFilter implements ProductFilter {
	private long id;

	public static IdFilter of(long id) {
		IdFilter filter = new IdFilter();
		filter.id = id;
		return filter;
	}

	@Override
	public boolean isValid(Product product) {
		return product.id() == id;
	}

	public long id() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
