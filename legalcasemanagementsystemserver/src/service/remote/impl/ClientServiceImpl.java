package service.remote.impl;

import dao.ClientDao;
import model.Client;
import service.remote.ClientService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientServiceImpl extends UnicastRemoteObject implements ClientService {

    private final ClientDao clientDao;

    public ClientServiceImpl() throws RemoteException {
        super();
        this.clientDao = new ClientDao();
    }

    @Override
    public String registerClient(Client client) throws RemoteException {
        return clientDao.registerClient(client);
    }

    @Override
    public String updateClient(Client client) throws RemoteException {
        return clientDao.updateClient(client);
    }

    @Override
    public String deleteClient(Client client) throws RemoteException {
        return clientDao.deleteClient(client);
    }

    @Override
    public List<Client> getAllClients() throws RemoteException {
        return clientDao.retrieveAll();
    }

    @Override
    public Client getClientById(int clientId) throws RemoteException {
        Client tempClient = new Client();
        tempClient.setId(clientId); // Assuming setId exists in model.Client
        return clientDao.retrieveById(tempClient); // Assuming retrieveById takes a Client object
    }
}
