/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.CaseDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Case;
import service.CaseInterface;

/**
 *
 * @author orny
 */
public class CaseImpl extends UnicastRemoteObject implements CaseInterface{
    public CaseImpl() throws RemoteException{
        super();
    }
    CaseDao dao=new CaseDao();

    @Override
    public String registerCase(Case cases) throws RemoteException {
        return dao.registerCase(cases);
    }

    @Override
    public String updateCase(Case cases) throws RemoteException {
       return dao.updateCase(cases);
    }

    @Override
    public String deleteCase(Case cases) throws RemoteException {
       return dao.deleteCase(cases);
    }

    @Override
    public List<Case> retrieveAll() throws RemoteException {
        return dao.retrieveAll();
    }

    @Override
    public Case retrieveById(Case cases) throws RemoteException {
        return dao.retrieveById(cases);
    }

    
   
    
}
