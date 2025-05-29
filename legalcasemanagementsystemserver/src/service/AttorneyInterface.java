/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Attorney;

/**
 *
 * @author orny
 */
public interface AttorneyInterface extends Remote  {
    public String  registerAttorney(Attorney attorney) throws RemoteException;
        public String  updateAttorney(Attorney attorney) throws RemoteException;
            public String deleteAttorney(Attorney attorney) throws RemoteException;
            public List<Attorney>retrieveAll() throws RemoteException;
            public Attorney retrieveById(Attorney attorney)throws RemoteException;
        
            
           
    
    
    
    
}
