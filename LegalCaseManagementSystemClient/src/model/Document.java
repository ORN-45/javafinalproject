package model; // Assuming client.model package

import java.io.Serializable;
import java.time.LocalDate;

public class Document implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String documentId; // Business key
    private String title;
    private String description;
    private String documentType; // Category/type (e.g., "Pleading", "Contract")
    private String filePath;     // May become less relevant if storing content in DB
    private LocalDate dateAdded;
    private LocalDate documentDate; // Date on the document itself
    private int createdBy;       // User ID of creator
    private String status;       // E.g., "Active", "Archived"
    private int caseId;          // To associate with a Case

    // New fields for file content and metadata
    private String fileName;
    private String fileType;     // MIME type
    private byte[] fileContent;  // Actual file content

    public Document() {
        this.dateAdded = LocalDate.now();
        this.status = "Active";
    }

    // Example constructor (can be expanded)
    public Document(int id, String documentId, String title, String documentType, int caseId, String fileName, String fileType) {
        this();
        this.id = id;
        this.documentId = documentId;
        this.title = title;
        this.documentType = documentType;
        this.caseId = caseId;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    // Getters and Setters for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public LocalDate getDateAdded() { return dateAdded; }
    public void setDateAdded(LocalDate dateAdded) { this.dateAdded = dateAdded; }
    public LocalDate getDocumentDate() { return documentDate; }
    public void setDocumentDate(LocalDate documentDate) { this.documentDate = documentDate; }
    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getCaseId() { return caseId; }
    public void setCaseId(int caseId) { this.caseId = caseId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public byte[] getFileContent() { return fileContent; }
    public void setFileContent(byte[] fileContent) { this.fileContent = fileContent; }

    @Override
    public String toString() {
        return "DocumentDTO [id=" + id + ", title=" + title + ", fileName=" + fileName + "]";
    }
}
