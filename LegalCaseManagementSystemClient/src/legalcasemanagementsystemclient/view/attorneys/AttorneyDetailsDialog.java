package view.attorneys;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.rmi.RemoteException;

// Client-side DTOs
import model.Attorney; // This is client.model.Attorney
import model.Case;     // This is client.model.Case 

// Server-side Models (aliased)
import legalcasemanagementsystemserver.model.Case_SERVER;
import legalcasemanagementsystemserver.model.Attorney_SERVER; // For mapping within Case DTO if needed

import controller.AttorneyController;
import controller.CaseController; // To fetch cases
import view.util.UIConstants;
import view.components.CustomTable;
import view.cases.CaseDetailsDialog; // For viewing a specific case
import view.util.SwingUtils;


/**
 * Dialog for viewing attorney details and related cases.
 */
public class AttorneyDetailsDialog extends JDialog {
    private Attorney attorney; // This is client.model.Attorney DTO
    private AttorneyController attorneyController;
    private CaseController caseController; // Added
    
    private JPanel attorneyInfoPanel;
    private JPanel casesPanel;
    private CustomTable casesTable;
    
    private JButton closeButton;
    private JButton editButton;
    
    /**
     * Constructor
     * 
     * @param parent The parent window
     * @param attorney The attorney to display
     */
    public AttorneyDetailsDialog(Window parent, Attorney attorney) {
        super(parent, "Attorney Details", Dialog.ModalityType.APPLICATION_MODAL);
        
        this.attorney = attorney;
        this.attorneyController = new AttorneyController();
        
        initializeUI();
        loadAttorneyData();
    }
    
    /**
     * Initialize the user interface components
     */
    private void initializeUI() {
        setSize(800, 600);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        // Create title panel
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        // Create tabbed pane for attorney information and cases
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIConstants.NORMAL_FONT);
        
        // Create attorney info panel
        attorneyInfoPanel = createAttorneyInfoPanel();
        tabbedPane.addTab("Attorney Information", attorneyInfoPanel);
        
        // Create cases panel
        casesPanel = createCasesPanel();
        tabbedPane.addTab("Assigned Cases", casesPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create the title panel
     * 
     * @return The title panel
     */
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UIConstants.PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Title label
        JLabel titleLabel = new JLabel("Attorney Details");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        return titlePanel;
    }
    
    /**
     * Create the attorney information panel
     * 
     * @return The attorney info panel
     */
    private JPanel createAttorneyInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create grid for attorney information
        JPanel infoGrid = new JPanel(new GridBagLayout());
        infoGrid.setBackground(Color.WHITE);
        
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = GridBagConstraints.RELATIVE;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(5, 5, 5, 15);
        
        GridBagConstraints valueConstraints = new GridBagConstraints();
        valueConstraints.gridx = 1;
        valueConstraints.gridy = GridBagConstraints.RELATIVE;
        valueConstraints.anchor = GridBagConstraints.WEST;
        valueConstraints.weightx = 1.0;
        valueConstraints.fill = GridBagConstraints.HORIZONTAL;
        valueConstraints.insets = new Insets(5, 5, 5, 5);
        
        // Add attorney information fields (will be populated in loadAttorneyData)
        
        // Attorney ID
        infoGrid.add(createFieldLabel("Attorney ID:"), labelConstraints);
        JLabel attorneyIdValue = new JLabel();
        attorneyIdValue.setName("attorneyId");
        infoGrid.add(attorneyIdValue, valueConstraints);
        
        // Full Name
        infoGrid.add(createFieldLabel("Full Name:"), labelConstraints);
        JLabel nameValue = new JLabel();
        nameValue.setName("name");
        infoGrid.add(nameValue, valueConstraints);
        
        // Specialization
        infoGrid.add(createFieldLabel("Specialization:"), labelConstraints);
        JLabel specializationValue = new JLabel();
        specializationValue.setName("specialization");
        infoGrid.add(specializationValue, valueConstraints);
        
        // Bar Number
        infoGrid.add(createFieldLabel("Bar Number:"), labelConstraints);
        JLabel barNumberValue = new JLabel();
        barNumberValue.setName("barNumber");
        infoGrid.add(barNumberValue, valueConstraints);
        
        // Email
        infoGrid.add(createFieldLabel("Email:"), labelConstraints);
        JLabel emailValue = new JLabel();
        emailValue.setName("email");
        infoGrid.add(emailValue, valueConstraints);
        
        // Phone
        infoGrid.add(createFieldLabel("Phone:"), labelConstraints);
        JLabel phoneValue = new JLabel();
        phoneValue.setName("phone");
        infoGrid.add(phoneValue, valueConstraints);
        
        // Hourly Rate
        infoGrid.add(createFieldLabel("Hourly Rate:"), labelConstraints);
        JLabel hourlyRateValue = new JLabel();
        hourlyRateValue.setName("hourlyRate");
        infoGrid.add(hourlyRateValue, valueConstraints);
        
