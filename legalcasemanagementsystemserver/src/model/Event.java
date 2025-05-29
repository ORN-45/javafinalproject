package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.*;

/**
 * Represents an event or deadline in the legal system.
 * Can be associated with a specific case.
 */
@Entity
public class Event implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String eventId;
    private String title;
    private String description;
    private String eventType;  // Court Date, Meeting, Deadline, etc.
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String status;  // Scheduled, Completed, Cancelled, etc.
    private boolean reminderSet;
    private int reminderDays;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case associatedCase;

    /**
     * Default constructor
     */
    public Event() {
        this.status = "Scheduled";
        this.reminderSet = true;
        this.reminderDays = 1;
    }

    /**
     * Constructor with essential fields
     */
    public Event(String eventId, String title, String eventType, LocalDate eventDate, Case associatedCase) {
        this();
        this.eventId = eventId;
        this.title = title;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.associatedCase = associatedCase;
    }

    /**
     * Full constructor
     */
    public Event(int id, String eventId, String title, String description, String eventType,
                 LocalDate eventDate, LocalTime startTime, LocalTime endTime, String location,
                 String status, Case associatedCase, boolean reminderSet, int reminderDays) {
        this();
        this.id = id;
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.status = status;
        this.associatedCase = associatedCase;
        this.reminderSet = reminderSet;
        this.reminderDays = reminderDays;
    }

    // Getters and Setters

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

    public Case getAssociatedCase() { return associatedCase; }
    public void setAssociatedCase(Case associatedCase) { this.associatedCase = associatedCase; }

    // Business Logic

    public boolean isUpcoming() {
        return eventDate != null && eventDate.isAfter(LocalDate.now());
    }

    public boolean isOverdue() {
        return eventDate != null && eventDate.isBefore(LocalDate.now()) &&
               !("Completed".equalsIgnoreCase(status) || "Cancelled".equalsIgnoreCase(status));
    }

    public String getDisplayText() {
        String dateStr = eventDate != null ? eventDate.toString() : "No date";
        return title + " (" + dateStr + " - " + eventType + ")";
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", eventId=" + eventId + ", title=" + title +
               ", date=" + eventDate + ", status=" + status + "]";
    }
}
