/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.DocumentDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Document;
import service.DocumentInterface;

/**
 *
 * @author orny
 */
public class DocumentImpl extends UnicastRemoteObject implements DocumentInterface{
    public DocumentImpl() throws RemoteException{
        super();
    }
    DocumentDao dao=new DocumentDao();

    @Override
    public String registerDocument(Document document) throws RemoteException {
        return dao.registerDocument(document);
    }

    @Override
    public String updateDocument(Document document) throws RemoteException {
        return dao.updateDocument(document);
        
    }

    @Override
    public String deleteDocument(Document document) throws RemoteException {
       return dao.deleteDocument(document);
    }

    @Override
    public List<Document> retrieveAll() throws RemoteException {
        return dao.retrieveAll();
    }
    

    @Override
    public Document retrieveById(Document document) throws RemoteException {
        return dao.retrieveById(document);
        
    }

    
}
