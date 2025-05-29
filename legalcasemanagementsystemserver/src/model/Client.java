package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Represents a client in the legal system.
 * Can be either an individual or an organization.
 */
@Entity
public class Client implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String clientId;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private String clientType; // Individual or Organization
    private LocalDate registrationDate;

    // One client can have many cases
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Case> cases = new ArrayList<>();

    // Many clients can be assigned to one lawyer
    @ManyToOne
    @JoinColumn(name = "lawyer_id")  // Foreign Key column in the Client table
    private Attorney lawyer;

    /**
     * Default constructor
     */
    public Client() {
        this.registrationDate = LocalDate.now();
    }

    /**
     * Constructor with essential fields
     */
    public Client(String clientId, String name, String email, String clientType) {
        this();
        this.clientId = clientId;
        this.name = name;
        this.email = email;
        this.clientType = clientType;
    }

    /**
     * Full constructor
     */
    public Client(int id, String clientId, String name, String contactPerson, String email,
                  String phone, String address, String clientType, LocalDate registrationDate) {
        this();
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

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }

    public Attorney getLawyer() {
        return lawyer;
    }

    public void setLawyer(Attorney lawyer) {
        this.lawyer = lawyer;
    }

    /**
     * Add a case and link it to this client
     */
    public void addCase(Case legalCase) {
        this.cases.add(legalCase);
        legalCase.setClient(this);
    }

    public boolean isIndividual() {
        return "Individual".equalsIgnoreCase(clientType);
    }

    public boolean isOrganization() {
        return "Organization".equalsIgnoreCase(clientType);
    }

    public String getDisplayName() {
        if (isOrganization() && contactPerson != null && !contactPerson.isEmpty()) {
            return name + " (Org, Contact: " + contactPerson + ")";
        } else if (isOrganization()) {
            return name + " (Organization)";
        } else {
            return name + " (Individual)";
        }
    }

    @Override
    public String toString() {
        return "Client [id=" + id + ", clientId=" + clientId + ", name=" + name +
               ", type=" + clientType + ", email=" + email + "]";
    }
}
