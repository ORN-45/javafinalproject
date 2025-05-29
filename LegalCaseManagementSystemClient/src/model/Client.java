package model; // Assuming client.model package

import java.io.Serializable;
import java.time.LocalDate;
// For this DTO, we'll omit complex associations like List<Case> or Attorney object
// to keep it simple and focused on Client data. If these are needed later,
// they would typically be DTOs themselves (e.g., List<CaseDTO>).

public class Client implements Serializable {
    private static final long serialVersionUID = 1L; // Good practice for Serializable classes

    private int id; // Primary Key
    private String clientId; // Business Key (e.g., "CL001")
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private String clientType; // e.g., "Individual", "Organization"
    private LocalDate registrationDate;

    public Client() {
        this.registrationDate = LocalDate.now(); // Default value
    }

    // Full constructor (example)
    public Client(int id, String clientId, String name, String contactPerson, String email,
                  String phone, String address, String clientType, LocalDate registrationDate) {
        this.id = id;
        this.clientId = clientId;
        this.name = name;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.clientType = clientType;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getClientType() { return clientType; }
    public void setClientType(String clientType) { this.clientType = clientType; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    @Override
    public String toString() {
        return "Client [id=" + id + ", clientId=" + clientId + ", name=" + name + "]";
    }
}
