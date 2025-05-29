package controller;

// Client-side DTO
import model.Client; // Renaming to Client_DTO for clarity if needed, but fine as is if client.model.Client is distinct
// Server-side Model (needs to be available in client's classpath for RMI, or use a shared library)
import legalcasemanagementsystemserver.model.Client_SERVER; // Using an alias for server model
import service.remote.ClientService; 

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ArrayList;
import java.rmi.RemoteException; 

/**
 * Controller class for Client entity operations using RMI with DTO mapping.
 */
public class ClientController {
    private ClientService clientService; // RMI service stub

    public ClientController() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            this.clientService = (ClientService) registry.lookup("ClientService");
        } catch (Exception e) {
            System.err.println("ClientController: Error looking up ClientService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Mapping Helper Methods ---
    private Client_SERVER mapClientDtoToServerModel(Client clientDto) {
        if (clientDto == null) return null;
        Client_SERVER serverModel = new Client_SERVER();
        serverModel.setId(clientDto.getId());
        serverModel.setClientId(clientDto.getClientId());
        serverModel.setName(clientDto.getName());
        serverModel.setContactPerson(clientDto.getContactPerson());
        serverModel.setEmail(clientDto.getEmail());
        serverModel.setPhone(clientDto.getPhone());
        serverModel.setAddress(clientDto.getAddress());
        serverModel.setClientType(clientDto.getClientType());
        serverModel.setRegistrationDate(clientDto.getRegistrationDate());
        // Complex associations like List<Case> or Attorney not mapped in DTO->Server for now
        return serverModel;
    }

    private Client mapServerModelToClientDto(Client_SERVER serverModel) {
        if (serverModel == null) return null;
        Client clientDto = new Client();
        clientDto.setId(serverModel.getId());
        clientDto.setClientId(serverModel.getClientId());
        clientDto.setName(serverModel.getName());
        clientDto.setContactPerson(serverModel.getContactPerson());
        clientDto.setEmail(serverModel.getEmail());
        clientDto.setPhone(serverModel.getPhone());
        clientDto.setAddress(serverModel.getAddress());
        clientDto.setClientType(serverModel.getClientType());
        clientDto.setRegistrationDate(serverModel.getRegistrationDate());
        // Complex associations not mapped Server->DTO for now
        return clientDto;
    }

    /**
     * Register a new client.
     */
    public String createClient(Client clientDto) throws RemoteException {
        if (this.clientService == null) throw new RemoteException("ClientService not initialized.");
        if (clientDto == null) {
            throw new IllegalArgumentException("Client DTO cannot be null");
        }
        Client_SERVER serverModel = mapClientDtoToServerModel(clientDto);
        return clientService.registerClient(serverModel);
    }

    /**
     * Update an existing client.
     */
    public String updateClient(Client clientDto) throws RemoteException {
        if (this.clientService == null) throw new RemoteException("ClientService not initialized.");
        if (clientDto == null) {
            throw new IllegalArgumentException("Client DTO cannot be null");
        }
        Client_SERVER serverModel = mapClientDtoToServerModel(clientDto);
        return clientService.updateClient(serverModel);
    }

    /**
     * Delete a client.
     */
    public String deleteClient(Client clientDto) throws RemoteException {
        if (this.clientService == null) throw new RemoteException("ClientService not initialized.");
        if (clientDto == null) {
            throw new IllegalArgumentException("Client DTO cannot be null");
        }
        Client_SERVER serverModel = mapClientDtoToServerModel(clientDto);
        return clientService.deleteClient(serverModel);
    }

    /**
     * Retrieve a client by ID, returning a client DTO.
     */
    public Client getClientById(int clientId) throws RemoteException {
        if (this.clientService == null) throw new RemoteException("ClientService not initialized.");
        Client_SERVER serverModel = clientService.getClientById(clientId);
        return mapServerModelToClientDto(serverModel);
    }

    /**
     * Retrieve all clients, returning a list of client DTOs.
     */
    public List<Client> getAllClients() throws RemoteException {
        if (this.clientService == null) throw new RemoteException("ClientService not initialized.");
        List<Client_SERVER> serverClients = clientService.getAllClients();
        List<Client> clientDtos = new ArrayList<>();
        if (serverClients != null) {
            for (Client_SERVER serverClient : serverClients) {
                clientDtos.add(mapServerModelToClientDto(serverClient));
            }
        }
        return clientDtos;
    }
}
