package model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * Represents a legal document in the system.
 */
@Entity
public class Document implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String documentId;
    private String title;
    private String description;
    private String documentType;
    private String filePath;
    private LocalDate dateAdded;
    private LocalDate documentDate;
    private int createdBy;
    private String status;

    // ====== Relationship with Case ======
    @ManyToOne
    @JoinColumn(name = "case_id", nullable = false)
    private Case associatedCase;

    /**
     * Default constructor
     */
    public Document() {
        this.dateAdded = LocalDate.now();
        this.status = "Active";
    }

    /**
     * Constructor with essential fields
     */
    public Document(String documentId, String title, String documentType, Case associatedCase) {
        this();
        this.documentId = documentId;
        this.title = title;
        this.documentType = documentType;
        this.associatedCase = associatedCase;
    }

    /**
     * Full constructor
     */
    public Document(int id, String documentId, String title, String description,
                    String documentType, String filePath, LocalDate dateAdded,
                    LocalDate documentDate, Case associatedCase, int createdBy, String status) {
        this();
        this.id = id;
        this.documentId = documentId;
        this.title = title;
        this.description = description;
        this.documentType = documentType;
        this.filePath = filePath;
        this.dateAdded = dateAdded;
        this.documentDate = documentDate;
        this.associatedCase = associatedCase;
        this.createdBy = createdBy;
        this.status = status;
    }

    // ====== Getters and Setters ======

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Case getAssociatedCase() {
        return associatedCase;
    }

    public void setAssociatedCase(Case associatedCase) {
        this.associatedCase = associatedCase;
    }

    // ====== Utility Methods ======

    public boolean isActive() {
        return "Active".equalsIgnoreCase(status);
    }

    public String getFileExtension() {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }

        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filePath.length() - 1) {
            return filePath.substring(lastDotIndex + 1).toUpperCase();
        }

        return "";
    }

    public String getDisplayName() {
        String extension = getFileExtension();
        if (!extension.isEmpty()) {
            return title + " (" + extension + ")";
        }
        return title;
    }

    @Override
    public String toString() {
        return "Document [id=" + id + ", documentId=" + documentId + ", title=" + title +
               ", type=" + documentType + ", dateAdded=" + dateAdded + "]";
    }
}
