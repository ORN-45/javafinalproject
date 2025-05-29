package controller;

import dao.UserDao;
import model.User;

import java.util.List;

/**
 * Controller class for User entity operations.
 */
public class UserController {
    private UserDao userDao;

    public UserController() {
        this.userDao = new UserDao();
    }

    /**
     * Register a new user.
     */
    public String createUser(User user) {
        if (user == null) {
            return "User cannot be null";
        }
        return userDao.registerUser(user);
    }

    /**
     * Update an existing user.
     */
    public String updateUser(User user) {
        if (user == null) {
            return "User cannot be null";
        }
        return userDao.updateUser(user);
    }

    /**
     * Delete a user.
     */
    public String deleteUser(User user) {
        if (user == null) {
            return "User cannot be null";
        }
        return userDao.deleteUser(user);
    }

    /**
     * Retrieve a user by their ID.
     */
    public User getUserById(int id) {
        User temp = new User();
        temp.setId(id);
        return userDao.retrieveById(temp);
    }

    /**
     * Retrieve all users.
     */
    public List<User> getAllUsers() {
        return userDao.retrieveAll();
    }
    public List<User> getAllActiveUsers() {
        return userDao.getAllActiveUsers();
    }
    public List<User> getUsersByRole(String role) {
    return userDao.retrieveByRole(role);
}
public User getUserByUsername(String username) {
    return userDao.retrieveByUsername(username);
}

}
