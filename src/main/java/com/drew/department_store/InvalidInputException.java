package com.drew.department_store;

public class InvalidInputException extends Throwable {

	public InvalidInputException() {
		super("Input was invalid... Please try again.");
	}

	public InvalidInputException(String message) {
		super(String.format("InvalidInputException - %s", message));
	}
}
