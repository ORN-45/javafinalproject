/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Case;

/**
 *
 * @author orny
 */
public interface CaseInterface extends Remote  {
    public String registerCase(Case cases) throws RemoteException;
        public String updateCase(Case cases) throws RemoteException;
            public String deleteCase(Case cases) throws RemoteException;
            public List<Case>retrieveAll() throws RemoteException;
            public Case retrieveById(Case cases)throws RemoteException;
        
            
           
    
    
    
    
}
