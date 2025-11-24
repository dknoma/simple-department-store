package com.drew.department_store.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.drew.department_store.DepartmentStoreApplication;

public enum Category {

	None,
	Grocery,
	Stationary,
	Toiletry,
	Vegetables;

	private static final Map<String, Category> CATEGORIES = new HashMap<>();

	static {
		Arrays.asList(Category.values())
			  .forEach(instance -> CATEGORIES.put(instance.name().toLowerCase(), instance));
	}

	public static Category fromName(String text) {
		return CATEGORIES.getOrDefault(text.toLowerCase(), None);
	}
}
