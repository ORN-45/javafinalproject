package view.clients;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList; // Added
import java.util.stream.Collectors; // Added
import java.rmi.RemoteException; // Added

// Client-side models
import model.Client; // This is legalcasemanagementsystemclient.model.Client
import model.Case;   // This is legalcasemanagementsystemclient.model.Case

// Server-side models (aliased)
import legalcasemanagementsystemserver.model.Case_SERVER; // Assuming CaseController might return this

import controller.ClientController;
import controller.CaseController; // Added for fetching cases
import view.util.UIConstants;
import view.components.CustomTable;
import view.cases.CaseDetailsDialog; // For viewing a specific case
import view.cases.CaseEditorDialog; // For creating a new case for this client


/**
 * Dialog for viewing client details and related cases.
 */
public class ClientDetailsDialog extends JDialog {
    private Client client; // This is client.model.Client (DTO)
    private ClientController clientController;
    private CaseController caseController; // Added
    
    private JPanel clientInfoPanel;
    private JPanel casesPanel;
    private CustomTable casesTable;
    
    private JButton closeButton;
    private JButton editButton;
    
    /**
     * Constructor
     * 
     * @param parent The parent window
     * @param client The client to display
     */
    public ClientDetailsDialog(Window parent, Client client) {
        super(parent, "Client Details", ModalityType.APPLICATION_MODAL);
        
        this.client = client;
        this.clientController = new ClientController();
        
        initializeUI();
        loadClientData();
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
        
        // Create tabbed pane for client information and cases
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIConstants.NORMAL_FONT);
        
        // Create client info panel
        clientInfoPanel = createClientInfoPanel();
        tabbedPane.addTab("Client Information", clientInfoPanel);
        
