package view.documents;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.rmi.RemoteException;

// Client-side DTOs
import model.Document; 
import model.Case;     
import controller.DocumentController;
import controller.CaseController;
import view.components.CustomTable;
import view.components.TableFilterPanel;
import view.components.StatusIndicator;
import view.util.UIConstants;
import view.util.SwingUtils;

/**
 * Panel for document management in the Legal Case Management System.
 */
public class DocumentsPanel extends JPanel {
    private DocumentController documentController;
    private CaseController caseController;
    private CustomTable documentsTable;
    private DocumentFilterPanel filterPanel;
    private List<Document> displayedDocuments; // Store DTOs for local access
    
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewDetailsButton;
    private JButton downloadButton;
    
    /**
     * Constructor
     */
    public DocumentsPanel() {
        this.documentController = new DocumentController();
        this.caseController = new CaseController();
        
        initializeUI();
        loadDocuments();
    }
    
    /**
     * Initialize the user interface components
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create table panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create the header panel with title and filters
     * 
     * @return The header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
        
        JLabel titleLabel = new JLabel("Documents Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Filter panel
        filterPanel = new DocumentFilterPanel();
        headerPanel.add(filterPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Create the table panel with documents table
     * 
     * @return The table panel
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Create table
        String[] columnNames = {
            "Document ID", "Title", "Type", "Case", "Date Added", "Document Date", "Status"
        };
        documentsTable = new CustomTable(columnNames);
        
        // Set column widths
        documentsTable.setColumnWidth(0, 100);  // Document ID
        documentsTable.setColumnWidth(1, 250);  // Title
        documentsTable.setColumnWidth(2, 120);  // Type
        documentsTable.setColumnWidth(3, 150);  // Case
        documentsTable.setColumnWidth(4, 100);  // Date Added
        documentsTable.setColumnWidth(5, 100);  // Document Date
        documentsTable.setColumnWidth(6, 80);   // Status
        
        // Add double-click listener to open document details
        documentsTable.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && documentsTable.getSelectedRow() != -1) {
                    viewDocumentDetails();
                }
            }
        });
        
        // Enable/disable buttons based on selection
        documentsTable.addSelectionListener(e -> updateButtonStates());
        
        // Add a custom renderer for the Status column
        documentsTable.setColumnRenderer(6, (table, value, isSelected, hasFocus, row, column) -> {
            if (value == null) {
                return new JLabel();
            }
            
            StatusIndicator indicator = new StatusIndicator(value.toString());
            if (isSelected) {
                indicator.setBackground(table.getSelectionBackground());
            }
            return indicator;
        });
        
        tablePanel.add(documentsTable, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    /**
     * Create the button panel with action buttons
     * 
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Create buttons
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(UIConstants.NORMAL_FONT);
        refreshButton.addActionListener(e -> loadDocuments());
        
        downloadButton = new JButton("Download");
        downloadButton.setFont(UIConstants.NORMAL_FONT);
        downloadButton.addActionListener(e -> downloadSelectedDocument());
        
        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(UIConstants.NORMAL_FONT);
        viewDetailsButton.addActionListener(e -> viewDocumentDetails());
        
        editButton = new JButton("Edit Document");
        editButton.setFont(UIConstants.NORMAL_FONT);
        editButton.addActionListener(e -> editSelectedDocument());
        
        deleteButton = new JButton("Delete Document");
        deleteButton.setFont(UIConstants.NORMAL_FONT);
        deleteButton.addActionListener(e -> deleteSelectedDocument());
        
        addButton = new JButton("Upload Document");
        addButton.setFont(UIConstants.NORMAL_FONT);
        addButton.setBackground(UIConstants.SECONDARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> uploadNewDocument());
        
        // Add buttons to panel
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(downloadButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(addButton);
        
        // Initialize button states
        downloadButton.setEnabled(false);
        viewDetailsButton.setEnabled(false);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        return buttonPanel;
    }
    
    /**
     * Load documents from the database
     */
    private void loadDocuments() {
        try {
            // Clear existing data
            documentsTable.clearTable();
            documentsTable.clearFilters();
            
            // Get documents from controller (DocumentController now returns List<client.model.Document> DTOs)
            this.displayedDocuments = new ArrayList<>(); // Clear previous
            
            String filterType = filterPanel.getSelectedFilterType();
            String searchText = filterPanel.getSearchText().trim();
            
            if (searchText != null && !searchText.isEmpty()) {
                if ("Title".equals(filterType)) {
                    this.displayedDocuments = documentController.findDocumentsByTitle(searchText);
                } else if ("Type".equals(filterType)) {
                    this.displayedDocuments = documentController.findDocumentsByType(searchText);
                } else if ("Case".equals(filterType)) {
                    // CaseController.getAllCases() returns List<client.model.Case> DTOs
                    List<Case> allCases = caseController.getAllCases(); 
                    List<Case> matchingCases = allCases.stream()
                        .filter(c -> (c.getCaseNumber() != null && c.getCaseNumber().toLowerCase().contains(searchText.toLowerCase())) ||
                                     (c.getTitle() != null && c.getTitle().toLowerCase().contains(searchText.toLowerCase())))
                        .collect(Collectors.toList());
                    
                    for (Case legalCase : matchingCases) {
                        this.displayedDocuments.addAll(documentController.getDocumentsByCaseId(legalCase.getId()));
                    }
                    // Remove duplicates if a document is in multiple matching cases (though unlikely with current model)
                    this.displayedDocuments = this.displayedDocuments.stream().distinct().collect(Collectors.toList());
                } else { // "All"
                    this.displayedDocuments = documentController.getAllDocuments();
                    // Apply local filtering for "All" if search text is present
                    String lowerSearchText = searchText.toLowerCase();
                    this.displayedDocuments = this.displayedDocuments.stream()
                        .filter(d -> (d.getTitle() != null && d.getTitle().toLowerCase().contains(lowerSearchText)) ||
                                     (d.getDocumentType() != null && d.getDocumentType().toLowerCase().contains(lowerSearchText)) ||
                                     (d.getDocumentId() != null && d.getDocumentId().toLowerCase().contains(lowerSearchText)) ||
                                     (d.getFileName() != null && d.getFileName().toLowerCase().contains(lowerSearchText)))
                        .collect(Collectors.toList());
                }
            } else {
                this.displayedDocuments = documentController.getAllDocuments();
            }
            
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            if (this.displayedDocuments != null) {
                for (Document documentDto : this.displayedDocuments) {
                    String caseInfo = "N/A";
                    if (documentDto.getCaseId() > 0) {
                        try {
                            // Fetch case DTO for display - can be inefficient if many unique cases
                            // Consider caching case DTOs or adding caseNumber/title to Document DTO
                            Case caseDto = caseController.getCaseById(documentDto.getCaseId());
                            if (caseDto != null) {
                                caseInfo = caseDto.getCaseNumber() + " - " + caseDto.getTitle();
                            } else {
                                caseInfo = "Case ID: " + documentDto.getCaseId() + " (Not found)";
                            }
                        } catch (RemoteException e) {
                            System.err.println("Error fetching case details for doc ID " + documentDto.getId() + ": " + e.getMessage());
                            caseInfo = "Case ID: " + documentDto.getCaseId() + " (Error)";
                        }
                    }
                    
                    Object[] row = {
                        documentDto.getDocumentId(), // Business Key
                        documentDto.getTitle(),
                        documentDto.getDocumentType(),
                        caseInfo,
                        documentDto.getDateAdded() != null ? documentDto.getDateAdded().format(dateFormatter) : "N/A",
                        documentDto.getDocumentDate() != null ? documentDto.getDocumentDate().format(dateFormatter) : "N/A",
                        documentDto.getStatus()
                    };
                    documentsTable.addRow(row);
                }
            }
            
            if (this.displayedDocuments == null || this.displayedDocuments.isEmpty()) {
                 if (searchText == null || searchText.isEmpty()) {
                    SwingUtils.showInfoMessage(this, "No documents found. Upload a new document to get started.", "No Documents");
                } else {
                    SwingUtils.showInfoMessage(this, "No documents match your filter criteria.", "Filter Results");
                }
            }
            updateButtonStates();
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) { // Catch other unexpected errors
            SwingUtils.showErrorMessage(this, "An unexpected error occurred: " + e.getMessage(), "Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Update the enabled state of buttons based on table selection
     */
    private void updateButtonStates() {
        boolean hasSelection = documentsTable.getSelectedRow() != -1;
        downloadButton.setEnabled(hasSelection);
        viewDetailsButton.setEnabled(hasSelection);
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
    }
    
    /**
     * View details of the selected document
     */
    private void viewDocumentDetails() {
        int selectedRow = documentsTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        // Get document DTO from the displayed list
        Document selectedDocumentDto = getDTOFromSelectedRow();
        if (selectedDocumentDto == null) return;
        
        try {
            // For details, we might want to fetch again to ensure latest data, though not strictly necessary if list is fresh.
            // DocumentController.getDocumentById returns DTO (without content by default)
            Document documentForDialog = documentController.getDocumentById(selectedDocumentDto.getId());
            
            if (documentForDialog != null) {
                DocumentDetailsDialog dialog = new DocumentDetailsDialog(
                    SwingUtilities.getWindowAncestor(this), documentForDialog); // Pass DTO
                dialog.setVisible(true);
                
                // No explicit refresh usually needed after just viewing details
            } else {
                SwingUtils.showErrorMessage(this, "Could not retrieve document details from server.", "Error");
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error viewing document details: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Upload a new document
     * This method is public so it can be called from other panels, like MainView
     */
    public void uploadNewDocument() {
        try {
            // Show file chooser dialog
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                
                // Open document editor dialog
                DocumentEditorDialog dialog = new DocumentEditorDialog(
                    SwingUtilities.getWindowAncestor(this), null, selectedFile);
                dialog.setVisible(true);
                
                // Refresh the documents list if a document was added
                if (dialog.isDocumentSaved()) {
                    loadDocuments();
                }
            }
            
        } catch (Exception e) {
            SwingUtils.showErrorMessage(
                this,
                "Error uploading document: " + e.getMessage(),
                "Upload Error"
            );
            e.printStackTrace();
        }
    }
    
    /**
     * Edit the selected document
     */
    private void editSelectedDocument() {
        int selectedRow = documentsTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        Document selectedDocumentDto = getDTOFromSelectedRow();
        if (selectedDocumentDto == null) return;
        
        try {
            // Fetch the latest DTO for editing
            Document documentToEdit = documentController.getDocumentById(selectedDocumentDto.getId());
            
            if (documentToEdit != null) {
                DocumentEditorDialog dialog = new DocumentEditorDialog(
                    SwingUtilities.getWindowAncestor(this), documentToEdit, null); // Pass DTO
                dialog.setVisible(true);
                
                // Refresh the documents list if the document was updated
                if (dialog.isDocumentSaved()) {
                    loadDocuments();
                }
            } else {
                 SwingUtils.showErrorMessage(this, "Could not retrieve document for editing.", "Error");
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error editing document: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Delete the selected document
     */
    private void deleteSelectedDocument() {
        int selectedRow = documentsTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        // Get document info from selected row
        String documentId = documentsTable.getValueAt(selectedRow, 0).toString();
        String documentTitle = documentsTable.getValueAt(selectedRow, 1).toString();
        
        // Confirm deletion
        boolean confirmed = SwingUtils.showConfirmDialog(
            this,
            "Are you sure you want to delete document '" + documentTitle + "' (" + documentId + ")?\n" +
            "This action cannot be undone, and the file will be permanently deleted.",
            "Confirm Deletion"
        );
        
        if (confirmed) {
            try {
                Document documentDtoToDelete = getDTOFromSelectedRow();
                if (documentDtoToDelete == null) {
                     SwingUtils.showErrorMessage(this, "Document to delete not found in local list.", "Error");
                     return;
                }
                // DocumentController.deleteDocument expects a DTO
                String resultMessage = documentController.deleteDocument(documentDtoToDelete);
                
                if (resultMessage != null && resultMessage.toLowerCase().contains("success")) {
                    SwingUtils.showInfoMessage(this, resultMessage, "Delete Successful");
                    loadDocuments(); // Refresh list
                } else {
                    SwingUtils.showErrorMessage(this, resultMessage != null ? resultMessage : "Failed to delete document.", "Deletion Error");
                }
                
            } catch (RemoteException re) {
                SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
                re.printStackTrace();
            } catch (Exception e) {
                SwingUtils.showErrorMessage(this, "Error deleting document: " + e.getMessage(), "Application Error");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Download the selected document
     */
    private void downloadSelectedDocument() {
        int selectedRow = documentsTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        Document selectedDocumentDto = getDTOFromSelectedRow();
        if (selectedDocumentDto == null) return;
        
        try {
            // DocumentController.getDocumentById returns DTO (without content by default)
            // We need the content, so call getDocumentContent specifically.
            byte[] content = documentController.getDocumentContent(selectedDocumentDto.getId());
            
            if (content != null && content.length > 0) {
                JFileChooser fileChooser = new JFileChooser();
                // Use fileName from DTO for the default save name
                fileChooser.setSelectedFile(new File(selectedDocumentDto.getFileName() != null ? selectedDocumentDto.getFileName() : "document_export"));
                int result = fileChooser.showSaveDialog(this);
                
                if (result == JFileChooser.APPROVE_OPTION) {
                    File outputFile = fileChooser.getSelectedFile();
                    try {
                        java.nio.file.Files.write(outputFile.toPath(), content);
                        SwingUtils.showInfoMessage(this, "Document downloaded successfully to: " + outputFile.getAbsolutePath(), "Success");
                    } catch (java.io.IOException ioe) {
                        SwingUtils.showErrorMessage(this, "Error saving document: " + ioe.getMessage(), "Download Error");
                        ioe.printStackTrace();
                    }
                }
            } else {
                SwingUtils.showErrorMessage(this, "No content available to download for this document.", "Download Error");
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error downloading document: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Custom filter panel for documents
     */
    private class DocumentFilterPanel extends TableFilterPanel {
        /**
         * Constructor
         */
        public DocumentFilterPanel() {
            super(
                new String[]{"All", "Title", "Type", "Case"},
                    
                searchText -> loadDocuments(),
                () -> {
                    documentsTable.clearFilters();
                    loadDocuments();
                }
            );
        }
        
        /**
         * Apply filters based on filter type
         */
        private void applyFilters() {
            loadDocuments();
        }
    }

    // Helper to get the DTO from the selected row using displayedDocuments list
    private Document getDTOFromSelectedRow() {
        int selectedRow = documentsTable.getSelectedRow();
        if (selectedRow == -1 || displayedDocuments == null || selectedRow >= displayedDocuments.size()) {
            // This check might be problematic if table is sorted/filtered client-side independently of displayedDocuments
            // A more robust way is to ensure CustomTable can return the original DTO object.
            // For now, if table view index directly maps to displayedDocuments index:
            if (selectedRow != -1 && displayedDocuments != null && selectedRow < displayedDocuments.size() 
                && documentsTable.convertRowIndexToModel(selectedRow) < displayedDocuments.size()) { // Check model index too
                 // Assuming displayedDocuments is exactly what's in the table, in the same order.
                 // This is a simplification. Real-world tables with sorting/filtering need more robust model mapping.
                 // For now, we'll assume direct index correspondence after data load.
                 // A better solution would be to get the business key (Document ID string) from table 
                 // and find it in displayedDocuments.
                String docBusinessIdFromTable = (String) documentsTable.getValueAt(selectedRow, 0);
                return displayedDocuments.stream()
                                         .filter(d -> d.getDocumentId().equals(docBusinessIdFromTable))
                                         .findFirst()
                                         .orElse(null);
            }
            return null;
        }
        // This simple indexing is only safe if displayedDocuments is the direct source for table model
        // and no client-side sorting/filtering has changed the view index relative to model index.
        // return displayedDocuments.get(documentsTable.convertRowIndexToModel(selectedRow));
        // Fallback to fetching by business ID string from table:
        String docBusinessIdFromTable = (String) documentsTable.getValueAt(selectedRow, 0);
        return displayedDocuments.stream()
                                 .filter(d -> d.getDocumentId().equals(docBusinessIdFromTable))
                                 .findFirst()
                                 .orElse(null);

    }
}