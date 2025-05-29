package controller;

// Assuming model.User is compatible with server's model.User
import model.User; 
import service.remote.UserService; // UserService now handles login

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class LoginController {
    private UserService userService; // RMI service stub

    /**
     * Constructor initializes UserService stub via RMI.
     */
    public LoginController() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            this.userService = (UserService) registry.lookup("UserService");
        } catch (Exception e) {
            System.err.println("LoginController: Error looking up UserService: " + e.getMessage());
            e.printStackTrace();
            // userService will be null, methods should handle this.
        }
    }

    /**
     * Authenticate a user by username and password.
     *
     * @param username The username provided by the user
     * @param password The password provided by the user
     * @return The authenticated User object if credentials are valid, otherwise null
     * @throws RemoteException if an RMI communication error occurs.
     */
    public User authenticateUser(String username, String password) throws RemoteException {
        if (this.userService == null) {
            throw new RemoteException("UserService not initialized in LoginController.");
        }
        if (username == null || password == null) {
            // Or throw IllegalArgumentException, consistent with other controllers
            throw new IllegalArgumentException("Username or password cannot be null.");
        }
        return userService.loginUser(username, password);
    }
}
