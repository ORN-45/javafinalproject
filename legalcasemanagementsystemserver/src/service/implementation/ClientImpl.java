/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.ClientDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Client;
import service.ClientInterface;

/**
 *
 * @author orny
 */
public class ClientImpl extends UnicastRemoteObject implements ClientInterface{
    public ClientImpl() throws RemoteException{
        super();
    }
    ClientDao dao=new ClientDao();

    @Override
    public String registerClient(Client client) throws RemoteException {
        return dao.registerClient(client);
    }

    @Override
    public String updateClient(Client client) throws RemoteException {
        return dao.updateClient(client);
        
    }

    @Override
    public String deleteClient(Client client) throws RemoteException {
       return dao.deleteClient(client);
    }

    @Override
    public List<Client> retrieveAll() throws RemoteException {
        return dao.retrieveAll();
    }
    

    @Override
    public Client retrieveById(Client client) throws RemoteException {
        return dao.retrieveById(client);
        
    }

    
}
