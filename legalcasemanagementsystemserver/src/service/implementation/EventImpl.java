/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.EventDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Event;
import service.EventInterface;

/**
 *
 * @author orny
 */
public class EventImpl extends UnicastRemoteObject implements EventInterface{
    public EventImpl() throws RemoteException{
        super();
    }
    EventDao dao=new EventDao();

    @Override
    public String registerEvent(Event event) throws RemoteException {
        return dao.registerEvent(event);
    }

    @Override
    public String updateEvent(Event event) throws RemoteException {
        return dao.updateEvent(event);
        
    }

    @Override
    public String deleteEvent(Event event) throws RemoteException {
       return dao.deleteEvent(event);
    }

    @Override
    public List<Event> retrieveAll() throws RemoteException {
        return dao.retrieveAll();
    }
    

    @Override
    public Event retrieveById(Event event) throws RemoteException {
        return dao.retrieveById(event);
        
    }

    
}
