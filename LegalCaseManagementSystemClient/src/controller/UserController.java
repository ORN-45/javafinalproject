package controller;

// Client-side DTO
import model.User; // Alias: User_DTO
// Server-side Model
import legalcasemanagementsystemserver.model.User_SERVER; // Alias for server model
import service.remote.UserService; 

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.rmi.RemoteException; 

/**
 * Controller class for User entity operations using RMI with DTO mapping.
 */
public class UserController {
    private UserService userService; // RMI service stub

    public UserController() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            this.userService = (UserService) registry.lookup("UserService");
        } catch (Exception e) {
            System.err.println("UserController: Error looking up UserService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Mapping Helper Methods ---
    private User_SERVER mapUserDtoToServerModel(User userDto) { // For updates (no password)
        if (userDto == null) return null;
        User_SERVER serverModel = new User_SERVER();
        serverModel.setId(userDto.getId());
        serverModel.setUsername(userDto.getUsername());
        serverModel.setFullName(userDto.getFullName());
        serverModel.setEmail(userDto.getEmail());
        serverModel.setRole(userDto.getRole());
        serverModel.setActive(userDto.isActive());
        serverModel.setRegistrationDate(userDto.getRegistrationDate());
        serverModel.setLastLogin(userDto.getLastLogin());
        // Password is NOT mapped here for general updates
        return serverModel;
    }
    
    private User_SERVER mapUserDtoToServerModelWithPassword(User userDto) { // For creation
        User_SERVER serverModel = mapUserDtoToServerModel(userDto); // Base mapping
        if (userDto != null && serverModel != null) {
            serverModel.setPassword(userDto.getPassword()); // DTO's password field used for creation
        }
        return serverModel;
    }

    private User mapServerModelToUserDto(User_SERVER serverModel) {
        if (serverModel == null) return null;
        User userDto = new User();
        userDto.setId(serverModel.getId());
        userDto.setUsername(serverModel.getUsername());
        userDto.setFullName(serverModel.getFullName());
        userDto.setEmail(serverModel.getEmail());
        userDto.setRole(serverModel.getRole());
        userDto.setActive(serverModel.isActive());
        userDto.setRegistrationDate(serverModel.getRegistrationDate());
        userDto.setLastLogin(serverModel.getLastLogin());
        // Password is NOT set in DTO for security
        return userDto;
    }
    
    // --- Existing CRUD and Getter Methods (Updated for DTOs) ---

    public User createUser(User userDto) throws RemoteException { // Changed return type to User
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        if (userDto == null) throw new IllegalArgumentException("User DTO cannot be null");
        if (userDto.getUsername() == null || userDto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username in User DTO cannot be null or empty for creation.");
        }
        
        User_SERVER serverModel = mapUserDtoToServerModelWithPassword(userDto);
        String creationMessage = userService.createUser(serverModel); 
        
        if (creationMessage != null && creationMessage.toLowerCase().contains("success")) {
            // If creation was successful, fetch the user by username to get the full DTO with ID
            // This assumes username is unique and was used for creation.
            User_SERVER createdServerUser = userService.getUserByUsername(userDto.getUsername());
            if (createdServerUser != null) {
                return mapServerModelToUserDto(createdServerUser);
            } else {
                // This case is problematic: creation reported success, but user not found.
                // Could throw an exception or return null with a log.
                System.err.println("UserController: User creation reported success, but failed to fetch the created user: " + userDto.getUsername());
                // Depending on strictness, could return null or throw.
                // For now, let's return the original DTO, though it won't have the server-generated ID.
                // Or better, throw a specific exception or return null indicating failure to retrieve.
                throw new RemoteException("User created, but failed to retrieve details. Message: " + creationMessage);
            }
        } else {
            // Creation failed, throw an exception with the message from the server
            throw new RemoteException("User creation failed: " + (creationMessage != null ? creationMessage : "Unknown error"));
        }
    }

    public String updateUser(User userDto) throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        if (userDto == null) throw new IllegalArgumentException("User DTO cannot be null");
        User_SERVER serverModel = mapUserDtoToServerModel(userDto); // Uses mapper without password
        return userService.updateUser(serverModel);
    }

    public String deleteUser(User userDto) throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        if (userDto == null) throw new IllegalArgumentException("User DTO cannot be null");
        User_SERVER serverModel = mapUserDtoToServerModel(userDto);
        return userService.deleteUser(serverModel);
    }

    public User getUserById(int id) throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        User_SERVER serverModel = userService.getUserById(id);
        return mapServerModelToUserDto(serverModel);
    }

    public List<User> getAllUsers() throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        List<User_SERVER> serverUsers = userService.getAllUsers();
        if (serverUsers == null) return new ArrayList<>();
        return serverUsers.stream()
                          .map(this::mapServerModelToUserDto)
                          .collect(Collectors.toList());
    }

    public User loginUser(String username, String password) throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        User_SERVER serverModel = userService.loginUser(username, password);
        return mapServerModelToUserDto(serverModel);
    }

    public String changePassword(int userId, String oldPassword, String newPassword) throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        return userService.changePassword(userId, oldPassword, newPassword);
    }

    // --- Methods for New UserService Operations ---

    public List<User> getUsersByRole(String role) throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        List<User_SERVER> serverUsers = userService.getUsersByRole(role);
        if (serverUsers == null) return new ArrayList<>();
        return serverUsers.stream()
                          .map(this::mapServerModelToUserDto)
                          .collect(Collectors.toList());
    }

    public User getUserByUsername(String username) throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        User_SERVER serverModel = userService.getUserByUsername(username);
        return mapServerModelToUserDto(serverModel);
    }
    
    public User getUserByEmail(String email) throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        User_SERVER serverModel = userService.getUserByEmail(email);
        return mapServerModelToUserDto(serverModel);
    }

    public boolean deactivateUser(int userId) throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        return userService.deactivateUser(userId);
    }

    public boolean reactivateUser(int userId) throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        return userService.reactivateUser(userId);
    }

    public String resetPassword(String email) throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        return userService.resetPasswordByEmail(email);
    }

    public boolean isUsernameExists(String username) throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        return userService.isUsernameTaken(username);
    }

    public String[] getUserRoles() throws RemoteException {
        if (this.userService == null) throw new RemoteException("UserService not initialized.");
        List<String> rolesList = userService.getAvailableRoles();
        if (rolesList == null) return new String[0];
        return rolesList.toArray(new String[0]);
    }
}
