package controller;

import dao.LoginDao;
import model.User;

public class LoginController {
    private LoginDao loginDao;

    /**
     * Constructor initializes LoginDao instance
     */
    public LoginController() {
        this.loginDao = new LoginDao();
    }

    /**
     * Authenticate a user by username and password.
     *
     * @param username The username provided by the user
     * @param password The password provided by the user
     * @return The authenticated User object if credentials are valid, otherwise null
     */
    public User authenticateUser(String username, String password) {
        if (username == null || password == null) {
            return null; // Could also throw IllegalArgumentException if you want
        }
        return loginDao.authenticateUser(username, password);
    }
}
