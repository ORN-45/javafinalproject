package model; // Assuming client.model package

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String eventId; // Business key
    private String title;
    private String description;
    private String eventType;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String status;
    private boolean reminderSet;
    private int reminderDays;
    private int caseId; // To associate with a Case DTO
    // private Case associatedCaseObj; // Optional: if you want to carry the full Case DTO

    public Event() {
        this.status = "Scheduled"; // Default
        this.reminderSet = true;   // Default
        this.reminderDays = 1;     // Default
        this.eventDate = LocalDate.now(); // Sensible default
    }

    // Example constructor
    public Event(int id, String eventId, String title, String eventType, LocalDate eventDate, int caseId) {
        this();
        this.id = id;
        this.eventId = eventId;
        this.title = title;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.caseId = caseId;
    }

    // Getters and Setters for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isReminderSet() { return reminderSet; }
    public void setReminderSet(boolean reminderSet) { this.reminderSet = reminderSet; }
    public int getReminderDays() { return reminderDays; }
    public void setReminderDays(int reminderDays) { this.reminderDays = reminderDays; }
    public int getCaseId() { return caseId; }
    public void setCaseId(int caseId) { this.caseId = caseId; }

    // public Case getAssociatedCaseObj() { return associatedCaseObj; }
    // public void setAssociatedCaseObj(Case associatedCaseObj) { this.associatedCaseObj = associatedCaseObj; }


    @Override
    public String toString() {
        return "EventDTO [id=" + id + ", title=" + title + ", date=" + eventDate + ", status=" + status + "]";
    }
}
