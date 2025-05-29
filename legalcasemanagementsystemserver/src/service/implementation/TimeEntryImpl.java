/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.TimeEntryDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.TimeEntry;
import service.TimeEntryInterface;

/**
 *
 * @author orny
 */
public class TimeEntryImpl extends UnicastRemoteObject implements TimeEntryInterface{
    public TimeEntryImpl() throws RemoteException{
        super();
    }
    TimeEntryDao dao=new TimeEntryDao();

    @Override
    public String registerTimeEntry(TimeEntry timeentry) throws RemoteException {
        return dao.registerTimeEntry(timeentry);
    }

    @Override
    public String updateTimeEntry(TimeEntry timeentry) throws RemoteException {
        return dao.updateTimeEntry(timeentry);
        
    }

    @Override
    public String deleteTimeEntry(TimeEntry timeentry) throws RemoteException {
       return dao.deleteTimeEntry(timeentry);
    }

    @Override
    public List<TimeEntry> retrieveAll() throws RemoteException {
        return dao.retrieveAll();
    }
    

    @Override
    public TimeEntry retrieveById(TimeEntry timeentry) throws RemoteException {
        return dao.retrieveById(timeentry);
        
    }

    
}
