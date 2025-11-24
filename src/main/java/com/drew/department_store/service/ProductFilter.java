package com.drew.department_store.service;

import com.drew.department_store.models.Product;

@FunctionalInterface
public interface ProductFilter {
	boolean isValid(Product product);
}
