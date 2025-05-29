package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.*;

/**
 * Represents a user in the legal case management system.
 */

@Entity
@Table(name = "users")  // Optional: to avoid default table name "User" which can be a reserved word
public class User implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(nullable = false)
    private LocalDate registrationDate;

    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, length = 255) // Assuming a reasonable length for a password hash eventually
    private String password;

    // Relationship: One User can have many TimeEntries
    @OneToMany(mappedBy = "attorney", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TimeEntry> timeEntries;

    // Relationship: One User can be assigned to many Cases
    @OneToMany(mappedBy = "assignedUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Case> cases;

    // Role constants
    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_ATTORNEY = "Attorney";
    public static final String ROLE_STAFF = "Staff";
    public static final String ROLE_FINANCE = "Finance";
    public static final String ROLE_READONLY = "ReadOnly";

    /**
     * Default constructor
     */
    public User() {
        this.registrationDate = LocalDate.now();
        this.active = true;
    }

    /**
     * Constructor with essential fields
     */
    public User(String username, String email, String fullName, String role) {
        this();
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    /**
     * Full constructor
     */
    public User(int id, String username, String email, String fullName, String role, String password,
                LocalDate registrationDate, LocalDateTime lastLogin, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.password = password; // Initialize password
        this.registrationDate = registrationDate;
        this.lastLogin = lastLogin;
        this.active = active;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public void setTimeEntries(Set<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }

    public Set<Case> getCases() {
        return cases;
    }

    public void setCases(Set<Case> cases) {
        this.cases = cases;
    }

    // Role-checking methods

    public boolean isAdmin() {
        return ROLE_ADMIN.equals(role);
    }

    public boolean isAttorney() {
        return ROLE_ATTORNEY.equals(role);
    }

    public boolean isFinance() {
        return ROLE_FINANCE.equals(role);
    }

    public boolean isStaff() {
        return ROLE_STAFF.equals(role);
    }

    public boolean isReadOnly() {
        return ROLE_READONLY.equals(role);
    }

    public boolean canModifyCases() {
        return isAdmin() || isAttorney() || isStaff();
    }

    public boolean canViewFinancials() {
        return isAdmin() || isFinance() || isAttorney();
    }

    public boolean canModifyFinancials() {
        return isAdmin() || isFinance();
    }

    public boolean canManageUsers() {
        return isAdmin();
    }

    public String getDisplayName() {
        return fullName + " (" + role + ")";
    }

    public boolean isNewAccount() {
        return lastLogin == null;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", fullName=" + fullName +
                ", role=" + role + ", active=" + active + "]";
    }
}
