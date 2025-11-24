package com.drew.department_store.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.drew.department_store.models.Admin;
import com.drew.department_store.models.Customer;
import com.drew.department_store.models.User;

public class UserService {
	private static final Map<String, User> users = new HashMap<>();
	private static final Map<String, String> salts = new HashMap<>();

	// username -> User
	// username -> password

	static {
		registerNewAdmin("admin", "admin@store.com", "password123");
	}

	public static boolean userExists(String username) {
		return users.containsKey(username);
	}

	public static Optional<User> login(String username, String password) {
		Optional<User> container = Optional.empty();
		if (!users.containsKey(username) || !salts.containsKey(username)) return container;

		String salt = password; // TODO: fix to salt
		// salt
		String s = salts.getOrDefault(username, "");

		if (s.isEmpty()) {
			return container;
		}

		if (s.equals(salt)) {
			return Optional.of(users.get(username));
		}

		return container;
	}

	public static Optional<Customer> registerNewCustomer(String username, String email, String salt) {
		Optional<Customer> container = Optional.empty();
		if (users.containsKey(username)) return container;

		Customer customer = new Customer(username, email, 100);
		container = Optional.of(customer);
		users.put(username, customer);
		salts.put(username, salt);

		return container;
	}

	public static Optional<Admin> registerNewAdmin(String username, String email, String salt) {
		Optional<Admin> container = Optional.empty();
		if (users.containsKey(username)) return container;

		Admin admin = new Admin(username, email);
		container = Optional.of(admin);
		users.put(username, admin);
		salts.put(username, salt);

		return container;
	}
}