        // Create cases panel
        casesPanel = createCasesPanel();
        tabbedPane.addTab("Client Cases", casesPanel);
        
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
        JLabel titleLabel = new JLabel("Client Details");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        return titlePanel;
    }
    
    /**
     * Create the client information panel
     * 
     * @return The client info panel
     */
    private JPanel createClientInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create grid for client information
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
        
        // Add client information fields (will be populated in loadClientData)
        
        // Client ID
        infoGrid.add(createFieldLabel("Client ID:"), labelConstraints);
        JLabel clientIdValue = new JLabel();
        clientIdValue.setName("clientId");
        infoGrid.add(clientIdValue, valueConstraints);
        
        // Name
        infoGrid.add(createFieldLabel("Name:"), labelConstraints);
        JLabel nameValue = new JLabel();
        nameValue.setName("name");
        infoGrid.add(nameValue, valueConstraints);
        
        // Client Type
        infoGrid.add(createFieldLabel("Client Type:"), labelConstraints);
        JLabel clientTypeValue = new JLabel();
        clientTypeValue.setName("clientType");
        infoGrid.add(clientTypeValue, valueConstraints);
        
        // Contact Person
        infoGrid.add(createFieldLabel("Contact Person:"), labelConstraints);
        JLabel contactPersonValue = new JLabel();
        contactPersonValue.setName("contactPerson");
        infoGrid.add(contactPersonValue, valueConstraints);
        
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
        
        // Address
        infoGrid.add(createFieldLabel("Address:"), labelConstraints);
        JTextArea addressValue = new JTextArea(4, 30);
        addressValue.setName("address");
        addressValue.setEditable(false);
        addressValue.setLineWrap(true);
        addressValue.setWrapStyleWord(true);
        addressValue.setBackground(Color.WHITE);
        addressValue.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane addressScroll = new JScrollPane(addressValue);
        infoGrid.add(addressScroll, valueConstraints);
        
        // Registration Date
        infoGrid.add(createFieldLabel("Registration Date:"), labelConstraints);
        JLabel registrationDateValue = new JLabel();
        registrationDateValue.setName("registrationDate");
        infoGrid.add(registrationDateValue, valueConstraints);
        
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
            "Case #", "Title", "Type", "Status", "File Date", "Court"
        };
        
        casesTable = new CustomTable(columnNames);
        
        // Set column widths
        casesTable.setColumnWidth(0, 100);  // Case #
        casesTable.setColumnWidth(1, 200);  // Title
        casesTable.setColumnWidth(2, 100);  // Type
        casesTable.setColumnWidth(3, 100);  // Status
        casesTable.setColumnWidth(4, 100);  // File Date
        casesTable.setColumnWidth(5, 150);  // Court
        
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
        
        JButton newCaseButton = new JButton("New Case");
        newCaseButton.setFont(UIConstants.NORMAL_FONT);
        newCaseButton.setBackground(UIConstants.SECONDARY_COLOR);
        newCaseButton.setForeground(Color.WHITE);
        newCaseButton.addActionListener(e -> createNewCase());
        
        actionsPanel.add(viewCaseButton);
        actionsPanel.add(Box.createHorizontalStrut(10));
        actionsPanel.add(newCaseButton);
        
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
        
        editButton = new JButton("Edit Client");
        editButton.setFont(UIConstants.NORMAL_FONT);
        editButton.addActionListener(e -> editClient());
        
        closeButton = new JButton("Close");
        closeButton.setFont(UIConstants.NORMAL_FONT);
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(closeButton);
        
        return buttonPanel;
    }
    
    /**
     * Load client data into the UI
     */
    private void loadClientData() {
        try {
            // this.client is already the DTO passed in or updated.
            // Populate client information fields directly from this.client (DTO)
            if (this.client == null) {
                SwingUtils.showErrorMessage(this, "No client data to display.", "Error");
                return;
            }

            JLabel clientIdValue = (JLabel) findComponentByName(clientInfoPanel, "clientId");
            JLabel nameValue = (JLabel) findComponentByName(clientInfoPanel, "name");
            JLabel clientTypeValue = (JLabel) findComponentByName(clientInfoPanel, "clientType");
            JLabel contactPersonValue = (JLabel) findComponentByName(clientInfoPanel, "contactPerson");
            JLabel emailValue = (JLabel) findComponentByName(clientInfoPanel, "email");
            JLabel phoneValue = (JLabel) findComponentByName(clientInfoPanel, "phone");
            JTextArea addressValue = (JTextArea) findComponentByName(clientInfoPanel, "address");
            JLabel registrationDateValue = (JLabel) findComponentByName(clientInfoPanel, "registrationDate");
            
            clientIdValue.setText(client.getClientId());
            nameValue.setText(client.getName());
            clientTypeValue.setText(client.getClientType());
            contactPersonValue.setText(client.getContactPerson() != null ? client.getContactPerson() : "N/A");
            emailValue.setText(client.getEmail());
            phoneValue.setText(client.getPhone() != null ? client.getPhone() : "N/A");
            addressValue.setText(client.getAddress() != null ? client.getAddress() : "N/A");
            registrationDateValue.setText(client.getRegistrationDate() != null ? 
                                          client.getRegistrationDate().toString() : "N/A");
            
            // Load cases for this client
            casesTable.clearTable();
            if (this.caseController == null) { // Initialize if null (e.g. if constructor failed partially)
                 this.caseController = new CaseController();
            }

            List<Case_SERVER> serverCases = caseController.getAllCases(); // Fetches server models
            List<Case> clientCasesDTOs = new ArrayList<>();
            if (serverCases != null) {
                for (Case_SERVER serverCase : serverCases) {
                    // Simple mapping for display in table, assuming client.model.Case exists
                    // and has necessary setters. A proper mapServerCaseToClientCaseDTO helper is better.
                    if (serverCase.getClient() != null && serverCase.getClient().getId() == this.client.getId()) {
                        clientCasesDTOs.add(mapServerCaseToClientCaseDTO(serverCase));
                    }
                }
            }

            if (!clientCasesDTOs.isEmpty()) {
                for (Case cse : clientCasesDTOs) { // cse is now client.model.Case DTO
                    Object[] row = {
                        cse.getCaseNumber(),
                        cse.getTitle(),
                        cse.getCaseType(),
                        cse.getStatus(),
                        cse.getFileDate() != null ? cse.getFileDate().toString() : "N/A",
                        cse.getCourt() != null ? cse.getCourt() : "N/A"
                    };
                    casesTable.addRow(row);
                }
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error loading client data: " + e.getMessage(), "Application Error");
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
            JOptionPane.showMessageDialog(
                this,
                "Please select a case to view.",
                "No Case Selected",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        // Get the case number from the selected row
        String caseNumber = casesTable.getValueAt(selectedRow, 0).toString();
        
        try {
            // Look up the case in the clientCasesDTOs list (which should be a field or accessible)
            // For now, this assumes client.getCases() on the DTO is populated by loadClientData's case fetching.
            // This part is tricky as the client DTO for Client might not hold Cases directly.
            // The casesTable is populated from clientCasesDTOs, so we need to get Case DTO from there.
            
            // To get the selected Case DTO:
            // 1. Get the Case_SERVER object using caseController.getCaseByCaseNumber()
            // 2. Map it to a client.model.Case DTO.
            if (this.caseController == null) this.caseController = new CaseController();
            Case_SERVER serverCase = caseController.getCaseByCaseNumber(caseNumber); // Use existing controller method

            if (serverCase != null) {
                Case clientCaseDTO = mapServerCaseToClientCaseDTO(serverCase); // Use the new helper
                CaseDetailsDialog dialog = new CaseDetailsDialog(getOwner(), clientCaseDTO); // Pass DTO
                dialog.setVisible(true);
                
                // Potentially refresh client data if case details interaction could change client-related info
                // loadClientData(); 
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
     * Create a new case for this client
     */
    private void createNewCase() {
        if (this.client == null || this.client.getId() == 0) {
             SwingUtils.showErrorMessage(this, "Cannot create a case for an unsaved or unidentified client.", "Error");
            return;
        }
        // Create a new client.model.Case DTO, pre-populating with this client's ID
        Case newClientCaseDto = new Case();
        newClientCaseDto.setClientId(this.client.getId()); // Pre-set client ID
        // Potentially pre-set client object if Case DTO supports it and CaseEditorDialog uses it
        // newClientCaseDto.setClient(this.client); 

        CaseEditorDialog caseDialog = new CaseEditorDialog(SwingUtilities.getWindowAncestor(this), newClientCaseDto, true); // true for new case
        caseDialog.setVisible(true);

        if (caseDialog.isCaseSaved()) {
            loadClientData(); // Refresh the list of cases for this client
        }
    }
    
    /**
     * Edit this client
     */
    private void editClient() {
        try {
            // this.client is already a client.model.Client DTO
            ClientEditorDialog dialog = new ClientEditorDialog(getOwner(), this.client);
            dialog.setVisible(true);
            
            if (dialog.isClientSaved()) {
                // ClientController.getClientById returns a DTO, so this is correct.
                this.client = clientController.getClientById(this.client.getId()); 
                loadClientData(); // Reload all data for this client, including cases
            }
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error editing client: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }

    // Basic Case Server-to-Client DTO mapper (should ideally be in a shared location or CaseController)
    // This is a simplified version. Assumes client.model.Case and server.model.Case_SERVER exist.
    private Case mapServerCaseToClientCaseDTO(Case_SERVER serverCase) {
        if (serverCase == null) return null;
        Case clientCaseDTO = new Case();
        clientCaseDTO.setId(serverCase.getId());
        clientCaseDTO.setCaseNumber(serverCase.getCaseNumber());
        clientCaseDTO.setTitle(serverCase.getTitle());
        clientCaseDTO.setCaseType(serverCase.getCaseType());
        clientCaseDTO.setStatus(serverCase.getStatus());
        clientCaseDTO.setDescription(serverCase.getDescription());
        clientCaseDTO.setFileDate(serverCase.getFileDate());
        clientCaseDTO.setClosingDate(serverCase.getClosingDate());
        clientCaseDTO.setCourt(serverCase.getCourt());
        clientCaseDTO.setJudge(serverCase.getJudge());
        // For client association in Case DTO:
        if (serverCase.getClient() != null) {
            clientCaseDTO.setClientId(serverCase.getClient().getId());
            // If client.model.Case has a full Client DTO field:
            // client.model.Client caseClientDto = new client.model.Client();
            // caseClientDto.setId(serverCase.getClient().getId());
            // caseClientDto.setName(serverCase.getClient().getName()); // etc.
            // clientCaseDTO.setClient(caseClientDto);
        }
        // Other complex fields like attorneys, documents, etc., are omitted for DTO simplicity here
        return clientCaseDTO;
    }
}