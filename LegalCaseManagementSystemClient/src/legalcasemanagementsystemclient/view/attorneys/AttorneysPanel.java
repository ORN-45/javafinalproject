package view.attorneys;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.rmi.RemoteException;

// Client-side DTO (model.Attorney is now the DTO)
import model.Attorney; 
import controller.AttorneyController;
import view.components.CustomTable;
import view.components.TableFilterPanel;
// import view.components.StatusIndicator; // Not used in this table
import view.util.UIConstants;
import view.util.SwingUtils;

/**
 * Panel for attorney management in the Legal Case Management System.
 */
public class AttorneysPanel extends JPanel {
    private AttorneyController attorneyController;
    private CustomTable attorneysTable;
    private AttorneyFilterPanel filterPanel;
    private List<Attorney> displayedAttorneys; // To store DTOs for local filtering & access
    
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewDetailsButton;
    
    /**
     * Constructor
     */
    public AttorneysPanel() {
        this.attorneyController = new AttorneyController();
        
        initializeUI();
        loadAttorneys();
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
        
        JLabel titleLabel = new JLabel("Attorneys Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Filter panel
        filterPanel = new AttorneyFilterPanel();
        headerPanel.add(filterPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Create the table panel with attorneys table
     * 
     * @return The table panel
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Create table
        String[] columnNames = {
            "Attorney ID", "Name", "Specialization", "Bar Number", "Email", "Phone", "Hourly Rate"
        };
        attorneysTable = new CustomTable(columnNames);
        
        // Set column widths
        attorneysTable.setColumnWidth(0, 100);  // Attorney ID
        attorneysTable.setColumnWidth(1, 200);  // Name
        attorneysTable.setColumnWidth(2, 150);  // Specialization
        attorneysTable.setColumnWidth(3, 120);  // Bar Number
        attorneysTable.setColumnWidth(4, 180);  // Email
        attorneysTable.setColumnWidth(5, 120);  // Phone
        attorneysTable.setColumnWidth(6, 100);  // Hourly Rate
        
        // Add double-click listener to open attorney details
        attorneysTable.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && attorneysTable.getSelectedRow() != -1) {
                    viewAttorneyDetails();
                }
            }
        });
        
        // Enable/disable buttons based on selection
        attorneysTable.addSelectionListener(e -> updateButtonStates());
        
        tablePanel.add(attorneysTable, BorderLayout.CENTER);
        
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
        refreshButton.addActionListener(e -> loadAttorneys());
        
        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(UIConstants.NORMAL_FONT);
        viewDetailsButton.addActionListener(e -> viewAttorneyDetails());
        
        editButton = new JButton("Edit Attorney");
        editButton.setFont(UIConstants.NORMAL_FONT);
        editButton.addActionListener(e -> editSelectedAttorney());
        
        deleteButton = new JButton("Delete Attorney");
        deleteButton.setFont(UIConstants.NORMAL_FONT);
        deleteButton.addActionListener(e -> deleteSelectedAttorney());
        
        addButton = new JButton("Add New Attorney");
        addButton.setFont(UIConstants.NORMAL_FONT);
        addButton.setBackground(UIConstants.SECONDARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addNewAttorney());
        
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
     * Load attorneys from the database
     */
    private void loadAttorneys() {
        try {
            // Clear existing data
            attorneysTable.clearTable();
            attorneysTable.clearFilters();
            
            // Get attorney DTOs from controller
            this.displayedAttorneys = attorneyController.getAllAttorneys(); // Returns List<client.model.Attorney>
            
            List<Attorney> attorneysToDisplay = new ArrayList<>(this.displayedAttorneys);
            
            String filterType = filterPanel.getSelectedFilterType();
            String searchText = filterPanel.getSearchText();
            
            if (searchText != null && !searchText.isEmpty()) {
                String lowerSearchText = searchText.toLowerCase();
                if ("Name".equals(filterType)) {
                    attorneysToDisplay = this.displayedAttorneys.stream()
                        .filter(a -> a.getFullName() != null && a.getFullName().toLowerCase().contains(lowerSearchText))
                        .collect(Collectors.toList());
                } else if ("Specialization".equals(filterType)) {
                     attorneysToDisplay = this.displayedAttorneys.stream()
                        .filter(a -> a.getSpecialization() != null && a.getSpecialization().toLowerCase().contains(lowerSearchText))
                        .collect(Collectors.toList());
                } else { // "All" - filter locally across multiple relevant fields
                    attorneysToDisplay = this.displayedAttorneys.stream()
                        .filter(a -> (a.getFullName() != null && a.getFullName().toLowerCase().contains(lowerSearchText)) ||
                                     (a.getAttorneyId() != null && a.getAttorneyId().toLowerCase().contains(lowerSearchText)) ||
                                     (a.getEmail() != null && a.getEmail().toLowerCase().contains(lowerSearchText)) ||
                                     (a.getSpecialization() != null && a.getSpecialization().toLowerCase().contains(lowerSearchText)))
                        .collect(Collectors.toList());
                }
            }
            
            // Populate table
            for (Attorney attorney : attorneysToDisplay) {
                Object[] row = {
                    attorney.getAttorneyId(),
                    attorney.getFullName(),
                    attorney.getSpecialization() != null ? attorney.getSpecialization() : "",
                    attorney.getBarNumber() != null ? attorney.getBarNumber() : "",
                    attorney.getEmail(),
                    attorney.getPhone() != null ? attorney.getPhone() : "",
                    String.format("$%.2f", attorney.getHourlyRate())
                };
                attorneysTable.addRow(row);
            }
            
            // Display a message if no attorneys found
            if (attorneysToDisplay.isEmpty() && (searchText == null || searchText.isEmpty())) {
                SwingUtils.showInfoMessage(
                    this,
                    "No attorneys found. Add a new attorney to get started.",
                    "No Attorneys"
                );
            }
            
            // Update button states
            updateButtonStates();
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error loading attorneys: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Update the enabled state of buttons based on table selection
     */
    private void updateButtonStates() {
        boolean hasSelection = attorneysTable.getSelectedRow() != -1;
        viewDetailsButton.setEnabled(hasSelection);
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
    }
    
    /**
     * View details of the selected attorney
     */
    private void viewAttorneyDetails() {
        int selectedRow = attorneysTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        // Get attorney business ID (String) from selected row
        String attorneyBusinessId = attorneysTable.getValueAt(selectedRow, 0).toString();
        Attorney selectedAttorneyDto = getAttorneyFromDisplayedList(attorneyBusinessId);

        if (selectedAttorneyDto == null) {
            SwingUtils.showErrorMessage(this, "Could not retrieve details for attorney ID: " + attorneyBusinessId, "Error");
            return;
        }
        
        try {
            // Fetch the latest DTO using its primary key (id) for the dialog
            Attorney attorneyForDialog = attorneyController.getAttorneyById(selectedAttorneyDto.getId()); 
            
            if (attorneyForDialog != null) {
                AttorneyDetailsDialog dialog = new AttorneyDetailsDialog(
                    SwingUtilities.getWindowAncestor(this), attorneyForDialog); // Pass DTO
                dialog.setVisible(true);
                
                // No explicit refresh needed here unless details dialog can modify data that affects this list
                // loadAttorneys(); 
            } else {
                 SwingUtils.showErrorMessage(this, "Attorney details not found on server.", "Error");
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error viewing attorney details: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Add a new attorney
     * This method is public so it can be called from other panels, like MainView
     */
    public void addNewAttorney() {
        try {
            // Open attorney editor dialog for a new attorney DTO
            AttorneyEditorDialog dialog = new AttorneyEditorDialog(
                SwingUtilities.getWindowAncestor(this), null); // Pass null for new attorney
            dialog.setVisible(true);
            
            if (dialog.isAttorneySaved()) {
                Attorney attorneyDtoFromDialog = dialog.getAttorney(); // This is a client.model.Attorney DTO
                if (attorneyDtoFromDialog != null) {
                    String result = attorneyController.createAttorney(attorneyDtoFromDialog);
                    SwingUtils.showInfoMessage(this, result, "Create Attorney Status");
                    loadAttorneys(); // Refresh list
                }
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error adding attorney: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Edit the selected attorney
     */
    private void editSelectedAttorney() {
        int selectedRow = attorneysTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        // Get attorney business ID from selected row
        String attorneyBusinessId = attorneysTable.getValueAt(selectedRow, 0).toString();
        Attorney attorneyToEdit = getAttorneyFromDisplayedList(attorneyBusinessId);

        if (attorneyToEdit == null) {
            SwingUtils.showErrorMessage(this, "Could not retrieve attorney for editing: " + attorneyBusinessId, "Error");
            return;
        }

        try {
            // Fetch the latest DTO to pass to the editor
            Attorney currentAttorneyDto = attorneyController.getAttorneyById(attorneyToEdit.getId());
            if (currentAttorneyDto == null) {
                 SwingUtils.showErrorMessage(this, "Attorney not found on server for editing.", "Error");
                 return;
            }

            AttorneyEditorDialog dialog = new AttorneyEditorDialog(
                SwingUtilities.getWindowAncestor(this), currentAttorneyDto); // Pass DTO
            dialog.setVisible(true);
            
            if (dialog.isAttorneySaved()) {
                Attorney editedAttorneyDto = dialog.getAttorney(); // This is a client.model.Attorney DTO
                if (editedAttorneyDto != null) {
                    // The ID should already be set correctly from the dialog if it was an edit
                    String result = attorneyController.updateAttorney(editedAttorneyDto);
                    SwingUtils.showInfoMessage(this, result, "Update Attorney Status");
                    loadAttorneys(); // Refresh list
                }
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error editing attorney: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Delete the selected attorney
     */
    private void deleteSelectedAttorney() {
        int selectedRow = attorneysTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        // Get attorney info from selected row
        String attorneyId = attorneysTable.getValueAt(selectedRow, 0).toString();
        String attorneyName = attorneysTable.getValueAt(selectedRow, 1).toString();
        
        // Confirm deletion
        boolean confirmed = SwingUtils.showConfirmDialog(
            this,
            "Are you sure you want to delete attorney '" + attorneyName + "' (" + attorneyId + ")?\n" +
            "This action cannot be undone, and all related data will be lost.",
            "Confirm Deletion"
        );
        
        if (confirmed) {
            try {
                Attorney attorneyDtoToDelete = getAttorneyFromDisplayedList(attorneyId); // Find by business ID
                if (attorneyDtoToDelete == null) {
                     SwingUtils.showErrorMessage(this, "Attorney to delete not found in local list: " + attorneyId, "Error");
                     return;
                }
                
                // AttorneyController.deleteAttorney expects a client.model.Attorney DTO
                String result = attorneyController.deleteAttorney(attorneyDtoToDelete);
                SwingUtils.showInfoMessage(this, result, "Delete Attorney Status");
                loadAttorneys(); // Refresh list
                
            } catch (RemoteException re) {
                SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
                re.printStackTrace();
            } catch (Exception e) {
                SwingUtils.showErrorMessage(this, "Error deleting attorney: " + e.getMessage(), "Application Error");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Custom filter panel for attorneys
     */
    private class AttorneyFilterPanel extends TableFilterPanel {
        /**
         * Constructor
         */
        public AttorneyFilterPanel() {
            super(
                new String[]{"All", "Name", "Specialization"},
                    
                searchText -> loadAttorneys(),
                () -> {
                    attorneysTable.clearFilters();
                    loadAttorneys();
                }
            );
        }
        
        /**
         * Apply filters based on filter type
         */
        private void applyFilters() {
            loadAttorneys();
        }
    }

    // Helper to find an Attorney DTO from the displayed list by its business ID (String)
    private Attorney getAttorneyFromDisplayedList(String attorneyBusinessId) {
        if (displayedAttorneys == null || attorneyBusinessId == null) {
            return null;
        }
        for (Attorney attorney : displayedAttorneys) {
            if (attorneyBusinessId.equals(attorney.getAttorneyId())) {
                return attorney;
            }
        }
        return null;
    }
}