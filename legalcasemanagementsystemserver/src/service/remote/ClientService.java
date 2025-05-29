package service.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Client; // Server-side model.Client

public interface ClientService extends Remote {
    String registerClient(Client client) throws RemoteException;
    String updateClient(Client client) throws RemoteException;
    String deleteClient(Client client) throws RemoteException;
    List<Client> getAllClients() throws RemoteException;
    Client getClientById(int clientId) throws RemoteException;
}
