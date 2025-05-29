package controller;

import dao.ClientDao;
import model.Client;

import java.util.List;

/**
 * Controller class for Client entity operations.
 */
public class ClientController {
    private ClientDao clientDao;

    public ClientController() {
        this.clientDao = new ClientDao();
    }

    /**
     * Register a new client.
     */
    public String createClient(Client client) {
        if (client == null) {
            return "Client cannot be null";
        }
        return clientDao.registerClient(client);
    }

    /**
     * Update an existing client.
     */
    public String updateClient(Client client) {
        if (client == null) {
            return "Client cannot be null";
        }
        return clientDao.updateClient(client);
    }

    /**
     * Delete a client.
     */
    public String deleteClient(Client client) {
        if (client == null) {
            return "Client cannot be null";
        }
        return clientDao.deleteClient(client);
    }

    /**
     * Retrieve a client by ID.
     */
    public Client getClientById(int clientId) {
        Client temp = new Client();
        temp.setId(clientId);
        return clientDao.retrieveById(temp);
    }

    /**
     * Retrieve all clients.
     */
    public List<Client> getAllClients() {
        return clientDao.retrieveAll();
    }
}
