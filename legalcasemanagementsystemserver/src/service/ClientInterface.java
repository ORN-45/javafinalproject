/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Client;

/**
 *
 * @author orny
 */
public interface ClientInterface extends Remote  {
    public String registerClient(Client Clients) throws RemoteException;
        public String updateClient(Client Clients) throws RemoteException;
            public String deleteClient(Client Clients) throws RemoteException;
            public List<Client>retrieveAll() throws RemoteException;
            public Client retrieveById(Client Clients)throws RemoteException;
        
            
           
    
    
    
    
}
