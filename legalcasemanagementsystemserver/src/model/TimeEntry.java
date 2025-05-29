package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.math.BigDecimal;
import javax.persistence.*;

/**
 * Represents a time entry for billing purposes.
 * Records time spent on a case by an attorney.
 */
@Entity
@Table(name = "time_entries")  // Optional, to specify table name
public class TimeEntry implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Assuming auto-increment id
    private int id;

    @Column(nullable = false, unique = true)
    private String entryId;

    // Many time entries can belong to one case
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    private Case associatedCase;

    // Many time entries can belong to one attorney
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attorney_id", nullable = false)
    private Attorney attorney;

    @Column(nullable = false)
    private LocalDate entryDate;

    @Column(nullable = false)
    private double hours;

    @Column(length = 1024)
    private String description;

    private String activityCode;

    private BigDecimal hourlyRate;

    @Column(nullable = false)
    private boolean billed;

    // Many time entries can be associated with one invoice (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;    

public void setInvoice(Invoice invoice) {
    this.invoice = invoice;
    this.billed = true; // Mark as billed when linked to invoice
}

public Invoice getInvoice() {
    return invoice;
}

    /**
     * Default constructor
     */
    public TimeEntry() {
        this.entryDate = LocalDate.now();
        this.billed = false;
    }

    /**
     * Constructor with essential fields
     * 
     * @param entryId Unique time entry identifier
     * @param associatedCase The associated case
     * @param attorney The attorney who worked
     * @param hours Number of hours worked
     * @param description Description of work performed
     */
    public TimeEntry(String entryId, Case associatedCase, Attorney attorney, double hours, String description) {
        this();
        this.entryId = entryId;
        this.associatedCase = associatedCase;
        this.attorney = attorney;
        this.hours = hours;
        this.description = description;
        if (attorney != null) {
            this.hourlyRate = new BigDecimal(attorney.getHourlyRate());
        }
    }

    /**
     * Full constructor
     */
    public TimeEntry(int id, String entryId, Case associatedCase, Attorney attorney, LocalDate entryDate,
                     double hours, String description, String activityCode, BigDecimal hourlyRate,
                     boolean billed, Invoice invoice) {
        this();
        this.id = id;
        this.entryId = entryId;
        this.associatedCase = associatedCase;
        this.attorney = attorney;
        this.entryDate = entryDate;
        this.hours = hours;
        this.description = description;
        this.activityCode = activityCode;
        this.hourlyRate = hourlyRate;
        this.billed = billed;
        this.invoice = invoice;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
  
    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public Case getAssociatedCase() {
        return associatedCase;
    }

    public void setAssociatedCase(Case associatedCase) {
        this.associatedCase = associatedCase;
    }

    public Attorney getAttorney() {
        return attorney;
    }

    public void setAttorney(Attorney attorney) {
        this.attorney = attorney;
        if (attorney != null && this.hourlyRate == null) {
            this.hourlyRate = new BigDecimal(attorney.getHourlyRate());
        }
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public boolean isBilled() {
        return billed;
    }

    public void setBilled(boolean billed) {
        this.billed = billed;
    }



    /**
     * Calculate the amount for this time entry
     * 
     * @return Hours multiplied by hourly rate
     */
    public BigDecimal getAmount() {
        if (hourlyRate == null) {
            return BigDecimal.ZERO;
        }
        return hourlyRate.multiply(BigDecimal.valueOf(hours));
    }

    /**
     * Format hours as a string with quarter-hour precision
     * 
     * @return Formatted hours string (e.g., "2:15" for 2.25 hours)
     */
    public String getFormattedHours() {
        int hrs = (int) hours;
        int minutes = (int) Math.round((hours - hrs) * 60);
        return String.format("%d:%02d", hrs, minutes);
    }

    @Override
    public String toString() {
        return "TimeEntry [id=" + id + ", entryId=" + entryId + ", case=" + (associatedCase != null ? associatedCase.getId() : "null") +
               ", attorney=" + (attorney != null ? attorney.getId() : "null") + ", hours=" + hours + ", billed=" + billed + "]";
    }
}
