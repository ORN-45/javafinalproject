package view.clients;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.rmi.RemoteException;

// Client-side model
import legalcasemanagementsystemclient.model.Client;
// Server-side model (aliased)
import legalcasemanagementsystemserver.model.Client_SERVER; 

import controller.ClientController;
import view.components.CustomTable;
import view.components.TableFilterPanel;
// import view.components.StatusIndicator; // Not used in this panel's table
import view.util.UIConstants;
import view.util.SwingUtils;

/**
 * Panel for client management in the Legal Case Management System.
 */
public class ClientsPanel extends JPanel {
    private ClientController clientController;
    private CustomTable clientsTable;
    private ClientFilterPanel filterPanel;
    private List<Client> currentlyDisplayedClients; // To store mapped client-side models
    
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewDetailsButton;
    
    /**
     * Constructor
     */
    public ClientsPanel() {
        this.clientController = new ClientController();
        
        initializeUI();
        loadClients();
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
        
        JLabel titleLabel = new JLabel("Clients Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Filter panel
        filterPanel = new ClientFilterPanel();
        headerPanel.add(filterPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Create the table panel with clients table
     * 
     * @return The table panel
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Create table
        String[] columnNames = {
            "Client ID", "Name", "Type", "Contact Person", "Email", "Phone", "Registration Date"
        };
        clientsTable = new CustomTable(columnNames);
        
        // Set column widths
        clientsTable.setColumnWidth(0, 100);  // Client ID
        clientsTable.setColumnWidth(1, 200);  // Name
        clientsTable.setColumnWidth(2, 100);  // Type
        clientsTable.setColumnWidth(3, 150);  // Contact Person
        clientsTable.setColumnWidth(4, 180);  // Email
        clientsTable.setColumnWidth(5, 120);  // Phone
        clientsTable.setColumnWidth(6, 120);  // Registration Date
        
        // Add double-click listener to open client details
        clientsTable.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && clientsTable.getSelectedRow() != -1) {
                    viewClientDetails();
                }
            }
        });
        
        // Enable/disable buttons based on selection
        clientsTable.addSelectionListener(e -> updateButtonStates());
        
        tablePanel.add(clientsTable, BorderLayout.CENTER);
        
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
        refreshButton.addActionListener(e -> loadClients());
        
        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(UIConstants.NORMAL_FONT);
        viewDetailsButton.addActionListener(e -> viewClientDetails());
        
        editButton = new JButton("Edit Client");
        editButton.setFont(UIConstants.NORMAL_FONT);
        editButton.addActionListener(e -> editSelectedClient());
        
        deleteButton = new JButton("Delete Client");
        deleteButton.setFont(UIConstants.NORMAL_FONT);
        deleteButton.addActionListener(e -> deleteSelectedClient());
        
        addButton = new JButton("Add New Client");
        addButton.setFont(UIConstants.NORMAL_FONT);
        addButton.setBackground(UIConstants.SECONDARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addNewClient());
        
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
     * Load clients from the database
     */
    private void loadClients() {
        try {
            // Clear existing data
            clientsTable.clearTable();
            clientsTable.clearFilters();
            
            // Get clients from controller
            List<Client_SERVER> serverClients = clientController.getAllClients();
            List<Client> clientClients = new ArrayList<>();
            if (serverClients != null) {
                for (Client_SERVER serverClient : serverClients) {
                    clientClients.add(mapServerClientToClientClient(serverClient));
                }
            }
            
            String filterType = filterPanel.getSelectedFilterType();
            String searchText = filterPanel.getSearchText();
            List<Client> filteredClients = clientClients;

            if (searchText != null && !searchText.isEmpty()) {
                String lowerSearchText = searchText.toLowerCase();
                if ("Name".equals(filterType)) {
                    filteredClients = clientClients.stream()
                        .filter(c -> c.getName() != null && c.getName().toLowerCase().contains(lowerSearchText))
                        .collect(Collectors.toList());
                } else if ("Type".equals(filterType)) {
                     filteredClients = clientClients.stream()
                        .filter(c -> c.getClientType() != null && c.getClientType().toLowerCase().contains(lowerSearchText))
                        .collect(Collectors.toList());
                } else { // "All" - filter locally across multiple relevant fields if desired
                    filteredClients = clientClients.stream()
                        .filter(c -> (c.getName() != null && c.getName().toLowerCase().contains(lowerSearchText)) ||
                                     (c.getClientId() != null && c.getClientId().toLowerCase().contains(lowerSearchText)) ||
                                     (c.getEmail() != null && c.getEmail().toLowerCase().contains(lowerSearchText)) ||
                                     (c.getClientType() != null && c.getClientType().toLowerCase().contains(lowerSearchText)))
                        .collect(Collectors.toList());
                }
            }
            
            // Populate table
            for (Client client : filteredClients) {
                Object[] row = {
                    client.getClientId(), // This is the String client ID (business key)
                    client.getName(),
                    client.getClientType(),
                    client.getContactPerson() != null ? client.getContactPerson() : "",
                    client.getEmail(),
                    client.getPhone() != null ? client.getPhone() : "",
                    client.getRegistrationDate() != null ? client.getRegistrationDate().toString() : ""
                };
                clientsTable.addRow(row);
            }
            
            // Display a message if no clients found
            if (filteredClients.isEmpty() && (searchText == null || searchText.isEmpty())) {
                SwingUtils.showInfoMessage(
                    this,
                    "No clients found. Add a new client to get started.",
                    "No Clients"
                );
            }
            
            // Update button states
            updateButtonStates();
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with the server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error loading clients: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Update the enabled state of buttons based on table selection
     */
    private void updateButtonStates() {
        boolean hasSelection = clientsTable.getSelectedRow() != -1;
        viewDetailsButton.setEnabled(hasSelection);
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
    }
    
    /**
     * View details of the selected client
     */
    private void viewClientDetails() {
        int selectedRow = clientsTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        // Get client ID (String version like "CL001") from selected row
        String clientBusinessId = clientsTable.getValueAt(selectedRow, 0).toString();
        Client selectedClient = getClientFromDisplayedList(clientBusinessId);

        if (selectedClient == null) {
            SwingUtils.showErrorMessage(this, "Could not retrieve details for client ID: " + clientBusinessId, "Error");
            return;
        }
        
        try {
            // Fetch the latest server version of the client using its integer PK for the dialog
            Client_SERVER serverClient = clientController.getClientById(selectedClient.getId()); // uses int PK
            
            if (serverClient != null) {
                Client clientForDialog = mapServerClientToClientClient(serverClient);
                ClientDetailsDialog dialog = new ClientDetailsDialog(
                    SwingUtilities.getWindowAncestor(this), clientForDialog);
                dialog.setVisible(true);
                
                // No need to reload all clients after just viewing details typically
                // loadClients(); 
            } else {
                 SwingUtils.showErrorMessage(this, "Client details not found on server.", "Error");
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with the server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error viewing client details: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Add a new client
     * This method is public so it can be called from other panels, like MainView
     */
    public void addNewClient() {
        try {
            // Open client editor dialog for a new client
            ClientEditorDialog dialog = new ClientEditorDialog(
                SwingUtilities.getWindowAncestor(this), null); // Pass null for new client
            dialog.setVisible(true);
            
            if (dialog.isClientSaved()) {
                Client clientFromDialog = dialog.getClient();
                if (clientFromDialog != null) {
                    Client_SERVER serverClientToCreate = mapClientClientToServerClient(clientFromDialog);
                    String result = clientController.createClient(serverClientToCreate); // createClient was registerClient in service
                    SwingUtils.showInfoMessage(this, result, "Create Client Status");
                    loadClients(); // Refresh list
                }
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with the server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error adding client: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Edit the selected client
     */
    private void editSelectedClient() {
        int selectedRow = clientsTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        // Get client business ID from selected row
        String clientBusinessId = clientsTable.getValueAt(selectedRow, 0).toString();
        Client clientToEdit = getClientFromDisplayedList(clientBusinessId);

        if (clientToEdit == null) {
            SwingUtils.showErrorMessage(this, "Could not retrieve client for editing: " + clientBusinessId, "Error");
            return;
        }

        try {
            // Fetch the full server object to ensure we have the latest data and the correct int ID
            Client_SERVER serverClientCurrent = clientController.getClientById(clientToEdit.getId());
            if (serverClientCurrent == null) {
                 SwingUtils.showErrorMessage(this, "Client not found on server for editing.", "Error");
                 return;
            }
            Client clientForDialog = mapServerClientToClientClient(serverClientCurrent);

            ClientEditorDialog dialog = new ClientEditorDialog(
                SwingUtilities.getWindowAncestor(this), clientForDialog);
            dialog.setVisible(true);
            
            if (dialog.isClientSaved()) {
                Client editedClientFromDialog = dialog.getClient();
                if (editedClientFromDialog != null) {
                    Client_SERVER serverClientToUpdate = mapClientClientToServerClient(editedClientFromDialog);
                    // Important: Ensure the ID from the original server object is used for update
                    serverClientToUpdate.setId(serverClientCurrent.getId()); 
                    String result = clientController.updateClient(serverClientToUpdate);
                    SwingUtils.showInfoMessage(this, result, "Update Client Status");
                    loadClients(); // Refresh list
                }
            }
            
        } catch (RemoteException re) {
            SwingUtils.showErrorMessage(this, "Error communicating with the server: " + re.getMessage(), "Connection Error");
            re.printStackTrace();
        } catch (Exception e) {
            SwingUtils.showErrorMessage(this, "Error editing client: " + e.getMessage(), "Application Error");
            e.printStackTrace();
        }
    }
    
    /**
     * Delete the selected client
     */
    private void deleteSelectedClient() {
        int selectedRow = clientsTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        // Get client info from selected row
        String clientId = clientsTable.getValueAt(selectedRow, 0).toString();
        String clientName = clientsTable.getValueAt(selectedRow, 1).toString();
        
        // Confirm deletion
        boolean confirmed = SwingUtils.showConfirmDialog(
            this,
            "Are you sure you want to delete client '" + clientName + "' (" + clientId + ")?\n" +
            "This action cannot be undone, and all related data will be lost.",
            "Confirm Deletion"
        );
        
        if (confirmed) {
            try {
                Client clientToDelete = getClientFromDisplayedList(clientId); // Find by business ID
                if (clientToDelete == null) {
                     SwingUtils.showErrorMessage(this, "Client to delete not found in local list: " + clientId, "Error");
                     return;
                }

                // ClientController.deleteClient expects a server.model.Client object.
                // We need its primary key (id) to fetch the server object first.
                Client_SERVER serverClientToDelete = clientController.getClientById(clientToDelete.getId());
                
                if (serverClientToDelete != null) {
                    String result = clientController.deleteClient(serverClientToDelete);
                    SwingUtils.showInfoMessage(this, result, "Delete Client Status");
                    loadClients(); // Refresh list
                } else {
                     SwingUtils.showErrorMessage(this, "Client not found on server for deletion: " + clientId, "Error");
                }
            } catch (RemoteException re) {
                SwingUtils.showErrorMessage(this, "Error communicating with the server: " + re.getMessage(), "Connection Error");
                re.printStackTrace();
            } catch (Exception e) {
                SwingUtils.showErrorMessage(this, "Error deleting client: " + e.getMessage(), "Application Error");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Custom filter panel for clients
     */
    private class ClientFilterPanel extends TableFilterPanel {
        /**
         * Constructor
         */
        public ClientFilterPanel() {
            super(
                new String[]{"All", "Name", "Type"},
                    
                searchText -> loadClients(),
                () -> {
                    clientsTable.clearFilters();
                    loadClients();
                }
            );
        }
        
        /**
         * Apply filters based on filter type
         */
        private void applyFilters() {
            loadClients();
        }
    }
    
    // Helper to find a client from the currentlyDisplayClients list by its business ID (String)
    private Client getClientFromDisplayedList(String clientBusinessId) {
        if (currentlyDisplayedClients == null || clientBusinessId == null) {
            return null;
        }
        for (Client client : currentlyDisplayedClients) {
            if (clientBusinessId.equals(client.getClientId())) {
                return client;
            }
        }
        return null;
    }

    // Helper method to map server-side Client to client-side Client
    private legalcasemanagementsystemclient.model.Client mapServerClientToClientClient(legalcasemanagementsystemserver.model.Client_SERVER serverClient) {
        if (serverClient == null) return null;
        legalcasemanagementsystemclient.model.Client clientClient = new legalcasemanagementsystemclient.model.Client();
        clientClient.setId(serverClient.getId()); // int PK
        clientClient.setClientId(serverClient.getClientId()); // String business key
        clientClient.setName(serverClient.getName());
        clientClient.setClientType(serverClient.getClientType());
        clientClient.setContactPerson(serverClient.getContactPerson());
        clientClient.setEmail(serverClient.getEmail());
        clientClient.setPhone(serverClient.getPhone());
        clientClient.setAddress(serverClient.getAddress());
        clientClient.setRegistrationDate(serverClient.getRegistrationDate());
        // Cases list is not mapped for now to avoid complexity
        return clientClient;
    }

    // Helper method to map client-side Client to server-side Client
    private legalcasemanagementsystemserver.model.Client_SERVER mapClientClientToServerClient(legalcasemanagementsystemclient.model.Client clientClient) {
        if (clientClient == null) return null;
        legalcasemanagementsystemserver.model.Client_SERVER serverClient = new legalcasemanagementsystemserver.model.Client_SERVER();
        serverClient.setId(clientClient.getId()); // int PK
        serverClient.setClientId(clientClient.getClientId()); // String business key
        serverClient.setName(clientClient.getName());
        serverClient.setClientType(clientClient.getClientType());
        serverClient.setContactPerson(clientClient.getContactPerson());
        serverClient.setEmail(clientClient.getEmail());
        serverClient.setPhone(clientClient.getPhone());
        serverClient.setAddress(clientClient.getAddress());
        serverClient.setRegistrationDate(clientClient.getRegistrationDate());
        // Cases list is not mapped
        return serverClient;
    }
}