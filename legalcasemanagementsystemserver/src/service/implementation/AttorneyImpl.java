/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.AttorneyDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Attorney;
import service.AttorneyInterface;

/**
 *
 * @author orny
 */
public class AttorneyImpl extends UnicastRemoteObject implements AttorneyInterface{
    public AttorneyImpl() throws RemoteException{
        super();
    }
    AttorneyDao dao=new AttorneyDao();

    @Override
    public String registerAttorney(Attorney attorney) throws RemoteException {
        return dao.registerAttorney(attorney);
    }

    @Override
    public String updateAttorney(Attorney attorney) throws RemoteException {
        return dao.updateAttorney(attorney);
        
    }

    @Override
    public String deleteAttorney(Attorney attorney) throws RemoteException {
       return dao.deleteAttorney(attorney);
    }

    @Override
    public List<Attorney> retrieveAll() throws RemoteException {
        return dao.retrieveAll();
    }
    

    @Override
    public Attorney retrieveById(Attorney attorney) throws RemoteException {
        return dao.retrieveById(attorney);
        
    }

    
}
