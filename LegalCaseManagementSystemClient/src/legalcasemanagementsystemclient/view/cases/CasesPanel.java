package view.cases;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.List;
import java.util.ArrayList; // Added for ArrayList
import java.time.LocalDate;
import java.rmi.RemoteException; // Added for RemoteException

// Client-side model
import legalcasemanagementsystemclient.model.Case; 
// Server-side model (assuming CaseController returns this)
import legalcasemanagementsystemserver.model.Case_SERVER; // Alias for server-side Case

import controller.CaseController;
import view.components.CustomTable;
import view.components.TableFilterPanel;
import view.components.StatusIndicator;
import view.util.UIConstants;
import view.util.SwingUtils;


/**
 * Panel for case management in the Legal Case Management System.
 */
public class CasesPanel extends JPanel {
    private CaseController caseController;
    private CustomTable casesTable;
    private CaseFilterPanel filterPanel;
    
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewDetailsButton;
    
    /**
     * Constructor
     */
    public CasesPanel() {
        this.caseController = new CaseController();
        
        initializeUI();
        loadCases();
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
        
        JLabel titleLabel = new JLabel("Cases Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Filter panel
        filterPanel = new CaseFilterPanel();
        headerPanel.add(filterPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Create the table panel with cases table
     * 
     * @return The table panel
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Create table
        String[] columnNames = {
            "Case Number", "Title", "Type", "Status", "Client", "Filing Date", "Court"
        };
        casesTable = new CustomTable(columnNames);
        
        // Set column widths
        casesTable.setColumnWidth(0, 120);  // Case Number
        casesTable.setColumnWidth(1, 200);  // Title
        casesTable.setColumnWidth(2, 100);  // Type
        casesTable.setColumnWidth(3, 100);  // Status
        casesTable.setColumnWidth(4, 150);  // Client
        casesTable.setColumnWidth(5, 120);  // Filing Date
        casesTable.setColumnWidth(6, 150);  // Court
        
        // Add custom renderer for Status column
        casesTable.setColumnRenderer(3, (table, value, isSelected, hasFocus, row, column) -> {
            if (value == null) {
                return new JLabel("");
            }
            
            StatusIndicator indicator = new StatusIndicator(value.toString());
            if (isSelected) {
                indicator.setBackground(table.getSelectionBackground());
            } else {
                indicator.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250));
            }
            return indicator;
        });
        
        // Add double-click listener to open case details
        casesTable.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && casesTable.getSelectedRow() != -1) {
                    viewCaseDetails();
                }
            }
        });
        
        // Enable/disable buttons based on selection
        casesTable.addSelectionListener(e -> updateButtonStates());
        
        tablePanel.add(casesTable, BorderLayout.CENTER);
        
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
        refreshButton.addActionListener(e -> loadCases());
        
        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(UIConstants.NORMAL_FONT);
        viewDetailsButton.addActionListener(e -> viewCaseDetails());
        
        editButton = new JButton("Edit Case");
        editButton.setFont(UIConstants.NORMAL_FONT);
        editButton.addActionListener(e -> editSelectedCase());
        
        deleteButton = new JButton("Delete Case");
        deleteButton.setFont(UIConstants.NORMAL_FONT);
        deleteButton.addActionListener(e -> deleteSelectedCase());
        
        addButton = new JButton("Add New Case");
        addButton.setFont(UIConstants.NORMAL_FONT);
        addButton.setBackground(UIConstants.SECONDARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addNewCase());
        
        // Add buttons to panel
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(addButton);
        
        // Initialize button states
        viewDetailsButton.setEnabled(false);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        return buttonPanel;
    }
    
    /**
     * Load cases from the database
     */
    private void loadCases() {
        try {
            // Clear existing data
            casesTable.clearTable();
            casesTable.clearFilters();
            
            // Get cases from controller
            List<Case_SERVER> serverCases = null; // Will hold server-side models
            
            String filterType = filterPanel.getSelectedFilterType();
            String searchText = filterPanel.getSearchText();
            
            casesTable.clearFilters(); // Clear any local JTable filters before new load

            if (searchText != null && !searchText.isEmpty()) {
                switch (filterType) {
                    case "Title":
                        serverCases = caseController.findCasesByTitle(searchText);
                        break;
                    case "Status":
                        serverCases = caseController.findCasesByStatus(searchText);
                        break;
                    // For "Type", "Client", and "All" with searchText, we fetch all and apply local filtering.
                    // The CustomTable's addFilter method can be used for this post-mapping.
                    default: 
                        serverCases = caseController.getAllCases();
                        // Local filtering will be applied after mapping to clientCases
                        break;
                }
            } else {
                serverCases = caseController.getAllCases();
            }

            List<Case> clientCases = new ArrayList<>();
            if (serverCases != null) {
                for (Case_SERVER serverCase : serverCases) {
                    clientCases.add(mapServerCaseToClientCase(serverCase));
                }
            }
            
            // Populate table
            for (Case legalCase : clientCases) { // Use mapped clientCases
                String clientName = legalCase.getClient() != null ? 
                                   legalCase.getClient().getName() : 
                                   (legalCase.getClientId() > 0 ? "Client #" + legalCase.getClientId() : "N/A");
                
                Object[] row = {
                    legalCase.getCaseNumber(),
                    legalCase.getTitle(),
                    legalCase.getCaseType(),
                    legalCase.getStatus(),
                    clientName,
                    legalCase.getFileDate() != null ? legalCase.getFileDate().toString() : "",
                    legalCase.getCourt() != null ? legalCase.getCourt() : ""
                };
                casesTable.addRow(row);
            }
            
            // Display a message if no cases found
            if (clientCases.isEmpty() && (searchText == null || searchText.isEmpty())) {
                SwingUtils.showInfoMessage(
                    this,
                    "No cases found. Add a new case to get started.",
                    "No Cases"
                );
            }
            
            // Update button states
            updateButtonStates();
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with the server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error loading cases: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Update the enabled state of buttons based on table selection
     */
    private void updateButtonStates() {
        boolean hasSelection = casesTable.getSelectedRow() != -1;
        viewDetailsButton.setEnabled(hasSelection);
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
    }
    
    /**
     * View details of the selected case
     */
    private void viewCaseDetails() {
        int selectedRow = casesTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        // Get case number from selected row
        String caseNumber = casesTable.getValueAt(selectedRow, 0).toString();
        
        try {
            // Get case ID from selected row (assuming case number is unique and can be used to get ID or ID is stored)
            // For simplicity, let's assume we can get the ID to use caseController.getCaseById
            // This part might need adjustment based on how CustomTable stores original objects or IDs.
            // If CustomTable only stores string representations, fetching by caseNumber from controller is needed.
            // Let's assume caseNumber is used to fetch the server.model.Case.
            // CaseController does not have getCaseByCaseNumber anymore.
            // We need the ID. We will assume the client.model.Case object is retrievable from the table or
            // we iterate through clientCases list to find the ID.
            // For now, this is a simplification - in a real app, table would hold IDs or objects.
            
            // Simplified: Fetching by ID after finding it.
            // This is a conceptual change. The actual retrieval of ID from table needs proper implementation.
            // For now, let's assume we have the ID.
            // int caseId = ... get ID from selected row, perhaps from a hidden column or by looking up in clientCases list
            // For this example, let's assume caseNumber is still the primary way to identify from the table for now
            // and that caseController will be updated or we adapt.
            // Given CaseController.getCaseById(int) is the RMI method:
            // We need the case ID. The table displays caseNumber. We need to find the client.model.Case object
            // that corresponds to the selected row to get its ID.
            
            // This is a placeholder for getting the actual ID.
            // In a real scenario, you'd get the client.model.Case object from the table model for the selected row
            // and then use its ID.
            // For now, let's re-fetch the list and find by caseNumber to get the ID. This is inefficient.
            // A better way is to make CustomTable model aware of the underlying objects.

            // Efficiently fetch the specific case by its case number
            Case_SERVER selectedServerCase = caseController.getCaseByCaseNumber(caseNumber);

            if (selectedServerCase != null) {
                // No need for getCaseWithDetails if getCaseById already returns all necessary info.
                // Assuming getCaseById from controller returns the full server.model.Case.
                legalcasemanagementsystemclient.model.Case clientCase = mapServerCaseToClientCase(selectedServerCase);
                
                CaseDetailsDialog dialog = new CaseDetailsDialog(
                    SwingUtilities.getWindowAncestor(this), clientCase);
                dialog.setVisible(true);
                
                // Refresh the cases list after the dialog is closed - not strictly needed for view details
                // loadCases(); 
            } else {
                 SwingUtils.showErrorMessage(this, "Could not find details for case: " + caseNumber, "Error");
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with the server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error viewing case details: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Add a new case
     * This method is public so it can be called from other panels, like MainView
     */
    public void addNewCase() {
        try {
            // Open case editor dialog for a new client.model.Case
            CaseEditorDialog dialog = new CaseEditorDialog(
                SwingUtilities.getWindowAncestor(this), null); // Pass null for new case
            dialog.setVisible(true);
            
            if (dialog.isCaseSaved()) {
                legalcasemanagementsystemclient.model.Case clientCase = dialog.getCase();
                if (clientCase != null) {
                    Case_SERVER serverCaseToCreate = mapClientCaseToServerCase(clientCase);
                    String result = caseController.createCase(serverCaseToCreate);
                    SwingUtils.showInfoMessage(this, result, "Create Case Status");
                    loadCases(); // Refresh list
                }
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with the server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error adding case: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Edit the selected case
     */
    private void editSelectedCase() {
        int selectedRow = casesTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        // Get case number from selected row
        String caseNumber = casesTable.getValueAt(selectedRow, 0).toString();
        
        try {
            // Similar to viewCaseDetails, need to get the ID of the selected case.
            // This is a placeholder for getting the actual ID.
            // Efficiently fetch the specific case by its case number
            Case_SERVER selectedServerCase = caseController.getCaseByCaseNumber(caseNumber);
            
            if (selectedServerCase != null) {
                legalcasemanagementsystemclient.model.Case clientCaseToEdit = mapServerCaseToClientCase(selectedServerCase);
                
                CaseEditorDialog dialog = new CaseEditorDialog(
                    SwingUtilities.getWindowAncestor(this), clientCaseToEdit); // Pass client model to dialog
                dialog.setVisible(true);
                
                if (dialog.isCaseSaved()) {
                    legalcasemanagementsystemclient.model.Case editedClientCase = dialog.getCase();
                    if (editedClientCase != null) {
                        Case_SERVER serverCaseToUpdate = mapClientCaseToServerCase(editedClientCase);
                        // Ensure ID is set for update
                        serverCaseToUpdate.setId(selectedServerCase.getId()); 
                        String result = caseController.updateCase(serverCaseToUpdate);
                        SwingUtils.showInfoMessage(this, result, "Update Case Status");
                        loadCases(); // Refresh list
                    }
                }
            } else {
                 SwingUtils.showErrorMessage(this, "Could not find case to edit: " + caseNumber, "Error");
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with the server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error editing case: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Delete the selected case
     */
    private void deleteSelectedCase() {
        int selectedRow = casesTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        // Get case info from selected row
        String caseNumber = casesTable.getValueAt(selectedRow, 0).toString();
        String caseTitle = casesTable.getValueAt(selectedRow, 1).toString();
        
        // Confirm deletion
        boolean confirmed = SwingUtils.showConfirmDialog(
            this,
            "Are you sure you want to delete case '" + caseTitle + "' (" + caseNumber + ")?\n" +
            "This action cannot be undone, and all related data will be lost.",
            "Confirm Deletion"
        );
        
        if (confirmed) {
            try {
                // Similar to edit/view, need to get the ID of the selected case.
                // Efficiently fetch the specific case by its case number
                Case_SERVER selectedServerCase = caseController.getCaseByCaseNumber(caseNumber);

                if (selectedServerCase != null) {
                    // CaseController.deleteCase expects a server.model.Case object
                    String result = caseController.deleteCase(selectedServerCase);
                    SwingUtils.showInfoMessage(this, result, "Delete Case Status");
                    loadCases(); // Refresh list
                } else {
                    SwingUtils.showErrorMessage(this, "Could not find case to delete: " + caseNumber, "Error");
                }
                
            } catch (RemoteException re) {
                SwingUtils.showErrorMessage(this, "Error communicating with the server: " + re.getMessage(), "Connection Error");
                re.printStackTrace();
            } catch (Exception e) {
                SwingUtils.showErrorMessage(this, "Error deleting case: " + e.getMessage(), "Application Error");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Custom filter panel for cases
     */
    private class CaseFilterPanel extends TableFilterPanel {
        /**
         * Constructor
         */
        public CaseFilterPanel() {
            super(
                new String[]{"All", "Title", "Status"}, // Added "Title", keep "Status"
                // Options for "Type", "Client" would rely on local filtering if kept.
                // For now, let's keep it focused on server-supported or "All".
                searchText -> loadCases(),
                () -> {
                    casesTable.clearFilters(); // Clear JTable level filters
                    loadCases(); // Reload all data
                }
            );
        }
    }

    // Helper method to map server-side Case to client-side Case
    private legalcasemanagementsystemclient.model.Case mapServerCaseToClientCase(legalcasemanagementsystemserver.model.Case_SERVER serverCase) {
        if (serverCase == null) return null;
        legalcasemanagementsystemclient.model.Case clientCase = new legalcasemanagementsystemclient.model.Case();
        clientCase.setId(serverCase.getId());
        clientCase.setCaseNumber(serverCase.getCaseNumber());
        clientCase.setTitle(serverCase.getTitle());
        clientCase.setCaseType(serverCase.getCaseType());
        clientCase.setStatus(serverCase.getStatus());
        clientCase.setDescription(serverCase.getDescription());
        clientCase.setFileDate(serverCase.getFileDate());
        clientCase.setClosingDate(serverCase.getClosingDate());
        clientCase.setCourt(serverCase.getCourt());
        clientCase.setJudge(serverCase.getJudge());
        clientCase.setOpposingParty(serverCase.getOpposingParty());
        clientCase.setOpposingCounsel(serverCase.getOpposingCounsel());
        if (serverCase.getClient() != null) {
            clientCase.setClientId(serverCase.getClient().getId());
            // For full client object mapping, a similar mapServerClientToClientClient would be needed.
            // And ClientController would need to be used to fetch client details if required here.
            // For now, clientCase.client object remains null unless explicitly set.
        }
        // Omitting mapping for lists like attorneys, documents, events, timeEntries for brevity
        // These would require mapping each item in the list.
        return clientCase;
    }

    // Helper method to map client-side Case to server-side Case
    private legalcasemanagementsystemserver.model.Case_SERVER mapClientCaseToServerCase(legalcasemanagementsystemclient.model.Case clientCase) {
        if (clientCase == null) return null;
        legalcasemanagementsystemserver.model.Case_SERVER serverCase = new legalcasemanagementsystemserver.model.Case_SERVER();
        serverCase.setId(clientCase.getId()); // Important for updates
        serverCase.setCaseNumber(clientCase.getCaseNumber());
        serverCase.setTitle(clientCase.getTitle());
        serverCase.setCaseType(clientCase.getCaseType());
        serverCase.setStatus(clientCase.getStatus());
        serverCase.setDescription(clientCase.getDescription());
        serverCase.setFileDate(clientCase.getFileDate());
        serverCase.setClosingDate(clientCase.getClosingDate());
        serverCase.setCourt(clientCase.getCourt());
        serverCase.setJudge(clientCase.getJudge());
        serverCase.setOpposingParty(clientCase.getOpposingParty());
        serverCase.setOpposingCounsel(clientCase.getOpposingCounsel());
        
        if (clientCase.getClientId() > 0) {
            // Create a placeholder server.model.Client, set its ID.
            // The server-side service/DAO should handle resolving this to a full entity if needed.
            legalcasemanagementsystemserver.model.Client serverClient = new legalcasemanagementsystemserver.model.Client();
            serverClient.setId(clientCase.getClientId());
            serverCase.setClient(serverClient);
        }
        // Omitting mapping for lists like attorneys, documents, events, timeEntries for brevity
        return serverCase;
    }
}