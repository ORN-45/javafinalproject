package model; // Assuming client.model package

import java.io.Serializable;
import java.time.LocalDate;
// No List imports for now, keeping DTO simple for selection/basic info

public class Case implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String caseNumber;
    private String title;
    private String caseType;
    private String status;
    private String description; 
    private LocalDate fileDate;
    private LocalDate closingDate; 
    private String court;         
    private int clientId;        // ID of the associated client
    private String clientName; // Added to store client name for display convenience

    public Case() {
        this.fileDate = LocalDate.now(); // Default
        this.status = "Open"; // Default
    }

    // Constructor for basic info
    public Case(int id, String caseNumber, String title, String status, int clientId) {
        this();
        this.id = id;
        this.caseNumber = caseNumber;
        this.title = title;
        this.status = status;
        this.clientId = clientId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCaseNumber() { return caseNumber; }
    public void setCaseNumber(String caseNumber) { this.caseNumber = caseNumber; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCaseType() { return caseType; }
    public void setCaseType(String caseType) { this.caseType = caseType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getFileDate() { return fileDate; }
    public void setFileDate(LocalDate fileDate) { this.fileDate = fileDate; }
    public LocalDate getClosingDate() { return closingDate; }
    public void setClosingDate(LocalDate closingDate) { this.closingDate = closingDate; }
    public String getCourt() { return court; }
    public void setCourt(String court) { this.court = court; }
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }


    @Override
    public String toString() {
        // Used in JComboBox, so a meaningful representation is good.
        String displayTitle = (title != null && !title.isEmpty()) ? title : "Untitled Case";
        String displayCaseNumber = (caseNumber != null && !caseNumber.isEmpty()) ? caseNumber : "ID " + id;
        return displayCaseNumber + " - " + displayTitle;
    }
}