        // Add the info grid to the panel
        panel.add(infoGrid, BorderLayout.NORTH);
        
        return panel;
    }
    
    /**
     * Create a field label with consistent styling
     * 
     * @param text The label text
     * @return The styled label
     */
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.LABEL_FONT);
        label.setForeground(UIConstants.PRIMARY_COLOR);
        return label;
    }
    
    /**
     * Create the cases panel with a table of cases
     * 
     * @return The cases panel
     */
    private JPanel createCasesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create table for cases
        String[] columnNames = {
            "Case #", "Title", "Type", "Status", "File Date", "Client"
        };
        
        casesTable = new CustomTable(columnNames);
        
        // Set column widths
        casesTable.setColumnWidth(0, 100);  // Case #
        casesTable.setColumnWidth(1, 200);  // Title
        casesTable.setColumnWidth(2, 100);  // Type
        casesTable.setColumnWidth(3, 100);  // Status
        casesTable.setColumnWidth(4, 100);  // File Date
        casesTable.setColumnWidth(5, 150);  // Client
        
        // Add double-click listener to open case details
        casesTable.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && casesTable.getSelectedRow() != -1) {
                    openCaseDetails();
                }
            }
        });
        
        // Add actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JButton viewCaseButton = new JButton("View Case");
        viewCaseButton.setFont(UIConstants.NORMAL_FONT);
        viewCaseButton.addActionListener(e -> openCaseDetails());
        
        actionsPanel.add(viewCaseButton);
        
        // Add components to panel
        panel.add(actionsPanel, BorderLayout.NORTH);
        panel.add(casesTable, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create the button panel
     * 
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        buttonPanel.setBackground(Color.WHITE);
        
        editButton = new JButton("Edit Attorney");
        editButton.setFont(UIConstants.NORMAL_FONT);
        editButton.addActionListener(e -> editAttorney());
        
        closeButton = new JButton("Close");
        closeButton.setFont(UIConstants.NORMAL_FONT);
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(closeButton);
        
        return buttonPanel;
    }
    
    /**
     * Load attorney data into the UI
     */
    private void loadAttorneyData() {
        try {
            // this.attorney is the DTO passed in or updated.
            // Populate attorney information fields directly from this.attorney DTO
            if (this.attorney == null) {
                SwingUtils.showErrorMessage(this, "No attorney data to display.", "Error");
                return;
            }

            JLabel attorneyIdValue = (JLabel) findComponentByName(attorneyInfoPanel, "attorneyId");
            JLabel nameValue = (JLabel) findComponentByName(attorneyInfoPanel, "name");
            JLabel specializationValue = (JLabel) findComponentByName(attorneyInfoPanel, "specialization");
            JLabel barNumberValue = (JLabel) findComponentByName(attorneyInfoPanel, "barNumber");
            JLabel emailValue = (JLabel) findComponentByName(attorneyInfoPanel, "email");
            JLabel phoneValue = (JLabel) findComponentByName(attorneyInfoPanel, "phone");
            JLabel hourlyRateValue = (JLabel) findComponentByName(attorneyInfoPanel, "hourlyRate");
            
            attorneyIdValue.setText(attorney.getAttorneyId());
            nameValue.setText(attorney.getFullName());
            specializationValue.setText(attorney.getSpecialization() != null ? attorney.getSpecialization() : "N/A");
            barNumberValue.setText(attorney.getBarNumber() != null ? attorney.getBarNumber() : "N/A");
            emailValue.setText(attorney.getEmail());
            phoneValue.setText(attorney.getPhone() != null ? attorney.getPhone() : "N/A");
            hourlyRateValue.setText(String.format("$%.2f", attorney.getHourlyRate()));
            
            // Load cases assigned to this attorney
            casesTable.clearTable();
            if (this.caseController == null) {
                this.caseController = new CaseController();
            }

            // CaseController.getAllCases() returns List<server.model.Case_SERVER>
            // We need to map these to client.model.Case DTOs and then filter.
            List<Case_SERVER> serverCases = caseController.getAllCases();
            List<Case> attorneyCaseDTOs = new ArrayList<>();

            if (serverCases != null) {
                for (Case_SERVER serverCase : serverCases) {
                    if (serverCase.getAttorneys() != null) {
                        for (legalcasemanagementsystemserver.model.Attorney serverAttInCase : serverCase.getAttorneys()) {
                            if (serverAttInCase.getId() == this.attorney.getId()) {
                                attorneyCaseDTOs.add(mapServerCaseToClientCaseDTO(serverCase));
                                break; // Found this attorney in this case
                            }
                        }
                    }
                }
            }

            if (!attorneyCaseDTOs.isEmpty()) {
                for (Case cse : attorneyCaseDTOs) { // cse is now client.model.Case DTO
                    String clientName = cse.getClient() != null ? cse.getClient().getName() : 
                                       (cse.getClientId() > 0 ? "Client #" + cse.getClientId() : "N/A");
                    Object[] row = {
                        cse.getCaseNumber(),
                        cse.getTitle(),
                        cse.getCaseType(),
                        cse.getStatus(),
                        cse.getFileDate() != null ? cse.getFileDate().toString() : "N/A",
                        clientName
                    };
                    casesTable.addRow(row);
                }
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error loading attorney data: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Find a component by name within a parent container
     * 
     * @param container The container to search in
     * @param name The component name to find
     * @return The found component or null
     */
    private Component findComponentByName(Container container, String name) {
        for (Component component : container.getComponents()) {
            if (name.equals(component.getName())) {
                return component;
            }
            
            if (component instanceof Container) {
                Component found = findComponentByName((Container) component, name);
                if (found != null) {
                    return found;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Open the details dialog for the selected case
     */
    private void openCaseDetails() {
        int selectedRow = casesTable.getSelectedRow();
        if (selectedRow == -1) {
            SwingUtils.showInfoMessage(
                this,
                "Please select a case to view.",
                "No Case Selected"
            );
            return;
        }
        
        // Get the case number from the selected row
        String caseNumber = casesTable.getValueAt(selectedRow, 0).toString();
        
        try {
            // To get the selected Case DTO for CaseDetailsDialog:
            // 1. Use caseController.getCaseByCaseNumber (which returns server.model.Case_SERVER)
            // 2. Map it to a client.model.Case DTO.
            if (this.caseController == null) this.caseController = new CaseController();
            Case_SERVER serverCase = caseController.getCaseByCaseNumber(caseNumber);

            if (serverCase != null) {
                Case clientCaseDTO = mapServerCaseToClientCaseDTO(serverCase);
                CaseDetailsDialog dialog = new CaseDetailsDialog(getOwner(), clientCaseDTO); // Pass DTO
                dialog.setVisible(true);
                
                // Refresh attorney data if case details interaction could change attorney-related info
                // (e.g. if a case was unassigned from attorney, though that's not a feature here)
                // loadAttorneyData(); 
            } else {
                SwingUtils.showErrorMessage(this, "Could not find details for case: " + caseNumber, "Error");
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error opening case details: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Edit this attorney
     */
    private void editAttorney() {
        try {
            // Open attorney editor dialog
            AttorneyEditorDialog dialog = new AttorneyEditorDialog(getOwner(), attorney);
            dialog.setVisible(true);
            
            // Refresh data if attorney was saved
            if (dialog.isAttorneySaved()) {
                // AttorneyController.getAttorneyById returns a DTO, so this is correct.
                this.attorney = attorneyController.getAttorneyById(this.attorney.getId()); 
                loadAttorneyData(); // Reload all data for this attorney, including cases
            }
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error editing attorney: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }

    // Basic Case Server-to-Client DTO mapper (should ideally be in a shared location or CaseController)
    // This is a simplified version. Assumes client.model.Case and server.model.Case_SERVER exist.
    private Case mapServerCaseToClientCaseDTO(Case_SERVER serverCase) {
        if (serverCase == null) return null;
        Case clientCaseDTO = new Case(); // client.model.Case
        clientCaseDTO.setId(serverCase.getId());
        clientCaseDTO.setCaseNumber(serverCase.getCaseNumber());
        clientCaseDTO.setTitle(serverCase.getTitle());
        clientCaseDTO.setCaseType(serverCase.getCaseType());
        clientCaseDTO.setStatus(serverCase.getStatus());
        // clientCaseDTO.setDescription(serverCase.getDescription()); // Assuming client.model.Case has these
        clientCaseDTO.setFileDate(serverCase.getFileDate());
        // clientCaseDTO.setClosingDate(serverCase.getClosingDate());
        clientCaseDTO.setCourt(serverCase.getCourt());
        // clientCaseDTO.setJudge(serverCase.getJudge());
        
        if (serverCase.getClient() != null) {
            clientCaseDTO.setClientId(serverCase.getClient().getId());
            // If client.model.Case DTO has a full client.model.Client DTO field:
            // model.Client caseClientDto = new model.Client(); // client.model.Client
            // caseClientDto.setId(serverCase.getClient().getId());
            // caseClientDto.setName(serverCase.getClient().getName()); 
            // clientCaseDTO.setClient(caseClientDto);
        }
        
        // For attorneys in a case DTO
        if (serverCase.getAttorneys() != null && clientCaseDTO.getAttorneys() != null) { // Assuming clientCaseDTO has getAttorneys()
            for (Attorney_SERVER serverAtt : serverCase.getAttorneys()) {
                Attorney clientAtt = new Attorney(); // client.model.Attorney
                clientAtt.setId(serverAtt.getId());
                clientAtt.setFirstName(serverAtt.getFirstName());
                clientAtt.setLastName(serverAtt.getLastName());
                // ... other attorney fields ...
                clientCaseDTO.getAttorneys().add(clientAtt);
            }
        }
        return clientCaseDTO;
    }
}