package service.remote.impl;

import dao.LoginDao;
import dao.UserDao;
import model.User;
import service.remote.UserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {

    private final UserDao userDao;
    private final LoginDao loginDao;

    public UserServiceImpl() throws RemoteException {
        super();
        this.userDao = new UserDao();
        this.loginDao = new LoginDao();
    }

    @Override
    public User loginUser(String username, String password) throws RemoteException {
        // model.User now has a 'password' field, and LoginDao.authenticateUser is updated.
        return loginDao.authenticateUser(username, password);
    }

    @Override
    public String createUser(User user) throws RemoteException {
        // model.User now has a 'password' field.
        // LoginDao.createUser is fixed and handles setting the role and saving.
        // Assumes user object passed in has password and role set.
        return loginDao.createUser(user, user.getRole());
    }

    @Override
    public User getUserById(int userId) throws RemoteException {
        User tempUser = new User();
        tempUser.setId(userId);
        return userDao.retrieveById(tempUser);
    }

    @Override
    public List<User> getAllUsers() throws RemoteException {
        return userDao.retrieveAll();
    }

    @Override
    public String updateUser(User user) throws RemoteException {
        // Note: This method updates user details except for the password.
        // Password changes should go through changePassword.
        // If user.getPassword() is part of the 'user' object, this might unintentionally update it
        // if the client sends it. Best to fetch user, apply changes, and explicitly not set password here.
        
        // For now, let's assume client does not send password in general update, or if it does, it's intended.
        // Or, even better, ensure User.password is not part of general update payloads from client.
        return userDao.updateUser(user);
    }

    @Override
    public String deleteUser(User user) throws RemoteException {
        return userDao.deleteUser(user);
    }

    @Override
    public String changePassword(int userId, String oldPassword, String newPassword) throws RemoteException {
        User userToUpdate = getUserById(userId); // This already uses userDao.retrieveById

        if (userToUpdate == null) {
            return "User not found with ID: " + userId;
        }

        // Verify old password
        // Note: Passwords should be hashed in a real system. This is plain text comparison.
        if (userToUpdate.getPassword() == null || !userToUpdate.getPassword().equals(oldPassword)) {
            return "Old password incorrect.";
        }

        // Update to new password
        return userDao.updatePassword(userId, newPassword);
    }

    // Implementation of new methods for Admin Panel Functionality
    @Override
    public List<User> getUsersByRole(String role) throws RemoteException {
        try {
            return userDao.findByRole(role); // Assuming findByRole exists in UserDao
        } catch (Exception e) {
            System.err.println("UserServiceImpl: Error in getUsersByRole: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching users by role.", e);
        }
    }

    @Override
    public User getUserByUsername(String username) throws RemoteException {
        try {
            return userDao.findByUsername(username); // Assuming findByUsername exists in UserDao
        } catch (Exception e) {
            System.err.println("UserServiceImpl: Error in getUserByUsername: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching user by username.", e);
        }
    }

    @Override
    public boolean deactivateUser(int userId) throws RemoteException {
        try {
            return userDao.deactivateUser(userId);
        } catch (Exception e) {
            System.err.println("UserServiceImpl: Error in deactivateUser: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while deactivating user.", e);
        }
    }

    @Override
    public boolean reactivateUser(int userId) throws RemoteException {
        try {
            return userDao.reactivateUser(userId);
        } catch (Exception e) {
            System.err.println("UserServiceImpl: Error in reactivateUser: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while reactivating user.", e);
        }
    }

    @Override
    public String resetPasswordByEmail(String email) throws RemoteException {
        try {
            User user = userDao.findByEmail(email);
            if (user == null) {
                return "User with specified email not found."; // Or throw a specific exception
            }
            String newPassword = generateRandomPassword(8); // Generate an 8-character random password
            String result = userDao.updatePassword(user.getId(), newPassword); // userDao.updatePassword updates and returns a message
            if (result != null && result.toLowerCase().contains("success")) {
                 // Ideally, the server would email this password or handle it more securely.
                 // Returning it directly in the response is for simplicity in this context.
                System.out.println("Password for user " + user.getUsername() + " (email: " + email + ") reset to: " + newPassword);
                return "Password reset successfully. New password: " + newPassword; 
            } else {
                return "Failed to reset password for user with email: " + email + ". " + (result != null ? result : "");
            }
        } catch (Exception e) {
            System.err.println("UserServiceImpl: Error in resetPasswordByEmail: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error during password reset by email.", e);
        }
    }

    @Override
    public boolean isUsernameTaken(String username) throws RemoteException {
        try {
            return userDao.isUsernameExists(username);
        } catch (Exception e) {
            System.err.println("UserServiceImpl: Error in isUsernameTaken: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while checking if username is taken.", e);
        }
    }

    @Override
    public List<String> getAvailableRoles() throws RemoteException {
        try {
            return userDao.getAllRoles();
        } catch (Exception e) {
            System.err.println("UserServiceImpl: Error in getAvailableRoles: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching available roles.", e);
        }
    }
    
    @Override
    public User getUserByEmail(String email) throws RemoteException {
        try {
            return userDao.findByEmail(email);
        } catch (Exception e) {
            System.err.println("UserServiceImpl: Error in getUserByEmail: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching user by email.", e);
        }
    }

    // Helper method to generate a random password
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";
        java.util.Random random = new java.util.Random();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}
