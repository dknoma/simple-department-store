package com.drew.department_store;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Operation {

	// Users
	REGISTER("register"),			// prompts for 'username', then 'password'
	LOGIN("login"),				// prompts for 'username', then 'password'
	LOGOUT("logout"),
	FILTER("filter"),				// args='category', 'price'
	CLEAR_FILTER("clearFilter"),

	// Admin
	LIST_PRODUCTS("all_products"),
	GET_FROM_ID("get_from_id"),		// args='productid'
	GET_FROM_NAME("get_from_name"),	// args='name'
	GET_FROM_CATEGORY("get_from_category"),	// args='name'
	ADD_PRODUCT("add_product"),
	TOTAL_SPENT("total_spent"),
	PROFITS("profits"),			// args='category'

	// All
	HOME("home"),
	EXIT("exit"),
	INVALID("invalid");

	private String text = "";

	private static final Map<String, Operation> OPERATIONS = new HashMap<>();

	static {
		Arrays.asList(Operation.values())
			  .forEach(instance -> OPERATIONS.put(instance.getOperation(), instance));
	}

	Operation(String text) {
		this.text = text;
	}

	public String getOperation() {
		return text;
	}

	public static Operation fromName(String name) {
		return OPERATIONS.getOrDefault(name.toLowerCase(), INVALID);
	}
}
