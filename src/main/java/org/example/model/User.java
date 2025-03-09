package org.example.model;

/**
 * The User class represents a user in the system with a username and password.
 */
public class User {
    private String username;
    private String password;

    /**
     * Constructor to initialize a User object with a username and password.
     *
     * @param username The username for the user.
     * @param password The password for the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    // Getters
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}

