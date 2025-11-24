package com.drew.department_store.models;

public class Customer extends User {

	private int superCoins;

	public Customer(String username, String email, int coins) {
		this.username = username;
		this.emailId = email;
		this.superCoins = coins;
		type = UserType.CUSTOMER.name().toLowerCase();
	}

	public int superCoins() {
		return superCoins;
	}

	public void setSuperCoins(int superCoins) {
		this.superCoins = superCoins;
	}
}
