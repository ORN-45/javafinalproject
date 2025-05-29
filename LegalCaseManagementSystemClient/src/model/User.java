package model; // Assuming client.model package

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private LocalDate registrationDate;
    private LocalDateTime lastLogin;
    private boolean active;
    // Password field is intentionally omitted from client DTO for security,
    // except when explicitly needed for creation/update forms where it's handled transiently.
    // For createUser, we might need a way to temporarily hold it.
    private String password; // Add for createUser scenario, ensure it's not populated from server.


    public User() {
        this.registrationDate = LocalDate.now();
        this.active = true;
    }

    // Constructor for DTO - typically wouldn't include password
    public User(int id, String username, String email, String fullName, String role,
                LocalDate registrationDate, LocalDateTime lastLogin, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.registrationDate = registrationDate;
        this.lastLogin = lastLogin;
        this.active = active;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    // Temporary password field for user creation forms
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }


    @Override
    public String toString() {
        return "UserDTO [id=" + id + ", username=" + username + ", fullName=" + fullName +
                ", role=" + role + ", active=" + active + "]";
    }
}
