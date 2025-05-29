/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Event;

/**
 *
 * @author orny
 */
public interface EventInterface extends Remote  {
    public String registerEvent(Event event) throws RemoteException;
        public String updateEvent(Event event) throws RemoteException;
            public String deleteEvent(Event event) throws RemoteException;
            public List<Event>retrieveAll() throws RemoteException;
            public Event retrieveById(Event event)throws RemoteException;
        
            
           
    
    
    
    
}
