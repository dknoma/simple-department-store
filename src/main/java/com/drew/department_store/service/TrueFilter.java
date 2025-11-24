package com.drew.department_store.service;

import com.drew.department_store.models.Product;

public class TrueFilter implements ProductFilter {
	@Override
	public boolean isValid(Product product) {
		return true;
	}
}
