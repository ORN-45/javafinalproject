/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Document;

/**
 *
 * @author orny
 */
public interface DocumentInterface extends Remote  {
    public String registerDocument(Document document) throws RemoteException;
        public String updateDocument(Document document) throws RemoteException;
            public String deleteDocument(Document document) throws RemoteException;
            public List<Document>retrieveAll() throws RemoteException;
            public Document retrieveById(Document document)throws RemoteException;
        
            
           
    
    
    
    
}
