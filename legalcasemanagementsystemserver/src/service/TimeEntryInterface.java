/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.TimeEntry;

/**
 *
 * @author orny
 */
public interface TimeEntryInterface extends Remote  {
    public String registerTimeEntry(TimeEntry timeentry) throws RemoteException;
        public String updateTimeEntry(TimeEntry timeentry) throws RemoteException;
            public String deleteTimeEntry(TimeEntry timeentry) throws RemoteException;
            public List<TimeEntry>retrieveAll() throws RemoteException;
            public TimeEntry retrieveById(TimeEntry timeentry)throws RemoteException;
        
            
           
    
    
    
    
}
