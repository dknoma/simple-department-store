package com.drew.department_store.models;

public class Admin extends User {
	public Admin(String username, String email) {
		super(username, email);
		type = UserType.ADMIN.name().toLowerCase();
	}
}
