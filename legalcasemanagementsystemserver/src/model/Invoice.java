package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Represents an invoice in the legal system.
 * Used for billing clients for legal services.
 */
@Entity
public class Invoice implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String invoiceNumber;

    // Many invoices can be for one client
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // Many invoices can be associated with one legal case
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    private Case legalCase;

    private LocalDate issueDate;
    private LocalDate dueDate;

    private BigDecimal amount;
    private BigDecimal amountPaid;

    private String status;
    private String notes;
    
    
    // One invoice can have many time entries
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeEntry> timeEntries = new ArrayList<>();

    // One invoice can have many payments
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    // Status constants
    public static final String STATUS_DRAFT = "Draft";
    public static final String STATUS_ISSUED = "Issued";
    public static final String STATUS_PAID = "Paid";
    public static final String STATUS_PARTIALLY_PAID = "Partially Paid";
    public static final String STATUS_OVERDUE = "Overdue";
    public static final String STATUS_CANCELLED = "Cancelled";

    /**
     * Default constructor
     */
    public Invoice() {
        this.issueDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(30);
        this.amount = BigDecimal.ZERO;
        this.amountPaid = BigDecimal.ZERO;
        this.status = STATUS_DRAFT;
    }

    /**
     * Constructor with essential fields
     */
    public Invoice(String invoiceNumber, Client client, Case legalCase, BigDecimal amount) {
        this();
        this.invoiceNumber = invoiceNumber;
        this.client = client;
        this.legalCase = legalCase;
        this.amount = amount;
    }

    /**
     * Full constructor
     */
    public Invoice(int id, String invoiceNumber, Client client, Case legalCase, LocalDate issueDate,
                   LocalDate dueDate, BigDecimal amount, BigDecimal amountPaid, String status, String notes) {
        this();
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.client = client;
        this.legalCase = legalCase;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.amount = amount;
        this.amountPaid = amountPaid;
        this.status = status;
        this.notes = notes;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Case getCase() {
        return legalCase;
    }

    public void setCase(Case legalCase) {
        this.legalCase = legalCase;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        updateStatus();
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
        updateStatus();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries.clear();
        if (timeEntries != null) {
            for (TimeEntry entry : timeEntries) {
                addTimeEntry(entry);
            }
        }
    }

    /**
     * Add a time entry to this invoice
     */
    public void addTimeEntry(TimeEntry entry) {
        timeEntries.add(entry);
        entry.setInvoice(this); // Instead of entry.setBilled(this);    
        recalculateAmount();
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments.clear();
        if (payments != null) {
            for (Payment payment : payments) {
                addPayment(payment);
            }
        }
    }

    /**
     * Add a payment to this invoice
     */
    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setInvoice(this); // Set back reference
        recalculateAmountPaid();
    }

    /**
     * Recalculate the invoice amount based on time entries
     */
    private void recalculateAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (TimeEntry entry : timeEntries) {
            total = total.add(entry.getAmount());
        }
        this.amount = total;
        updateStatus();
    }

    /**
     * Recalculate the amount paid based on payments
     */
    public void recalculateAmountPaid() {
        BigDecimal total = BigDecimal.ZERO;
        for (Payment payment : payments) {
            total = total.add(payment.getAmount());
        }
        this.amountPaid = total;
        updateStatus();
    }

    /**
     * Calculate the remaining balance on this invoice
     */
    public BigDecimal getBalance() {
        return amount.subtract(amountPaid);
    }

    /**
     * Check if the invoice is fully paid
     */
    public boolean isFullyPaid() {
        return amountPaid.compareTo(amount) >= 0;
    }

    /**
     * Check if the invoice is overdue
     */
    public boolean isOverdue() {
        return dueDate.isBefore(LocalDate.now()) && !isFullyPaid() &&
               !STATUS_CANCELLED.equals(status);
    }

    /**
     * Updates the invoice status based on payments and due date
     */
    public void updateStatus() {
        if (STATUS_DRAFT.equals(status) || STATUS_CANCELLED.equals(status)) {
            // Don't change draft or cancelled status
            return;
        }

        if (isFullyPaid()) {
            this.status = STATUS_PAID;
        } else if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
            this.status = STATUS_PARTIALLY_PAID;
        } else if (isOverdue()) {
            this.status = STATUS_OVERDUE;
        } else {
            this.status = STATUS_ISSUED;
        }
    }

    @Override
    public String toString() {
        return "Invoice [id=" + id + ", invoiceNumber=" + invoiceNumber +
               ", amount=" + amount + ", status=" + status + "]";
    }
}
