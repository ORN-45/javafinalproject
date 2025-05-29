package model; // Assuming client.model package

import java.io.Serializable;
// For this DTO, we'll omit complex associations like List<Case>
// to keep it simple and focused on Attorney data.

public class Attorney implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id; // Primary Key
    private String attorneyId; // Business Key (e.g., "ATT001")
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String specialization;
    private String barNumber;
    private double hourlyRate;

    public Attorney() {
    }

    // Full constructor (example)
    public Attorney(int id, String attorneyId, String firstName, String lastName, String email,
                    String phone, String specialization, String barNumber, double hourlyRate) {
        this.id = id;
        this.attorneyId = attorneyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.barNumber = barNumber;
        this.hourlyRate = hourlyRate;
    }

    // Getters and Setters for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getAttorneyId() { return attorneyId; }
    public void setAttorneyId(String attorneyId) { this.attorneyId = attorneyId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFullName() { 
        String first = this.firstName == null ? "" : this.firstName;
        String last = this.lastName == null ? "" : this.lastName;
        return (first + " " + last).trim();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getBarNumber() { return barNumber; }
    public void setBarNumber(String barNumber) { this.barNumber = barNumber; }
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    @Override
    public String toString() {
        return "Attorney [id=" + id + ", attorneyId=" + attorneyId + ", name=" + getFullName() + "]";
    }
}
