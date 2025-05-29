package model;

import java.io.Serializable;

/**
 * A simple model class to represent login data (username and password).
 */
public class LoginModel  implements Serializable{
    private String username;
    private String password;

    // Constructors
    public LoginModel() {
    }

    public LoginModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
