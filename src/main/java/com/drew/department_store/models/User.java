package com.drew.department_store.models;

public abstract class User {
    protected String username;
    protected String emailId;
    protected String password;
    protected String type;

    public User() {
    }

    public User(String name, String email) {
        this.username = name;
        this.emailId = email;
    }

    public String username() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String emailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String password() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String type() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("{ \"username\": \"%s\", \"email\": \"%s\", \"type\": \"%s\" }", username, emailId, type);
    }
}
