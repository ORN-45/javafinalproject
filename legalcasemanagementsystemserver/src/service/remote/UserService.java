package service.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.User; // Server-side model.User

public interface UserService extends Remote {
    User loginUser(String username, String password) throws RemoteException;
    String createUser(User user) throws RemoteException;
    User getUserById(int userId) throws RemoteException;
    List<User> getAllUsers() throws RemoteException;
    String updateUser(User user) throws RemoteException;
    String deleteUser(User user) throws RemoteException;
    String changePassword(int userId, String oldPassword, String newPassword) throws RemoteException;

    // New methods for Admin Panel Functionality
    List<User> getUsersByRole(String role) throws RemoteException;
    User getUserByUsername(String username) throws RemoteException;
    boolean deactivateUser(int userId) throws RemoteException;
    boolean reactivateUser(int userId) throws RemoteException;
    String resetPasswordByEmail(String email) throws RemoteException; // Changed from resetPasswordForUser
    boolean isUsernameTaken(String username) throws RemoteException;
    List<String> getAvailableRoles() throws RemoteException;
    User getUserByEmail(String email) throws RemoteException;
}
